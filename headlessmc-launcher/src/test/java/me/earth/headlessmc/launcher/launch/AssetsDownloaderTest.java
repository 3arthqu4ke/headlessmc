package me.earth.headlessmc.launcher.launch;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AssetsDownloaderTest {
    @Test
    @SneakyThrows
    public void testSha1() {
        byte[] bytes = new byte[] { 0, 1, 2, 3 };
        byte[] bytes2 = new byte[] { 3, 2, 1, 0 };
        String hash = new AssetsDownloader(null, null, "", "").sha1(bytes);
        String hash2 = new AssetsDownloader(null, null, "", "").sha1(bytes2);
        assertEquals("a02a05b025b928c039cf1ae7e8ee04e7c190c0db", hash);
        assertEquals("210ba3f2125565cd8cb66b8b0c1431f6a7e0f7bb", hash2);
    }

    @Test
    @SneakyThrows
    public void testCheckIntegrity() {
        byte[] bytes = new byte[64];
        new Random(0).nextBytes(bytes);
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = sha1.digest(bytes);
        StringBuilder hashBuilder = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) {
            hashBuilder.append(String.format("%02x", b));
        }

        String hash = hashBuilder.toString();
        AssetsDownloader downloader = new AssetsDownloader(null, null, "", "");
        assertTrue(downloader.checkIntegrity(bytes.length, hash, bytes));
        assertFalse(downloader.checkIntegrity(bytes.length - 1, hash, bytes));
        assertFalse(downloader.checkIntegrity(bytes.length, "", bytes));
    }

}
