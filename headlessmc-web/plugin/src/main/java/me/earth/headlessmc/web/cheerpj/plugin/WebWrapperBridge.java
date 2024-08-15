package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.web.cheerpj.Resizer;

import java.util.function.BiConsumer;

public class WebWrapperBridge {
    public static void setUpdateListener(BiConsumer<Integer, Integer> updateListener) {
        Resizer resizer = Resizer.getInstance();
        System.out.println(resizer);
        resizer.setUpdateListener(updateListener);
    }

    public static void getWidthAndHeight(BiConsumer<Integer, Integer> updateListener) {
        Resizer resizer = Resizer.getInstance();
        updateListener.accept(resizer.getWidth(), resizer.getHeight());
    }

}
