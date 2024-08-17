package me.earth.headlessmc.launcher.command.forge;

import me.earth.headlessmc.launcher.launch.SimpleInMemoryLauncher;

public class ForgeInMemoryLauncher extends SimpleInMemoryLauncher {
    public ForgeInMemoryLauncher() {
        this.setClassLoaderFactory(ForgeInstallerProtectionDomainClassloader::new);
    }

}
