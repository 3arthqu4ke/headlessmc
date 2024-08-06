package me.earth.headlessmc.test;

import lombok.extern.java.Log;
import me.earth.headlessmc.wrapper.HeadlessMcWrapper;
import me.earth.headlessmc.wrapper.Main;
import me.earth.headlessmc.wrapper.plugin.TransformerPlugin;
import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;
import me.earth.headlessmc.wrapper.plugin.TransformingPluginFinder;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Log
public class IntegrationTest {
    private static final Path ROOT = Paths.get("HeadlessMC");

    @BeforeAll
    public static void beforeAll() throws IOException {
        if (Files.exists(ROOT)) {
            try (Stream<Path> pathStream = Files.walk(Paths.get("HeadlessMC"))) {
                //noinspection ResultOfMethodCallIgnored
                pathStream.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }
    }

    @Test
    public void runIntegrationTest() throws Exception {
        // set the property in gradle.properties
        assertNotNull(System.getProperty("hmc.integration.test.enabled"));
        Assumptions.assumeTrue(Boolean.parseBoolean(System.getProperty("hmc.integration.test.enabled")));

        assertFalse(Files.exists(Paths.get("HeadlessMC")), "HeadlessMC directory should be deleted!");

        System.setProperty("hmc.mcdir", ROOT.resolve("mcdir").toAbsolutePath().toString());
        System.setProperty("hmc.gamedir", ROOT.resolve("gamedir").toAbsolutePath().toString());

        Path testPlugin = Paths.get("build").resolve("libs").resolve("testPlugin.jar");
        assertTrue(Files.exists(testPlugin), "NoExit plugin jar should exist!");
        Path plugins = ROOT.resolve("plugins");
        Files.createDirectories(plugins);
        assertTrue(Files.exists(Files.copy(testPlugin, plugins.resolve("testPlugin.jar"))), "NoExit plugin jar should exit in plugins!");

        HeadlessMcWrapper.setPluginFinderFactory(path -> new TransformingPluginFinder(plugins) {
            @Override
            protected TransformingClassloader newTransformingClassloader(URL[] urls, ClassLoader parent, URLClassLoader transformerClassloader, List<TransformerPlugin> plugins) {
                return new TransformingClassloaderWithJacoco(urls, parent, transformerClassloader, plugins);
            }
        });

        log.info("Running integration test!");
        Main.main(new String[0]);
    }

}
