package me.earth.headlessmc.launcher.server.downloader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.server.ServerTypeDownloader;
import me.earth.headlessmc.launcher.util.JsonUtil;
import me.earth.headlessmc.launcher.util.URLs;
import net.lenni0451.commons.httpclient.HttpResponse;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;

@CustomLog
@RequiredArgsConstructor
public class PurpurDownloader implements ServerTypeDownloader {
    private static final URL URL = URLs.url("https://api.purpurmc.org/v2/purpur/");

    @Override
    public DownloadHandler download(Launcher launcher, String version, @Nullable String typeVersionIn, String... args) throws IOException {
        String build = getBuild(launcher.getDownloadService(), version, typeVersionIn);
        URL url = new URL(String.format("%s%s/%s/download", URL, version, build));
        log.debug("Downloading purpur from " + url);
        return new UrlJarDownloadHandler(launcher.getDownloadService(), url.toString(), build);
    }

    private String getBuild(DownloadService downloadService, String version, @Nullable String typeVersionIn) throws IOException {
        String build = typeVersionIn;
        if (build == null) {
            HttpResponse response = downloadService.download(new URL(URL + version + "/"));
            String string = response.getContentAsString();
            JsonElement element = JsonParser.parseString(string);
            JsonArray array = JsonUtil.getArray(element, "builds", "all");
            if (array != null) {
                build = array.get(array.size() - 1).getAsString();
            }

            if (build == null) {
                throw new IOException("No builds found in " + string);
            }
        }

        return build;
    }

}
