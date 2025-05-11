package me.earth.headlessmc.launcher.launch;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.auth.LaunchAccount;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.os.OS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class JavaLaunchCommandBuilderTest implements UsesResources {
    private final Version version = getVersion("version_parent.json", 0);

    @Test
    @SneakyThrows
    public void testCommand() {
        val command = setupCommand();
        Assertions.assertThrows(LaunchException.class, command::build);
        LauncherMock.INSTANCE.getJavaService().add(new Java("dummy", 8));
        Assertions.assertThrows(LaunchException.class, command::build);
        LauncherMock.INSTANCE.getJavaService().add(new Java("java-10000", 10000));
        System.setProperty(LauncherProperties.SET_LIBRARY_DIR.getName(), "false");
        val expected = Arrays.asList(
            "java-10000",
            "-Dadditional.property=true",
            "-Dhmc.deencapsulate=true",
            "-Djava.library.path=natives_path", "-cp",
            "test" + File.pathSeparator + "test",
            "-DSomeSystemProperty=${some_arg}",
            "-DignoreList=headlessmc-runtime.jar",
            "-Dhmc.main_method=path.to.MainClass",
            "me.earth.headlessmc.runtime.Main",
            "--username", "d", "--versionType", "release", "-game-arg");

        Assertions.assertEquals(expected, command.build());
    }

    private JavaLaunchCommandBuilder setupCommand() {
        return JavaLaunchCommandBuilder.builder()
                      .classpath(Arrays.asList("test", "test"))
                      .account(new LaunchAccount("d", "d", "d", "d", "d"))
                      .os(new OS("win", OS.Type.WINDOWS, "11", true))
                      .jvmArgs(Collections.singletonList("-Dadditional.property=true"))
                      .gameArgs(Collections.singletonList("-game-arg"))
                      .natives("natives_path")
                      .runtime(true)
                      .version(version)
                      .launcher(LauncherMock.INSTANCE)
                      .build();
    }

}
