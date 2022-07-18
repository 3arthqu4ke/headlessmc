package me.earth.headlessmc.lwjgl.launchwrapper;

import lombok.SneakyThrows;
import me.earth.headlessmc.lwjgl.LwjglProperties;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class LwjglTweaker implements ITweaker {
    @Override
    public void acceptOptions(
        List<String> args, File gameDir, File assetsDir, String profile) {
        // TODO: maybe use impacts SimpleTweaker?
    }

    @Override
    @SneakyThrows
    public void injectIntoClassLoader(LaunchClassLoader lcl) {
        Field exc = lcl.getClass().getDeclaredField("classLoaderExceptions");
        exc.setAccessible(true);
        ((Set<?>) exc.get(lcl)).remove("org.lwjgl.");
        lcl.registerTransformer("me.earth.headlessmc.lwjgl.launchwrapper" +
                                    ".LaunchWrapperLwjglTransformer");
    }

    @Override
    public String getLaunchTarget() {
        return System.getProperty(LwjglProperties.TWEAKER_MAIN_CLASS,
                                  "net.minecraft.client.main.Main");
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

}
