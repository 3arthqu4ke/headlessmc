package me.earth.headlessmc.launcher.launch;

import lombok.*;
import me.earth.headlessmc.config.HmcProperties;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.auth.LaunchAccount;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.version.Features;
import me.earth.headlessmc.launcher.version.Version;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Getter
@CustomLog
class Command {
    private static final String RT_MAIN = "me.earth.headlessmc.runtime.Main";

    private final LaunchAccount account;
    private final List<String> classpath;
    private final List<String> jvmArgs;
    private final Launcher launcher;
    private final Version version;
    private final String natives;
    private final boolean runtime;
    private final boolean lwjgl;
    private final boolean inMemory;
    private final OS os;

    public List<String> build() throws LaunchException, AuthException {
        val config = launcher.getConfig();
        var java = launcher.getJavaService().findBestVersion(version.getJava());
        if (inMemory) {
            Java current = Java.current();
            if (current.getVersion() != version.getJava()) {
                if (launcher.getConfig().get(LauncherProperties.IN_MEMORY_REQUIRE_CORRECT_JAVA, true)) {
                    throw new LaunchException("Running in memory with java version "
                                                  + current.getVersion()
                                                  + " but minecraft needs "
                                                  + version.getJava());
                }

                log.warning("Running in memory with java version "
                                + current.getVersion()
                                + " but minecraft needs "
                                + version.getJava());
            } else {
                log.info("Running with Minecraft in memory in this JVM.");
            }

            java = current;
        } else if (java == null) {
            throw new LaunchException("Couldn't find Java version for "
                                          + version.getName()
                                          + ", requires Java "
                                          + version.getJava());
        }

        val result = new ArrayList<String>();
        result.add(java.getExecutable());
        result.addAll(Arrays.asList(config.get(LauncherProperties.JVM_ARGS, new String[0])));
        if (config.get(LauncherProperties.SET_LIBRARY_DIR, true)) {
            result.add("-DlibraryDirectory=" + launcher.getMcFiles().getDir("libraries").getAbsolutePath());
        }

        result.addAll(jvmArgs);

        if (runtime
            && java.getVersion() > 8
            && config.get(HmcProperties.DEENCAPSULATE, true)) {
            log.info("Java version > 8 detected, deencapsulating!");
            result.add("-D" + HmcProperties.DEENCAPSULATE.getName() + "=true");
        }

        if (lwjgl && config.get(LauncherProperties.JOML_NO_UNSAFE, true)) {
            result.add("-Djoml.nounsafe=true");
        }

        if (inMemory) {
            result.add("-D" + LauncherProperties.IN_MEMORY.getName() + "=true");
        }

        result.add("-Djava.library.path=" + natives);
        result.add("-cp");
        result.add(String.join("" + File.pathSeparatorChar, classpath)
                       + config.get(LauncherProperties.CLASS_PATH, ""));

        val adapter = ArgumentAdapterHelper.create(launcher, version, natives, account);
        result.addAll(adapter.build(os, Features.EMPTY, "jvm"));
        getActualMainClass(result);
        result.addAll(adapter.build(os, Features.EMPTY, "game"));
        result.addAll(Arrays.asList(config.get(LauncherProperties.GAME_ARGS,
                                               new String[0])));
        return result;
    }

    public String getActualMainClass(List<String> result) {
        var mainClass = version.getMainClass();
        if (runtime) {
            result.add("-D" + HmcProperties.MAIN.getName() + "="
                           + version.getMainClass());
            mainClass = RT_MAIN;
        }

        mainClass = launcher.getConfig().get(LauncherProperties.CUSTOM_MAIN_CLASS, mainClass);
        result.add(mainClass);
        return mainClass;
    }

}
