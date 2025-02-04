package me.earth.headlessmc.launcher.command.download;

import java.util.ArrayList;
import java.util.List;

public class DummyVersionInfoCache extends VersionInfoCache {
    @Override
    public List<VersionInfo> cache(boolean force) {
        return new ArrayList<>();
    }

}
