package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.instrumentation.Instrumentation;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationHelper;
import me.earth.headlessmc.launcher.instrumentation.Target;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.version.Features;
import me.earth.headlessmc.launcher.version.Rule;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

@Getter
@CustomLog
@RequiredArgsConstructor
public class ProcessFactory {
    private final FileManager files;
    private final HasConfig config;
    private final OS os;

    public @Nullable Process run(LaunchOptions options)
        throws LaunchException, AuthException, IOException {
        val instrumentation = InstrumentationHelper.create(options);
        return run(options, instrumentation);
    }

    public @Nullable Process run(LaunchOptions options, Instrumentation instrumentation)
        throws IOException, LaunchException, AuthException {
        val launcher = options.getLauncher();

        log.debug("Creating version merger");
        val version = new VersionMerger(options.getVersion());
        if (version.getArguments() == null) {
            throw new LaunchException(
                version.getName() + ": Version file and its parents" +
                    " didn't contain arguments.");
        }

        log.debug("Creating extraction directory");
        val dlls = options.getFiles().createRelative("extracted");
        val targets = processLibraries(version, dlls);
        addGameJar(version, targets);
        List<String> classpath = instrumentation.instrument(targets);
        if (options.isRuntime()) {
            String runtimeJar = null;
            for (String path : classpath) {
                if (path.endsWith(InstrumentationHelper.RUNTIME_JAR)) {
                    runtimeJar = path;
                    classpath.remove(path);
                    break;
                }
            }

            if (runtimeJar == null) {
                throw new IllegalStateException("Failed to find RuntimeJar in classpath " + classpath);
            }
            // add RuntimeJar as the first jar on the classpath
            // this makes java look it up for libraries first
            // really important because forge provides an incompatible version of JLine.
            // TODO: this works, but is it really something we want to trust?
            //  bring over the VersionAgnosticJLineCommandLineReader from hmc-specifics?
            classpath.add(0, runtimeJar);
        }

        log.debug("Building command");
        val commandBuilder = JavaLaunchCommandBuilder.builder()
                             .account(options.getAccount())
                             .classpath(classpath)
                             .os(os)
                             .jvmArgs(options.getAdditionalJvmArgs())
                             .natives(dlls.getBase().getAbsolutePath())
                             .runtime(options.isRuntime())
                             .version(version)
                             .launcher(launcher)
                             .inMemory(options.isInMemory())
                             .lwjgl(options.isLwjgl())
                             .build();

        val command = commandBuilder.build();
        downloadAssets(files, version);
        debugCommand(command, commandBuilder);

        val dir = new File(launcher.getConfig().get(LauncherProperties.GAME_DIR, launcher.getMcFiles().getPath()));
        log.info("Game will run in " + dir);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        if (options.isPrepare()) {
            return null;
        }

        if (options.isInMemory()) {
            inMemoryLaunch(new InMemoryLauncher(options, commandBuilder, version, launcher.getJavaService().getCurrent()));
            return null;
        }

        return this.run(new ProcessBuilder()
            .command(command)
            .directory(dir)
            .redirectError(options.isNoOut()
                               ? ProcessBuilder.Redirect.PIPE
                               : ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(options.isNoOut()
                                ? ProcessBuilder.Redirect.PIPE
                                : ProcessBuilder.Redirect.INHERIT)
            .redirectInput(options.isNoIn()
                               ? ProcessBuilder.Redirect.PIPE
                               : ProcessBuilder.Redirect.INHERIT));
    }

    protected void inMemoryLaunch(InMemoryLauncher inMemoryLauncher) throws LaunchException, AuthException, IOException {
        inMemoryLauncher.launch();
    }

    private void addGameJar(Version version, List<Target> targets) throws IOException {
        File gameJar = new File(version.getFolder(), version.getName() + ".jar");
        log.debug("GameJar: " + gameJar.getAbsolutePath());
        if (!gameJar.exists() || !checkZipIntact(gameJar) && gameJar.delete()) {
            log.info("Downloading " + version.getName() + " from " + version.getClientDownload());
            download(version.getClientDownload(), gameJar.getAbsolutePath());
        }

        targets.add(new Target(true, gameJar.getAbsolutePath()));
        log.debug("Processed GameJar");
    }

    private List<Target> processLibraries(Version version, FileManager dlls) throws IOException {
        log.debug("Processing libraries...");
        // TODO: proper features
        val features = Features.EMPTY;
        val targets = new ArrayList<Target>(version.getLibraries().size());
        Set<String> libPaths = new HashSet<>();
        for (val library : version.getLibraries()) {
            if (library.getRule().apply(os, features) == Rule.Action.ALLOW) {
                log.debug("Checking: " + library);
                String libPath = library.getPath(os);
                if (!libPaths.add(libPath)) {
                    continue;
                }

                val path = files.getDir("libraries") + File.separator + libPath;
                if (!new File(path).exists()) {
                    String url = library.getUrl(libPath);
                    log.info(libPath + " is missing, downloading from " + url);
                    download(url, path);
                }

                library.getExtractor().extract(path, dlls);
                if (!library.isNativeLibrary()) {
                    targets.add(new Target(false, path));
                }
            } else {
                log.debug("Ignoring: " + library.getName());
            }
        }

        log.debug("Finished processing libraries");
        return targets;
    }

    protected boolean checkZipIntact(File file) {
        val name = file.getName();
        boolean result = true;
        if (name.endsWith(".jar") || name.endsWith(".zip")) {
            try {
                val zipFile = new ZipFile(file);
                zipFile.close();
            } catch (IOException e) {
                log.error("Couldn't read " + name + " : " + e.getMessage());
                result = false;
            }
        }

        return result;
    }

    protected Process run(ProcessBuilder builder) throws IOException {
        return builder.start();
    }

    protected void downloadAssets(FileManager files, Version version) throws IOException {
        log.debug("Downloading Assets");
        new AssetsDownloader(files, config, version.getAssetsUrl(), version.getAssets()).download();
    }

    protected void download(String from, String to) throws IOException {
        IOUtil.download(from, to);
    }

    private void debugCommand(List<String> command, JavaLaunchCommandBuilder commandBuilder) {
        StringBuilder commandDebugBuilder = new StringBuilder();
        if (!command.isEmpty()) {
            commandDebugBuilder.append("\"").append(command.get(0)).append("\" "); // escape java path
        }

        for (int i = 1; i < command.size(); i++) {
            if (commandBuilder.getAccount().getToken().equals(command.get(i))) {
                commandDebugBuilder.append("********").append((i == command.size() - 1) ? "" : " ");
            } else {
                commandDebugBuilder.append(command.get(i)).append((i == command.size() - 1) ? "" : " ");
            }
        }

        log.debug(commandDebugBuilder.toString());
    }

}
