package me.earth.headlessmc.launcher.auth;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.ConfigService;

@Setter
@Getter
public class OfflineChecker {
    private boolean offline;

    public OfflineChecker(ConfigService configService) {
        this.offline = configService.getConfig().get(LauncherProperties.OFFLINE, false);
    }

}
