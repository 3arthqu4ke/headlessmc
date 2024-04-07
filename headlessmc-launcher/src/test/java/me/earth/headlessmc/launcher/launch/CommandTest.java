package me.earth.headlessmc.launcher.launch;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.version.Version;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class CommandTest implements UsesResources {
    private final Version version = getVersion("version_parent.json", 0);

    @Test
    @SneakyThrows
    public void testCommand() {
        val command = setupCommand();
        Assertions.assertThrows(LaunchException.class, command::build);
        LauncherMock.INSTANCE.getJavaService().add(new Java("dummy", 8));
        Assertions.assertThrows(LaunchException.class, command::build);
        LauncherMock.INSTANCE.getJavaService().add(new Java("java-17", 17));
        System.setProperty(LauncherProperties.SET_LIBRARY_DIR.getName(), "false");
        val expected = Arrays.asList(
            "java-17", "-Dhmc.deencapsulate=true",
            "-Djava.library.path=natives_path", "-cp",
            "test" + File.pathSeparator + "test",
            "-DSomeSystemProperty=${some_arg}",
            "-Dhmc.main_method=path.to.MainClass",
            "me.earth.headlessmc.runtime.Main",
            "--username", "d", "--versionType", "release");

        Assertions.assertEquals(expected, command.build());
    }

    private Command setupCommand() {
        return Command.builder()
                      .classpath(Arrays.asList("test", "test"))
                      .os(new OS("win", OS.Type.WINDOWS, "11", true))
                      .jvmArgs(Collections.emptyList())
                      .natives("natives_path")
                      .runtime(true)
                      .version(version)
                      .launcher(LauncherMock.INSTANCE)
                      .build();
    }

}
