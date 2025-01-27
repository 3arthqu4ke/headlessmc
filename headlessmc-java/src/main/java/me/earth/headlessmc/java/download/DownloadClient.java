package me.earth.headlessmc.java.download;

import me.earth.headlessmc.api.command.line.ProgressBarProvider;

import java.io.IOException;
import java.nio.file.Path;

public interface DownloadClient {
    String httpGetText(String url) throws IOException;

    void downloadBigFile(String url, Path destination, String progressBarTitle, ProgressBarProvider progressBarProvider) throws IOException;

}
