package me.earth.headlessmc.web.cheerpj;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static final String MAIN_CLASS = "me.earth.headlessmc.web.cheerpj.plugin.CheerpJMain";
    private static String[] args = new String[0];

    public static void main(String[] args) throws Exception {
        Main.args = args;
        init();
    }

    public static void init() throws Exception {
        Path root = me.earth.headlessmc.wrapper.Main.createRootDirectory();
        Path jarPath = root.resolve("plugins").resolve("headlessmc-web-plugin.jar");
        Files.createDirectories(jarPath.getParent());
        me.earth.headlessmc.wrapper.Main.extractResource("headlessmc/headlessmc-web-plugin.jar", jarPath);
        // do not close classloader the awt thread will continue but we want to give control back to JavaScript
        System.setProperty(me.earth.headlessmc.wrapper.Main.CLOSE_CLASSLOADER_PROPERTY, "false");
        System.setProperty(me.earth.headlessmc.wrapper.Main.WRAPPED_MAIN_PROPERTY, MAIN_CLASS);
        me.earth.headlessmc.wrapper.Main.main(args);
    }

}
