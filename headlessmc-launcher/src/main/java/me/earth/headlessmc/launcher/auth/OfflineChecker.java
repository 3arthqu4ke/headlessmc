package me.earth.headlessmc.launcher.auth;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;

@Setter
@Getter
public class OfflineChecker {
    private boolean offline;

    public OfflineChecker(HasConfig configService) {
        this.offline = configService.getConfig().get(LauncherProperties.OFFLINE, false);
    }

}
