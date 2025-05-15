package io.github.headlesshq.headlessmc.launcher.auth;

import lombok.Getter;
import lombok.Setter;
import io.github.headlesshq.headlessmc.api.config.HasConfig;
import io.github.headlesshq.headlessmc.launcher.LauncherProperties;

@Setter
@Getter
public class OfflineChecker {
    private boolean offline;

    public OfflineChecker(HasConfig configService) {
        this.offline = configService.getConfig().get(LauncherProperties.OFFLINE, false);
    }

}
