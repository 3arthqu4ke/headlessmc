package io.github.headlesshq.headlessmc.test;

import io.github.headlesshq.headlessmc.wrapper.plugin.Transformer;
import io.github.headlesshq.headlessmc.wrapper.plugin.TransformerPlugin;

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
