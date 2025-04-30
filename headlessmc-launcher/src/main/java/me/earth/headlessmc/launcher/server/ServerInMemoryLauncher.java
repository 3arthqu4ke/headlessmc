package me.earth.headlessmc.launcher.server;

import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.launch.AbstractInMemoryGameProcessLauncher;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.launch.LaunchOptions;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerInMemoryLauncher extends AbstractInMemoryGameProcessLauncher {
    private final Server server;
    private String mainClassName;

    public ServerInMemoryLauncher(LaunchOptions options, @Nullable Java java, Server server) {
        super(options, java);
        this.server = server;
    }

    @Override
    protected String getMainClassName() throws IOException {
        if (mainClassName == null) {
            mainClassName = getMainClassFromJar(server.getJar().toFile());
            if (mainClassName == null) {
                throw new IOException("Failed to read Main class attribute of " + server.getJar());
            }
        }

        return mainClassName;
    }

    @Override
    protected List<String> getClasspath() {
        return new ArrayList<>(Collections.singletonList(server.getJar().toAbsolutePath().toString()));
    }

    @Override
    protected List<String> buildCommand() throws LaunchException, AuthException, IOException {
        // TODO
        return new ArrayList<>(Arrays.asList(
            getMainClassName()
        ));
    }

    @Override
    protected boolean needsJava9(Java java) throws IOException {
        return java.getVersion() > 8
                && !options.isForceSimple()
                && ("cpw.mods.bootstraplauncher.BootstrapLauncher".equals(getMainClassName())
                    || options.isForceBoot());
    }

}
