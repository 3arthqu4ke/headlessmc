package me.earth.headlessmc.launcher;

import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.launcher.auth.OfflineChecker;

public class TestOfflineChecker extends OfflineChecker {
    public TestOfflineChecker() {
        super(ConfigImpl::empty);
    }

    @Override
    public boolean isOffline() {
        return true;
    }

}
