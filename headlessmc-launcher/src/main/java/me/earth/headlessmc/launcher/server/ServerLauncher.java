package me.earth.headlessmc.launcher.server;

import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.command.download.VersionInfo;
import me.earth.headlessmc.launcher.command.download.VersionInfoUtil;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Setter
@CustomLog
@RequiredArgsConstructor
public class ServerLauncher {
    private final Launcher launcher;
    private final Server server;

    private boolean noOut = false;
    private boolean noIn = false;
    private @Nullable String eula;

    public String readEula() throws IOException {
        try (BufferedReader br = IOUtil.reader(Files.newInputStream(server.getEula()))) {
            eula = IOUtil.read(br, true);
            return eula;
        }
    }

    public void acceptEula() throws IOException {
        if (eula == null) {
            eula = readEula();
        }

        eula = eula.replace("eula=false", "eula=true");
        Files.write(server.getEula(), eula.getBytes(StandardCharsets.UTF_8));
    }

    public void eulaLaunch() throws CommandException {
        if (!server.hasEula()) {
            if (launcher.getConfig().get(LauncherProperties.SERVER_LAUNCH_FOR_EULA, true)) {
                log.info("Launching server to create EULA...");
                Process process = launch0();
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    throw new CommandException(e);
                }
            }

            if (!server.hasEula()) {
                throw new CommandException("EULA could not be found for server " + server.getName());
            }
        }
    }

    public Process launch() throws CommandException {
        eulaLaunch();
        if (launcher.getConfig().get(LauncherProperties.SERVER_ACCEPT_EULA, false)) {
            try {
                log.info("Accepting EULA...");
                acceptEula();
            } catch (IOException e) {
                throw new CommandException("EULA could not be accepted for server " + server.getName(), e);
            }
        }

        return launch0();
    }

    private Process launch0() throws CommandException {
        Path serverJar = server.getPath().resolve("server.jar");
        if (!Files.exists(serverJar)) {
            try {
                launcher.getServerManager().remove(server);
            } catch (IOException e) {
                log.error("Failed to delete server without server jar at " + server.getPath(), e);
            }

            throw new CommandException("Server " + server.getName() + " has no valid server jar!");
        }

        Java java = getJava(server);
        ProcessBuilder processBuilder = new ProcessBuilder()
                .command(java.getExecutable(), "-jar", serverJar.toAbsolutePath().toString())
                .directory(server.getPath().toFile())
                .redirectError(noOut
                        ? ProcessBuilder.Redirect.PIPE
                        : ProcessBuilder.Redirect.INHERIT)
                .redirectOutput(noOut
                        ? ProcessBuilder.Redirect.PIPE
                        : ProcessBuilder.Redirect.INHERIT)
                .redirectInput(noIn
                        ? ProcessBuilder.Redirect.PIPE
                        : ProcessBuilder.Redirect.INHERIT);
        try {
            return processBuilder.start();
        } catch (IOException e) {
            throw new CommandException(e);
        }
    }

    private Java getJava(Server server) throws CommandException {
        VersionInfo versionInfo = launcher.getVersionInfoCache().getByName(server.getVersion().getVersion());
        if (versionInfo == null) {
            throw new CommandException("Failed to find version '" + server.getVersion().getVersion() + "'!");
        }

        Version version;
        try {
            version = VersionInfoUtil.toVersion(versionInfo, launcher.getVersionService(), launcher.getDownloadService());
        } catch (IOException e) {
            throw new CommandException("Failed to read Version " + versionInfo.getName(), e);
        }

        Java java = launcher.getJavaService().findBestVersion(launcher, version.getJava());
        if (java == null) {
            throw new CommandException("Failed to find Java version for "
                    + versionInfo.getName() + ", required: " + version.getJava());
        }

        return java;
    }

}
