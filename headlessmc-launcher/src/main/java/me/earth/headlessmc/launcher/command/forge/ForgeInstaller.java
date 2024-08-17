package me.earth.headlessmc.launcher.command.forge;

import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.instrumentation.ResourceExtractor;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.launch.SimpleInMemoryLauncher;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Getter
@CustomLog
@RequiredArgsConstructor
public class ForgeInstaller {
    private static final String FORGE_CLI = "forge-cli.jar";

    private final SimpleInMemoryLauncher inMemoryLauncher = new ForgeInMemoryLauncher();
    private final ForgeRepoFormat repoFormat;
    private final Launcher launcher;
    private final String forgeName;
    private final String baseUrl;

    public void install(ForgeVersion version, FileManager fileManager, boolean inMemory) throws IOException {
        val cli = new ResourceExtractor(fileManager, FORGE_CLI).extract();
        val fileName = repoFormat.getFileName(version);
        // TODO: delete installer?
        val installer = fileManager.create(fileName);
        downloadInstaller(version, installer);

        val java = inMemory ? launcher.getJavaService().getCurrent() : launcher.getJavaService().findBestVersion(8);
        if (java == null) {
            throw new IOException("No Java version found! Please configure hmc.java.versions.");
        }

        val mc = launcher.getMcFiles();
        val command = getCommand(java, mc.getBase(), cli, installer, inMemory);

        ensureJsonExists("launcher_profiles.json", mc);
        ensureJsonExists("launcher_profiles_microsoft_store.json", mc);

        if (inMemory) {
            try {
                // Our ForgeCLI does the following:
                // try (URLClassLoader ucl = new InstallerClassLoader(new URL[]{
                //                Main.class.getProtectionDomain().getCodeSource().getLocation(),
                // When running inside an IDE Main.class.getProtectionDomain().getCodeSource().getLocation() is null!
                inMemoryLauncher.simpleLaunch(new URL[] { cli.toURI().toURL() }, inMemoryLauncher.getMainClassFromJar(cli), command);
                launcher.log(String.format("%s %s installed successfully!", forgeName, version.getFullName()));
            } catch (Throwable t) {
                throw new IOException(t);
            }
        } else {
            Process process = new ProcessBuilder()
                    .directory(fileManager.getBase())
                    .command(command)
                    .inheritIO()
                    .start();
            try {
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new IOException(String.format("Failed to install Forge %s, exitCode: %d", version.getFullName(), exitCode));
                } else {
                    launcher.log(String.format("%s %s installed successfully!", forgeName, version.getFullName()));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread has been interrupted!");
                throw new IllegalStateException(e);
            }
        }
    }

    protected List<String> getCommand(Java java, File mc, File cli, File fml, boolean inMemory) {
        val command = new ArrayList<String>();
        if (!inMemory) {
            command.add(java.getExecutable());
            command.add("-jar");
            command.add(cli.getAbsolutePath());
        }

        command.add("--installer");
        command.add(fml.getAbsolutePath());
        command.add("--target");
        command.add(mc.getAbsolutePath());
        return command;
    }

    protected void downloadInstaller(ForgeVersion version, File file) throws IOException {
        String url = repoFormat.getUrl(baseUrl, version);
        log.debug("Downloading Installer from " + url);
        try {
            IOUtil.download(url, file.getAbsolutePath());
        } catch (IOException e) {
            log.debug("Failed to download Forge from " + url + ": " + e.getMessage());
            url = baseUrl + version.getFullName() + "-" + version.getVersion()
                + "/forge-" + version.getFullName() + "-" + version.getVersion()
                + "-installer.jar";
            log.debug("Downloading from forge from " + url);
            IOUtil.download(url, file.getAbsolutePath());
        }
    }

    private void ensureJsonExists(String name, FileManager mcFiles) throws IOException {
        val file = mcFiles.create(name);
        try {
            if (!JsonUtil.fromFile(file).isJsonObject()) {
                throw new IOException("Not a JsonObject!");
            }
        } catch (IOException ioException) {
            log.debug("Writing in " + name);
            Files.write(file.toPath(), "{\"profiles\": {}}".getBytes(StandardCharsets.UTF_8));
        }
    }

}
