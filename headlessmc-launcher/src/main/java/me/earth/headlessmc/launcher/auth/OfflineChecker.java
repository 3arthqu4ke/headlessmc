package me.earth.headlessmc.launcher.auth;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.ConfigService;

@RequiredArgsConstructor
public class OfflineChecker {
    private final ConfigService configService;
    
    public boolean isOffline() {
        return configService.getConfig().get(LauncherProperties.OFFLINE, false);
    }
    
}
