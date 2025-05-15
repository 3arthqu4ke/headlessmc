package io.github.headlesshq.headlessmc.launcher.download;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class ChecksumServiceTest {
    private final ChecksumService checksumService = new ChecksumService();

    @Test
    void testCheckIntegrityWithCorrectHashAndSize() {
        byte[] bytes = "test data".getBytes();
        String hash = checksumService.hash(bytes);
        assertTrue(checksumService.checkIntegrity(bytes, (long) bytes.length, hash));
    }

    @Test
    void testCheckIntegrityWithIncorrectHash() {
        byte[] bytes = "test data".getBytes();
        String incorrectHash = "incorrectHash";
        assertFalse(checksumService.checkIntegrity(bytes, (long) bytes.length, incorrectHash));
    }

    @Test
    void testCheckIntegrityWithCorrectSizeButNoHash() {
        byte[] bytes = "test data".getBytes();
        assertTrue(checksumService.checkIntegrity(bytes, (long) bytes.length, null));
    }

    @Test
    void testCheckIntegrityWithoutSizeAndHash() {
        byte[] bytes = "test data".getBytes();
        assertTrue(checksumService.checkIntegrity(bytes, null, null));
    }

    @Test
    void testCheckIntegrityPathWithCorrectHashes() throws IOException {
        Path tempFile = Files.createTempFile("test", "data");
        Files.write(tempFile, "test data".getBytes());

        String hash = checksumService.hash(Files.readAllBytes(tempFile));
        assertTrue(checksumService.checkIntegrity(tempFile, Files.size(tempFile), hash));

        Files.delete(tempFile);
    }

    @Test
    void testCheckIntegrityPathWithIncorrectSize() throws IOException {
        Path tempFile = Files.createTempFile("test", "data");
        Files.write(tempFile, "test data".getBytes());

        assertFalse(checksumService.checkIntegrity(tempFile, 100L, null));

        Files.delete(tempFile);
    }

    @Test
    void testCheckIntegrityPathWithoutHashAndSize() throws IOException {
        Path tempFile = Files.createTempFile("test", "data");
        Files.write(tempFile, "test data".getBytes());

        assertTrue(checksumService.checkIntegrity(tempFile, null, null));

        Files.delete(tempFile);
    }

    @Test
    void testVerifyIntegrityWithCorrectData() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        String correctHash = checksumService.hash(bytes);

        assertTrue(checksumService.checkIntegrity(inputStream, (long) bytes.length, correctHash, new byte[8096], i -> {}));
    }

    @Test
    void testVerifyIntegrityWithIncorrectData() throws IOException {
        byte[] bytes = "test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("wrong data".getBytes());
        String correctHash = checksumService.hash(bytes);

        assertFalse(checksumService.checkIntegrity(inputStream, (long) bytes.length, correctHash, new byte[8096], i -> {}));
    }

    @Test
    void testHashBytes() throws NoSuchAlgorithmException {
        byte[] bytes = "test data".getBytes();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(bytes);

        assertArrayEquals(hash, checksumService.hashBytes(bytes));
    }

    @Test
    void testToHashString() {
        byte[] bytes = {(byte) 0x12, (byte) 0x34, (byte) 0xab, (byte) 0xcd};
        String expectedHashString = "1234abcd";
        assertEquals(expectedHashString, checksumService.toHashString(bytes));
    }

    @Test
    void testGetHashFunction() {
        assertNotNull(checksumService.getHashFunction());
    }

}
