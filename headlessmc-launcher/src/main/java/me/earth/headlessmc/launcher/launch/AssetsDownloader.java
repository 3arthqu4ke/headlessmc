package me.earth.headlessmc.launcher.launch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

// TODO: support legacy assets!!!
// TODO: whats the map_to_resources thing?
// TODO: this is awful Spaghetti
@CustomLog
@RequiredArgsConstructor
class AssetsDownloader {
    private static final String URL = "https://resources.download.minecraft.net/";
    private final DummyAssets dummyAssets = new DummyAssets();
    private final FileManager files;
    private final HasConfig config;
    private final String url;
    private final String id;

    public void download() throws IOException {
        // TODO: this could probably be done in a better way
        val index = files.getDir("assets").toPath().resolve("indexes").resolve(id + ".json");
        if (!Files.exists(index)) {
            log.info("Downloading assets from " + url);
            IOUtil.download(url, index.toAbsolutePath().toString());
        }

        val objects = JsonUtil.getObject(JsonUtil.fromFile(index.toFile()), "objects");
        if (objects == null || !objects.isJsonObject()) {
            throw new IOException("Couldn't read contents of " + index.toAbsolutePath());
        }

        AtomicReference<IOException> failed = new AtomicReference<>();
        AtomicInteger count = new AtomicInteger();
        int total = objects.getAsJsonObject().size();
        boolean parallel = config.getConfig().get(LauncherProperties.ASSETS_PARALLEL, true);
        Stream<Map.Entry<String, JsonElement>> stream =
            parallel ? objects.getAsJsonObject().entrySet().parallelStream()
                     : objects.getAsJsonObject().entrySet().stream();
        long time = System.currentTimeMillis();
        //noinspection ResultOfMethodCallIgnored
        stream.anyMatch(entry -> {
            downloadAsset(entry, total, count, failed);
            return failed.get() != null; // end stream early if an asset failed completely
        });

        time = System.currentTimeMillis() - time;
        log.info("Downloading assets took " + time + "ms, parallel: " + parallel);
        if (failed.get() != null) {
            throw failed.get();
        }
    }

    private void downloadAsset(Map.Entry<String, JsonElement> entry, int total, AtomicInteger count, AtomicReference<IOException> failed) {
        int downloaded = count.incrementAndGet();
        String percentage = String.format("%d", (downloaded * 100 / total)) + "%";
        String progress =  percentage + " (" + downloaded + "/" + total + ")";
        log.debug(progress + " Checking " + entry.getKey());

        JsonObject jo = entry.getValue().getAsJsonObject();
        int tries = Math.max(1, config.getConfig().get(LauncherProperties.ASSETS_RETRIES, 3L).intValue());
        IOException exception = null;
        for (int i = 0; i < tries; i++) {
            try {
                long wait = config.getConfig().get(LauncherProperties.ASSETS_DELAY, 0L);
                if (config.getConfig().get(LauncherProperties.ASSETS_BACKOFF, true)) {
                    wait *= (i + 1); // increase wait time
                }

                if (wait > 0L) {
                    Thread.sleep(wait);
                }

                downloadAsset(progress,
                              entry.getKey(),
                              jo.get("hash").getAsString(),
                              jo.get("size") == null ? -1 : jo.get("size").getAsLong(),
                              jo.get("map_to_resources") != null && jo.get("map_to_resources").getAsBoolean());
                return; // downloaded successfully, return
            } catch (IOException e) {
                e.printStackTrace();
                log.warning(progress + " Failed to download asset " + entry.getKey() + ", retrying... (" + e.getMessage() + ")");
                exception = e;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                exception = new IOException("Thread interrupted");
            }
        }

        log.error("Failed to download asset " + entry.getKey() + " after " + tries + " tries!");
        if (exception != null) {
            failed.set(exception);
        }
    }

    private void downloadAsset(String progress, String name, String hash, long size, boolean mapToResources) throws IOException {
        val firstTwo = hash.substring(0, 2);
        val to = files.getDir("assets").toPath().resolve("objects").resolve(firstTwo).resolve(hash);
        Path file = getAssetsFile(name, to, hash, size);
        if (!Files.exists(file)) {
            byte[] bytes = null;
            if (config.getConfig().get(LauncherProperties.DUMMY_ASSETS, false)) {
                log.debug("Using dummy asset for " + name);
                bytes = dummyAssets.getResource(name);
            }

            if (bytes == null) {
                val from = URL + firstTwo + "/" + hash;
                log.info(progress + " Downloading: " + name + " from " + from + " to " + to);
                // TODO: user-agent? GZIPInputStream?
                bytes = IOUtil.downloadBytes(from);
                if (config.getConfig().get(LauncherProperties.ASSETS_CHECK_HASH, true)
                    && !checkIntegrity(size, hash, bytes)) {
                    throw new IOException("Failed integrity check on " + name + " from " + from);
                }
            }

            Files.createDirectories(to.getParent());
            try (OutputStream os = Files.newOutputStream(to)) {
                os.write(bytes);
            }
        }

        if ("pre-1.6".equals(id)) {
            // TODO: old versions have the map_to_resource thing, copy to resources
            val legacy = files.getDir("assets").toPath().resolve("virtual").resolve("legacy").resolve(name);
            log.info("Legacy version, copying to " + legacy);
            if (!Files.exists(legacy)) {
                Files.copy(file, legacy, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        if (mapToResources) {
            val resources = files.getDir("resources").toPath().resolve(name);
            log.debug("Mapping " + name + " to resources " + resources);
            if (!Files.exists(resources)) {
                Files.copy(file, resources, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private Path getAssetsFile(String name, Path file, String hash, long size) throws IOException {
        if (Files.exists(file) && config.getConfig().get(LauncherProperties.ASSETS_CHECK_FILE_HASH, false)) {
            try (FileInputStream fis = new FileInputStream(file.toFile())) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtil.copy(fis, baos);
                fis.close(); // < very important or we can't delete the file
                if (!checkIntegrity(size, hash, baos.toByteArray())) {
                    log.warning("File " + file + " (" + name + ") failed integrity check, deleting...");
                    Files.delete(file);
                }
            }
        }

        return file;
    }

    @SneakyThrows
    public boolean checkIntegrity(long size, String hash, byte[] bytes) {
        if (size >= 0L && size != bytes.length) {
            return false;
        }

        String byteHash = sha1(bytes);
        return hash.equalsIgnoreCase(byteHash);
    }

    public String sha1(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.digest(bytes);
        byte[] hashBytes = sha1.digest(bytes);
        StringBuilder hashBuilder = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) {
            hashBuilder.append(String.format("%02x", b));
        }

        return hashBuilder.toString();
    }

}
