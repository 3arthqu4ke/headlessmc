package me.earth.headlessmc.launcher;

import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.config.HmcProperties;
import me.earth.headlessmc.lwjgl.LwjglProperties;

import static me.earth.headlessmc.config.PropertyTypes.*;

public interface LauncherProperties extends HmcProperties {
    Property<String> MC_DIR = string("hmc.mcdir");
    Property<String> GAME_DIR = string("hmc.gamedir");

    Property<String> OS_NAME = string("hmc.osname");
    Property<String> OS_TYPE = string("hmc.ostype");
    Property<String> OS_VERSION = string("hmc.osversion");
    Property<Boolean> OS_ARCH = bool("hmc.osarch");

    Property<String[]> JAVA = array("hmc.java.versions", ";");

    Property<String[]> JVM_ARGS = array("hmc.jvmargs", " ");
    Property<String[]> GAME_ARGS = array("hmc.gameargs", " ");

    Property<String> XUID = string("hmc.xuid");
    Property<String> CLIENT_ID = string("hmc.clientId");

    Property<String> LAUNCHER_NAME = string("hmc.launchername");
    Property<String> LAUNCHER_VERSION = string("hmc.launcherversion");

    Property<String> EMAIL = string("hmc.email");
    Property<String> PASSWORD = string("hmc.password");
    Property<String> AUTH_TOKEN = string("hmc.authtoken");
    Property<String> AUTH_TYPE = string("hmc.authtype");

    Property<Long> DEFAULT_JAVA = number("hmc.version.defaultJava");

}
