package me.earth.headlessmc.launcher.launch;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.util.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LaunchTest {
    private final Launcher launcher = LauncherMock.INSTANCE;

    @Test
    @SneakyThrows
    public void testLaunch() {
        System.setProperty(LauncherProperties.SET_LIBRARY_DIR.getName(), "false");
        setupLauncher();
        var builder = ((MockProcessFactory) launcher.getProcessFactory())
            .getBuilder();
        assertNull(builder);

        launcher.getCommandContext().execute("launch 1.19-launch-test -stay");

        builder = ((MockProcessFactory) launcher.getProcessFactory())
            .getBuilder();
        assertNotNull(builder);

        var command = builder.command();
        assertEquals(10, command.size());
        assertEquals("java17", command.get(0));
        assertTrue(command.get(1).startsWith("-Djava.library.path="));
        assertEquals("-cp", command.get(2));
        assertEquals(2, command.get(3).split(File.pathSeparator).length);
        assertEquals("-DSomeSystemProperty=${some_arg}", command.get(4));
        assertEquals("path.to.MainClass", command.get(5));
        assertEquals("--username", command.get(6));
        assertEquals("d", command.get(7));
        assertEquals("--versionType", command.get(8));
        assertEquals("release", command.get(9));
    }

    @Test
    public void testWithVmArgs() {
        System.setProperty(LauncherProperties.SET_LIBRARY_DIR.getName(), "false");
        setupLauncher();
        launcher.getCommandContext().execute(
            // TODO: all this escaping is kinda ehh
            //   maybe allow multiple --jvm flags?
            "launch 1.19-launch-test -stay --jvm \"-testVmArg -testVmArg\\\\" +
                " withEscapedSpace \\\"-testVmArg2 with space\\\"" +
                " -DVMSystemProp=\\\\\\\"systemProp\\\\ with\\\\" +
                " space\\\\\\\"\"");

        val cmd = ((MockProcessFactory) launcher.getProcessFactory())
            .getBuilder()
            .command();

        assertEquals(14, cmd.size());
        assertEquals("java17", cmd.get(0));
        assertEquals("-testVmArg", cmd.get(1));
        assertEquals("-testVmArg withEscapedSpace", cmd.get(2));
        assertEquals("-testVmArg2 with space", cmd.get(3));
        assertEquals("-DVMSystemProp=\"systemProp with space\"", cmd.get(4));
    }

    private void setupLauncher() {
        resetLauncher();
        unpackVersion();
        val vs = launcher.getVersionService();
        vs.refresh();
        assertTrue(vs.iterator().hasNext());
        assertEquals(1, vs.size());
        assertEquals("1.19-launch-test", vs.iterator().next().getName());
    }

    private void resetLauncher() {
        launcher.getJavaService().clear();
        launcher.getJavaService().add(new Java("java17", 17));
        launcher.getJavaService().add(new Java("java8", 8));
        launcher.setCommandContext(new LaunchContext(launcher));
    }

    @SneakyThrows
    private void unpackVersion() {
        val versionFile = launcher.getMcFiles().create(
            "versions", "version", "version.json");
        @Cleanup
        val is = ResourceUtil.getResource("launch.json");
        @Cleanup
        val os = new FileOutputStream(versionFile);
        IOUtil.copy(is, os);
    }

}
