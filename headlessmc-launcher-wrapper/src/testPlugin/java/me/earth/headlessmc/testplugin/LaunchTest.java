package me.earth.headlessmc.testplugin;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.version.Version;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaunchTest {
    public static void build(Java java, Launcher launcher, TestInputStream is) {
        String vanilla = java.getVersion() <= 8 ? "1.12.2" : (java.getVersion() <= 17 ? "1.20.6" : "1.21");
        String modlauncher = java.getVersion() <= 8 ? "forge" : "fabric";

        is.add("help");

        is.add("versions");

        is.add("download " + vanilla);

        is.add("login");

        is.add("login -cancel 0");

        is.add("login -webview");

        is.add("login -cancel 0");

        is.add("login test.mail@test.com");

        is.add("abort");

        is.add(modlauncher + " " + vanilla);

        is.add("multi \"json " + vanilla + "\" versions");

        is.add("java");

        is.add("java -current");

        is.add("memory");

        is.add("loglevel INFO");

        is.add("offline true");

        is.add(ps -> {
            System.out.println(launcher.getVersionService().stream().map(HasName::getName).collect(Collectors.toList()));
            Optional<Version> version = launcher.getVersionService().stream().filter(v -> v.getName().toLowerCase(Locale.ENGLISH).contains(modlauncher)).findFirst();
            assertTrue(version.isPresent());
            ps.println("specifics " + version.get().getId() + " mc-runtime-test -id");
        });

        AtomicBoolean returnedFromLaunching = new AtomicBoolean();
        is.add(ps -> {
            Optional<Version> version = launcher.getVersionService().stream().filter(v -> v.getName().toLowerCase(Locale.ENGLISH).contains(modlauncher)).findFirst();
            assertTrue(version.isPresent());
            Thread timeOutThread = new Thread(() -> {
                try {
                    Thread.sleep(TimeUnit.MINUTES.toMillis(10));
                    if (!returnedFromLaunching.get()) {
                        System.exit(-1);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            ps.println("launch " + version.get().getId() + " -id -lwjgl");
        });

        is.add(ps -> returnedFromLaunching.set(true));
    }

}
