package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.LauncherConfig;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationClassloader;
import me.earth.headlessmc.launcher.instrumentation.debug.DebugTransformer;
import me.earth.headlessmc.launcher.launch.InMemoryLauncher;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.launch.SimpleInMemoryLauncher;
import me.earth.headlessmc.os.OS;

import java.io.IOException;
import java.util.ArrayList;

public class CheerpJProcessFactory extends ProcessFactory {
    public CheerpJProcessFactory(DownloadService downloadService, LauncherConfig launcherConfig, OS os) {
        super(downloadService, launcherConfig, os);
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
