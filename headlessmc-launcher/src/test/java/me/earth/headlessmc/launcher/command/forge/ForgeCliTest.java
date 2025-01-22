package me.earth.headlessmc.launcher.command.forge;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ForgeCliTest {
    // just a test to tell GraalVM that we need this resource!
    @Test
    public void testForgeCliResourceForGraalVM() {
        try (InputStream is = ForgeCliTest.class.getClassLoader().getResourceAsStream("headlessmc/forge-cli.jar")) {
            assertNotNull(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
