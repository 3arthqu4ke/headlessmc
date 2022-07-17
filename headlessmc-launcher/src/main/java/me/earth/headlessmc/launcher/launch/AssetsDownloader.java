package me.earth.headlessmc.launcher.launch;

import com.google.gson.JsonElement;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

// TODO: support legacy assets!!!
// TODO: whats the map_to_resources thing?
// TODO: can we download in parallel?
@CustomLog
@RequiredArgsConstructor
class AssetsDownloader {
    private static final String URL =
        "https://resources.download.minecraft.net/";
    private final FileManager files;
    private final String url;
    private final String id;

    public void download() throws IOException {
        // TODO: this could probably be done in a better way
        val index = new File(files.getDir("assets") + File.separator
                                 + "indexes" + File.separator + id + ".json");
        if (!index.exists()) {
            log.info("Downloading assets from " + url);
            IOUtil.download(url, index.getAbsolutePath());
        }

        val objects = JsonUtil.getObject(JsonUtil.fromFile(index), "objects");
        if (objects == null || !objects.isJsonObject()) {
            throw new IOException("Couldn't read contents of "
                                      + index.getAbsolutePath());
        }

        for (Map.Entry<String, JsonElement> entry : objects.getAsJsonObject()
                                                           .entrySet()) {
            log.debug("Checking " + entry.getKey() + "...");
            // TODO: we trust a lot in this always having the right format!
            downloadAsset(entry.getKey(), entry.getValue()
                                               .getAsJsonObject()
                                               .get("hash").getAsString());
        }
    }

    private void downloadAsset(String name, String hash) throws IOException {
        val firstTwo = hash.substring(0, 2);
        val to = files.getDir("assets") + File.separator + "objects"
            + File.separator + firstTwo + File.separator + hash;

        val file = new File(to);
        if (!file.exists()) {
            val from = URL + firstTwo + "/" + hash;
            log.info("Downloading: " + name + " from " + from + " to " + to);
            IOUtil.download(from, to);
        }

        if ("pre-1.6".equals(id)) {
            val legacy = new File(files.getDir("assets") + File.separator
                                      + "virtual" + File.separator + "legacy"
                                      + File.separator + name);
            log.info("Legacy version, copying to " + legacy);
            if (!legacy.exists()) {
                Files.copy(file.toPath(), legacy.toPath(),
                           StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

}
