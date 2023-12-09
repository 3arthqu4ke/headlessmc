package me.earth.headlessmc.launcher;

import me.earth.headlessmc.launcher.auth.OfflineChecker;

public class TestOfflineChecker extends OfflineChecker {
    public TestOfflineChecker() {
        super(null);
    }

    @Override
    public boolean isOffline() {
        return false;
    }

}
