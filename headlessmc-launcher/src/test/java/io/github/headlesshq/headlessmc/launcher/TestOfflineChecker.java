package io.github.headlesshq.headlessmc.launcher;

import io.github.headlesshq.headlessmc.api.config.ConfigImpl;
import io.github.headlesshq.headlessmc.launcher.auth.OfflineChecker;

public class TestOfflineChecker extends OfflineChecker {
    public TestOfflineChecker() {
        super(ConfigImpl::empty);
    }

    @Override
    public boolean isOffline() {
        return true;
    }

}
