package me.earth.headlessmc.launcher.instrumentation;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

@Data
public class Target {
    private final boolean gameJar;
    private final String path;

    public JarFile toJar() throws IOException {
        return new JarFile(new File(path));
    }

}
