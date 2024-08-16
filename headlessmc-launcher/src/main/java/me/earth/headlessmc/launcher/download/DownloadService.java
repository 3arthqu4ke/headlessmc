package me.earth.headlessmc.launcher.download;

import org.jetbrains.annotations.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class DownloadService {
    public void download(String from, String to, @Nullable String sha1) throws IOException {
        URL url = new URL(from);
        try (InputStream is = url.openStream(); FileOutputStream os = new FileOutputStream(to)) {
            MessageDigest sha1Digest = null;
            if (sha1 != null) {
                sha1Digest = MessageDigest.getInstance("SHA-1");
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                if (sha1Digest != null) {
                    sha1Digest.update(buffer, 0, bytesRead);
                }
            }

            if (sha1Digest != null) {
                String actualSha1Hex = toHashString(sha1Digest.digest());
                if (!actualSha1Hex.equalsIgnoreCase(sha1)) {
                    throw new IOException("Checksum does not match. Expected: " + sha1 + " but was: " + actualSha1Hex);
                }
            }
        } catch (Exception e) {
            throw new IOException("Failed to download or validate file", e);
        }
    }

    public String toHashString(byte[] bytes) {
        StringBuilder hashBuilder = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hashBuilder.append(String.format("%02x", b));
        }

        return hashBuilder.toString();
    }

}
