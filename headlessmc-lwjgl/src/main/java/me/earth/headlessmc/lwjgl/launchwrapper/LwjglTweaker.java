package me.earth.headlessmc.lwjgl.launchwrapper;

import lombok.SneakyThrows;
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
        lcl.addClassLoaderExclusion("me.earth.headlessmc.lwjgl.launchwrapper.");
        lcl.addClassLoaderExclusion("me.earth.headlessmc.lwjgl.transformer.");
        Field exc = lcl.getClass().getDeclaredField("classLoaderExceptions");
        exc.setAccessible(true);
        ((Set<?>) exc.get(lcl)).remove("org.lwjgl.");
        // TODO: check that I'm not stupid and that the LaunchWrapperTransformer
        //  really doesn't get loaded by the LaunchClassloader
        lcl.registerTransformer(LaunchWrapperLwjglTransformer.class.getName());
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

}
