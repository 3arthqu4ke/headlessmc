package me.earth.headlessmc.wrapper.plugin;

public interface Transformer {
    byte[] transform(String className, byte[] bytes);

}
