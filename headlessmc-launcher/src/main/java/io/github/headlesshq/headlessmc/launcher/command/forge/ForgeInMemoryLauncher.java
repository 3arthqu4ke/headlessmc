package io.github.headlesshq.headlessmc.launcher.command.forge;

import io.github.headlesshq.headlessmc.launcher.launch.SimpleInMemoryLauncher;

public class ForgeInMemoryLauncher extends SimpleInMemoryLauncher {
    public ForgeInMemoryLauncher() {
        this.setClassLoaderFactory(ForgeInstallerProtectionDomainClassloader::new);
    }

}
