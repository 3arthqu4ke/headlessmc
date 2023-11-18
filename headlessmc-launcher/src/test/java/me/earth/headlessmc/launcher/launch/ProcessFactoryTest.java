package me.earth.headlessmc.launcher.launch;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.os.OS;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessFactoryTest {
    @Test
    @SneakyThrows
    public void testCheckZipIntact() {
        val launcher = LauncherMock.INSTANCE;
        val processFactory = new ProcessFactory(
            launcher.getFileManager(),
            new OS("win", OS.Type.WINDOWS, "10", true));

        var invalid = new File("DOES_NOT_EXIST!!!!");
        assertTrue(processFactory.checkZipIntact(invalid));

        invalid = new File("DOES_NOT_EXIST!!!!.jar");
        assertFalse(processFactory.checkZipIntact(invalid));

        invalid = new File("DOES_NOT_EXIST!!!!.zip");
        assertFalse(processFactory.checkZipIntact(invalid));

        invalid = LauncherMock.INSTANCE.getFileManager().create("invalid.zip");
        assertFalse(processFactory.checkZipIntact(invalid));

        invalid = LauncherMock.INSTANCE.getFileManager().create("invalid.jar");
        assertFalse(processFactory.checkZipIntact(invalid));

        val valid = launcher.getFileManager().get(false, false, "valid.zip");
        try (val zos = new ZipOutputStream(new FileOutputStream(valid));
             val fis = new FileInputStream(invalid)) {
            val zipEntry = new ZipEntry(invalid.getAbsolutePath());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
        }

        assertTrue(processFactory.checkZipIntact(valid));
    }

}
