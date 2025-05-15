package io.github.headlesshq.headlessmc.web.cheerpj.plugin;

import io.github.headlesshq.headlessmc.launcher.auth.AuthException;
import io.github.headlesshq.headlessmc.launcher.download.DownloadService;
import io.github.headlesshq.headlessmc.launcher.files.LauncherConfig;
import io.github.headlesshq.headlessmc.launcher.instrumentation.InstrumentationClassloader;
import io.github.headlesshq.headlessmc.launcher.instrumentation.debug.DebugTransformer;
import io.github.headlesshq.headlessmc.launcher.launch.InMemoryLauncher;
import io.github.headlesshq.headlessmc.launcher.launch.LaunchException;
import io.github.headlesshq.headlessmc.launcher.launch.ProcessFactory;
import io.github.headlesshq.headlessmc.launcher.launch.SimpleInMemoryLauncher;
import io.github.headlesshq.headlessmc.os.OS;

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
