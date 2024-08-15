package me.earth.headlessmc.web.cheerpj;

public class Main {
    public static final String MAIN_CLASS = "me.earth.headlessmc.web.cheerpj.plugin.CheerpJMain";
    private static String[] args = new String[0];

    public static void main(String[] args) throws Exception {
        Main.args = args;
        init();
    }

    public static void init() throws Exception {
        System.setProperty(me.earth.headlessmc.wrapper.Main.WRAPPED_MAIN_PROPERTY, MAIN_CLASS);
        me.earth.headlessmc.wrapper.Main.main(args);
    }

}
