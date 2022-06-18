package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.Launcher;
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
import java.util.List;
import java.util.zip.ZipFile;

@CustomLog
@RequiredArgsConstructor
public class ProcessFactory {
    private final FileManager files;
    private final OS os;

    public Process run(Version version, Launcher launcher, FileManager files,
                       boolean runtime, boolean lwjgl, boolean jndi,
                       boolean lookup, boolean paulscode, boolean noOut)
        throws LaunchException, AuthException, IOException {
        val instrumentation = InstrumentationHelper.create(
            files, lwjgl, runtime, jndi, lookup, paulscode);
        return run(version, instrumentation, launcher, files, runtime, noOut);
    }

    public Process run(Version version, Instrumentation instrumentation,
                       Launcher launcher, FileManager fileManager,
                       boolean runtime, boolean noOut)
        throws IOException, LaunchException, AuthException {
        version = new VersionMerger(version);
        if (version.getArguments() == null) {
            throw new LaunchException(
                version.getName() + ": Version file and its parents" +
                    " didn't contain arguments.");
        }

        new AssetsDownloader(files, version.getAssetsUrl(), version.getAssets())
            .download();

        val dlls = fileManager.createRelative("extracted");
        val targets = processLibraries(version, dlls);
        addGameJar(version, targets);
        val command = Command.builder()
                             .classpath(instrumentation.instrument(targets))
                             .os(os)
                             .natives(dlls.getBase().getAbsolutePath())
                             .runtime(runtime)
                             .version(version)
                             .launcher(launcher)
                             .build()
                             .build();

        log.debug(command.toString());
        val dir = launcher.getConfig().get(LauncherProperties.GAME_DIR,
                                           launcher.getMcFiles().getPath());
        log.info("Game will run in " + dir);
        return new ProcessBuilder()
            .command(command)
            .directory(new File(dir))
            .redirectError(noOut
                               ? ProcessBuilder.Redirect.PIPE
                               : ProcessBuilder.Redirect.INHERIT)
            .redirectOutput(noOut
                                ? ProcessBuilder.Redirect.PIPE
                                : ProcessBuilder.Redirect.INHERIT)
            .redirectInput(ProcessBuilder.Redirect.INHERIT)
            .start();
    }

    private void addGameJar(Version version, List<Target> targets)
        throws IOException {
        var gameJar = new File(version.getFolder(), version.getName() + ".jar");
        log.debug("GameJar: " + gameJar.getAbsolutePath());
        if (!gameJar.exists() || !checkZipIntact(gameJar) && gameJar.delete()) {
            log.info("Downloading " + version.getName() + " from "
                         + version.getClientDownload());
            IOUtil.download(version.getClientDownload(),
                            gameJar.getAbsolutePath());
        }

        targets.add(new Target(true, gameJar.getAbsolutePath()));
    }

    private List<Target> processLibraries(Version version, FileManager dlls)
        throws IOException {
        // TODO: proper features
        val features = Features.EMPTY;
        val targets = new ArrayList<Target>(version.getLibraries().size());
        for (val library : version.getLibraries()) {
            if (library.getRule().apply(os, features) == Rule.Action.ALLOW) {
                String libPath = library.getPath(os);
                val path = files.getDir("libraries") + File.separator + libPath;
                if (!new File(path).exists()) {
                    String url = library.getUrl(libPath);
                    log.info(libPath + " is missing, downloading from " + url);
                    IOUtil.download(url, path);
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

    private boolean checkZipIntact(File file) {
        val name = file.getName();
        var result = true;
        if (name.endsWith(".jar") || name.endsWith(".zip")) {
            try {
                new ZipFile(file);
            } catch (IOException e) {
                log.error("Couldn't read " + name + " : " + e.getMessage());
                result = false;
            }
        }

        return result;
    }

}
