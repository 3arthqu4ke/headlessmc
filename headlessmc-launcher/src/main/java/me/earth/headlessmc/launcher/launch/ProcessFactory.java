package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

@CustomLog
@RequiredArgsConstructor
public class ProcessFactory {
    private final FileManager files;
    private final HasConfig config;
    private final OS os;

    public Process run(LaunchOptions options)
        throws LaunchException, AuthException, IOException {
        val instrumentation = InstrumentationHelper.create(options);
        return run(options, instrumentation);
    }

    public Process run(LaunchOptions options, Instrumentation instrumentation)
        throws IOException, LaunchException, AuthException {
        val launcher = options.getLauncher();
        if (launcher.getAccountManager().getLastAccount() == null) {
            launcher.getAccountManager().login(launcher.getConfig());
        }

        val version = new VersionMerger(options.getVersion());
        if (version.getArguments() == null) {
            throw new LaunchException(
                version.getName() + ": Version file and its parents" +
                    " didn't contain arguments.");
        }

        val dlls = options.getFiles().createRelative("extracted");
        val targets = processLibraries(version, dlls);
        addGameJar(version, targets);
        val command = Command.builder()
                             .classpath(instrumentation.instrument(targets))
                             .os(os)
                             .jvmArgs(options.getAdditionalJvmArgs())
                             .natives(dlls.getBase().getAbsolutePath())
                             .runtime(options.isRuntime())
                             .version(version)
                             .launcher(launcher)
                             .lwjgl(options.isLwjgl())
                             .build()
                             .build();

        downloadAssets(files, version);
        log.debug(command.toString());
        val dir = new File(launcher.getConfig().get(
            LauncherProperties.GAME_DIR, launcher.getMcFiles().getPath()));
        log.info("Game will run in " + dir);
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
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

    private void addGameJar(Version version, List<Target> targets)
        throws IOException {
        var gameJar = new File(version.getFolder(), version.getName() + ".jar");
        log.debug("GameJar: " + gameJar.getAbsolutePath());
        if (!gameJar.exists() || !checkZipIntact(gameJar) && gameJar.delete()) {
            log.info("Downloading " + version.getName() + " from "
                         + version.getClientDownload());
            download(version.getClientDownload(), gameJar.getAbsolutePath());
        }

        targets.add(new Target(true, gameJar.getAbsolutePath()));
    }

    private List<Target> processLibraries(Version version, FileManager dlls)
        throws IOException {
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

        return targets;
    }

    protected boolean checkZipIntact(File file) {
        val name = file.getName();
        var result = true;
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

    protected void downloadAssets(FileManager files, Version version)
        throws IOException {
        new AssetsDownloader(files, config, version.getAssetsUrl(), version.getAssets())
            .download();
    }

    protected void download(String from, String to) throws IOException {
        IOUtil.download(from, to);
    }

}
