package me.earth.headlessmc.launcher.download;

import me.earth.headlessmc.launcher.util.IOConsumer;
import org.jetbrains.annotations.Nullable;

import java.net.URL;

public class MockDownloadService extends DownloadService {
    @Override
    public void download(URL from, @Nullable Long size, @Nullable String hash, IOConsumer<byte[]> action) {
        // NOP
    }

}
