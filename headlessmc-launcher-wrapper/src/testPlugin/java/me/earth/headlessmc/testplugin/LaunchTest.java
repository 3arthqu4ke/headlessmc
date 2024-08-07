package me.earth.headlessmc.testplugin;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.version.Version;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaunchTest {
    public static void build(Java java, Launcher launcher, TestInputStream is) {
        String vanilla = java.getVersion() <= 8 ? "1.12.2" : (java.getVersion() <= 17 ? "1.20.4" : "1.21");
        String modlauncher = java.getVersion() <= 8 ? "forge" : "neoforge";
        boolean inMemory = Boolean.parseBoolean(System.getProperty("integrationTestRunInMemory", "false"));
        String inMemoryFlag = inMemory ? "-inmemory" : "";

        is.add("help");

        is.add("plugins");

        is.add("versions");

        is.add("download 1.12.2");
        is.add("n"); // potentially if already downloaded
        is.add(ps -> assertTrue(launcher.getVersionService().stream().anyMatch(v -> "1.12.2".equals(v.getName()))));

        is.add("download 1.20.4");
        is.add("n"); // potentially if already downloaded
        is.add(ps -> assertTrue(launcher.getVersionService().stream().anyMatch(v -> "1.20.4".equals(v.getName()))));

        is.add("download 1.21");
        is.add("n"); // potentially if already downloaded
        is.add(ps -> assertTrue(launcher.getVersionService().stream().anyMatch(v -> "1.21".equals(v.getName()))));

        is.add("versions");

        is.add("login");

        is.add("login -cancel 0");

        is.add("login -webview");

        is.add("login -cancel 0");

        is.add("login test.mail@test.com");

        is.add("abort");

        if (java.getVersion() > 8) {
            is.add("fabric " + vanilla);
            is.add("n");
            is.add(ps -> assertTrue(launcher.getVersionService().stream().anyMatch(v -> v.getName().toLowerCase(Locale.ENGLISH).contains("fabric"))));
        }

        is.add(modlauncher + " " + vanilla);
        is.add("n");
        is.add(ps -> assertTrue(launcher.getVersionService().stream().anyMatch(v -> v.getName().toLowerCase(Locale.ENGLISH).contains(modlauncher))));

        is.add("multi \"json " + vanilla + "\" versions");

        is.add("java");

        is.add("java -current");

        is.add("memory");

        is.add("loglevel INFO");

        is.add("offline true");

        is.add(ps -> {
            Optional<Version> version = launcher
                .getVersionService()
                .stream()
                .filter(v -> v.getName().toLowerCase(Locale.ENGLISH).contains(modlauncher) && v.getParentName().equals(vanilla)).findFirst();

            assertTrue(version.isPresent(), "Failed to find a version with name containing " + modlauncher + " in "
                + launcher.getVersionService().stream().map(HasName::getName).collect(Collectors.toList()));
            ps.println("specifics " + version.get().getId() + " mc-runtime-test -id");
        });

        AtomicBoolean returnedFromLaunching = new AtomicBoolean();
        is.add(ps -> {
            Optional<Version> version = launcher
                .getVersionService()
                .stream()
                .filter(v -> v.getName().toLowerCase(Locale.ENGLISH).contains(modlauncher) && v.getParentName().equals(vanilla)).findFirst();

            assertTrue(version.isPresent());
            Thread timeOutThread = new Thread(() -> {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(10));
                    if (!returnedFromLaunching.get()) {
                        System.exit(-1);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            });

            timeOutThread.start();
            assertTrue(launcher.getAccountManager().getOfflineChecker().isOffline());
            if (inMemory) {
                ExitTrap.trapExit();
            }

            ps.println("launch " + version.get().getId() + " -id -lwjgl -stay " + inMemoryFlag);
        });

        // for some reason test might hang here?
        is.add(PrintStream::println);
        is.add(PrintStream::flush);

        is.add(ps -> returnedFromLaunching.set(true));
        is.add(ps -> ExitTrap.remove());
    }

}
