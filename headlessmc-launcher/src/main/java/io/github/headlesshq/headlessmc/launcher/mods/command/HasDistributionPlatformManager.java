package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.CommandUtil;
import io.github.headlesshq.headlessmc.launcher.mods.ModDistributionPlatform;
import io.github.headlesshq.headlessmc.launcher.mods.ModDistributionPlatformManager;

public interface HasDistributionPlatformManager {
    ModDistributionPlatformManager getDistributionPlatformManager();

    default ModDistributionPlatform getPlatform(String... args) throws CommandException {
        String platform = CommandUtil.getOption("--mod-distributor", args);
        if (platform != null) {
            for (ModDistributionPlatform distributionPlatform : getDistributionPlatformManager()) {
                if (distributionPlatform.getName().equalsIgnoreCase(platform)) {
                    return distributionPlatform;
                }
            }

            throw new CommandException("Unknown mod-distributor: " + platform);
        }

        return getDistributionPlatformManager().iterator().next();
    }

}
