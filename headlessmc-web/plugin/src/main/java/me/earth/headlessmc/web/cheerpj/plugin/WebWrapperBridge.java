package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.web.cheerpj.Resizer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class WebWrapperBridge {
    public static void setUpdateListener(BiConsumer<Integer, Integer> updateListener) {
        Resizer resizer = Resizer.getInstance();
        resizer.setUpdateListener(updateListener);
    }

    public static void getWidthAndHeight(BiConsumer<Integer, Integer> updateListener) {
        Resizer resizer = Resizer.getInstance();
        updateListener.accept(resizer.getWidth(), resizer.getHeight());
    }

    public static @Nullable String getVersion() {
        return Resizer.getInstance().getVersion();
    }

    public static String getExpectedVersion() {
        return Resizer.EXPECTED_INDEX_HTML_VERSION;
    }

}
