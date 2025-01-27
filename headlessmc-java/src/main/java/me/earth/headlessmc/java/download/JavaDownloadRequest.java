package me.earth.headlessmc.java.download;

import lombok.Data;
import me.earth.headlessmc.api.command.line.ProgressBarProvider;
import me.earth.headlessmc.os.OS;
import org.jetbrains.annotations.Nullable;

@Data
public class JavaDownloadRequest {
    private final DownloadClient client;
    private final ProgressBarProvider progressBarProvider;
    private final int version;
    private final @Nullable String distribution;
    private final OS os;
    private final boolean jdk;

    public String getProgressBarTitle() {
        return "Downloading Java " + version;
    }

}
