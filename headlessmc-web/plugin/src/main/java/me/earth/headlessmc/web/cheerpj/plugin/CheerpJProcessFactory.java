package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationClassloader;
import me.earth.headlessmc.launcher.instrumentation.debug.DebugTransformer;
import me.earth.headlessmc.launcher.launch.InMemoryLauncher;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.launch.SimpleInMemoryLauncher;
import me.earth.headlessmc.launcher.os.OS;

import java.io.IOException;
import java.util.ArrayList;

public class CheerpJProcessFactory extends ProcessFactory {
    public CheerpJProcessFactory(FileManager files, HasConfig config, OS os) {
        super(files, config, os);
    }

    @Override
    protected void inMemoryLaunch(InMemoryLauncher inMemoryLauncher) throws LaunchException, AuthException, IOException {
        inMemoryLauncher.setClassLoaderFactory(urls -> {
            InstrumentationClassloader icl = new InstrumentationClassloader(urls, SimpleInMemoryLauncher.class.getClassLoader(), new ArrayList<>());
            icl.getTransformer().getTransformers().add(new DebugTransformer());
            return icl;
        });

        super.inMemoryLaunch(inMemoryLauncher);
    }

}
