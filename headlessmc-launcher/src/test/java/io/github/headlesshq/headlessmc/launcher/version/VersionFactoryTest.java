package io.github.headlesshq.headlessmc.launcher.version;

import lombok.val;
import io.github.headlesshq.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class VersionFactoryTest implements UsesResources {
    @Test
    public void testVersionFactory() {
        val factory = new DefaultVersionFactory();
        Assertions.assertThrows(
            VersionParseException.class,
            () -> factory.parse(getJsonObject("version_invalid.json"),
                                new File(""), () -> 0));

        // TODO: more tests
    }

}
