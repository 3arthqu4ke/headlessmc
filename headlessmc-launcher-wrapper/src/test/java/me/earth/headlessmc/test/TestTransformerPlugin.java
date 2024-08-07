package me.earth.headlessmc.test;

import me.earth.headlessmc.wrapper.plugin.Transformer;
import me.earth.headlessmc.wrapper.plugin.TransformerPlugin;

public class TestTransformerPlugin implements TransformerPlugin {
    @Override
    public Transformer getTransformer() {
        return (className, bytes) -> {
            if (className.endsWith("DummyClassThatCantBeLoaded")) {
                throw new IllegalStateException("DummyClassThatCantBeLoaded cant be loaded!");
            }

            return bytes;
        };
    }

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public int getPriority() {
        return 0;
    }

}
