package me.earth.headlessmc.graalvm;

import me.earth.headlessmc.api.HeadlessMcApi;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.java.download.JavaDownloadRequest;
import me.earth.headlessmc.java.download.JavaDownloaderManager;
import me.earth.headlessmc.jline.JlineProgressbarProvider;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.files.AutoConfiguration;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.logging.LoggingService;
import me.earth.headlessmc.os.OS;
import me.earth.headlessmc.os.OSFactory;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final Path HEADLESSMC_PATH = Paths.get(HeadlessMcApi.NAME);

    public static void main(String[] args) throws IOException {
        LoggingService service = new LoggingService();
        service.setFileHandler(false);
        service.init();

        FileManager fileManager = new FileManager(HEADLESSMC_PATH.toAbsolutePath().toString());
        AutoConfiguration.runAutoConfiguration(fileManager);
        ConfigService configs = Service.refresh(new ConfigService(fileManager));
        OS os = OSFactory.detect(configs.getConfig());

        Java java = findJava(configs, os);
        runHmcOn(java, args);
    }

    private static void runHmcOn(Java java, String[] args) throws IOException {
        Path launcherWrapper = HEADLESSMC_PATH.resolve("headlessmc-launcher-wrapper.jar");
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("headlessmc/headlessmc-launcher-wrapper.jar");
             OutputStream outputStream = Files.newOutputStream(launcherWrapper)) {
            Objects.requireNonNull(inputStream, "Failed to find headlessmc-launcher-wrapper resource").transferTo(outputStream);
        }

        RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = new ArrayList<>();
        arguments.add(java.getExecutable());
        arguments.addAll(mx.getInputArguments());
        arguments.add("-jar");
        arguments.add(launcherWrapper.toAbsolutePath().toString());
        arguments.addAll(Arrays.asList(args));

        Process process = new ProcessBuilder(arguments)
                .inheritIO()
                .directory(Paths.get("").toAbsolutePath().toFile())
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process.isAlive()) {
                process.destroy();
            }
        }));

        try {
            System.exit(process.waitFor());
        } catch (InterruptedException e) {
            if (process.isAlive()) {
                process.destroy();
            }

            throw new IllegalStateException(e);
        }
    }

    private static Java findJava(ConfigService configs, OS os) throws IOException {
        int requestedVersion = configs.getConfig().get(GraalProperties.JAVA_VERSION, -1L).intValue();
        String distribution = configs.getConfig().get(GraalProperties.JAVA_DISTRIBUTION, JavaDownloaderManager.DEFAULT_DISTRIBUTION);
        boolean forceDownload = configs.getConfig().get(GraalProperties.FORCE_DOWNLOAD, false);

        JavaService javaService = Service.refresh(new JavaService(configs, os));
        Java java = findJava(javaService, requestedVersion);
        if (java == null || forceDownload) {
            int versionToDownload = requestedVersion == -1 ? 21 : requestedVersion;
            JavaDownloaderManager downloaderManager = JavaDownloaderManager.getDefault();
            JavaDownloadRequest request = new JavaDownloadRequest(
                    new Java11DownloadClient(),
                    new JlineProgressbarProvider(),
                    versionToDownload,
                    distribution,
                    os,
                    configs.getConfig().get(GraalProperties.JDK, false)
            );

            downloaderManager.download(HEADLESSMC_PATH.resolve("java"), request);
            javaService.refreshHeadlessMcJavaVersions();
            java = findJava(javaService, requestedVersion);
            if (java == null) {
                throw new IllegalStateException("Failed to install Java " + requestedVersion + " (" + distribution + ")");
            }
        }

        return java;
    }

    private static @Nullable Java findJava(JavaService javaService, int requestedVersion) {
        List<Java> javas = new ArrayList<>();
        javaService.forEach(javas::add);
        javas.removeIf(java -> java.getVersion() < 8);
        javas.removeIf(Java::isCurrent);
        Collections.sort(javas);

        return requestedVersion == -1
                ? javas.stream().reduce((first, second) -> second).orElse(null) // find last, with the newest java version
                : javas.stream().filter(j -> j.getVersion() == requestedVersion).findFirst().orElse(null);
    }

}
