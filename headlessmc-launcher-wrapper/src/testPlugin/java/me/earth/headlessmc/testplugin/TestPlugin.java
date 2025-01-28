package me.earth.headlessmc.testplugin;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.line.DefaultCommandLineProvider;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;
import org.junit.platform.commons.util.ExceptionUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CustomLog
public class TestPlugin implements HeadlessMcPlugin {
    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void init(Launcher launcher) {
        launcher.getExitManager().setExitManager(i -> assertEquals(0, i, "Exit code should be 0!"));
        launcher.getExitManager().setMainThreadEndHook(throwable -> {
            if (throwable != null) {
                Throwable cause = throwable;
                while (cause != null) {
                    if (cause instanceof ExitTrap.ExitTrappedException) {
                        if (((ExitTrap.ExitTrappedException) cause).getStatus() != 0) {
                            log.error("ExitCode != 0!");
                            ExceptionUtils.throwAsUncheckedException(throwable);
                        }

                        return;
                    }

                    cause = cause.getCause();
                }

                ExceptionUtils.throwAsUncheckedException(throwable); // TODO: this is caught and added as suppressed?
            }
        });
        // TransformerPlugin will not let us load this class
        assertThrows(ClassNotFoundException.class, () -> Class.forName("me.earth.headlessmc.testplugin.DummyClassThatCantBeLoaded"));
        // ReadablePrintStream out = new ReadablePrintStream();
        // launcher.getInAndOutProvider().setOut(() -> out);
        // launcher.getInAndOutProvider().setErr(() -> out);
        TestInputStream in = new TestInputStream();
        launcher.getCommandLine().getInAndOutProvider().setIn(() -> in);
        launcher.getCommandLine().setCommandLineProvider(new DefaultCommandLineProvider(launcher.getCommandLine().getInAndOutProvider()));

        System.setProperty(LauncherProperties.RE_THROW_LAUNCH_EXCEPTIONS.getName(), "true");
        Java java = launcher.getJavaService().getCurrent();
        if (java == null) {
            java = new Java("unknown", launcher.getConfig().get(LauncherProperties.ASSUMED_JAVA_VERSION, 8L).intValue());
        }

        LaunchTest.build(java, launcher, in);
    }

    @Override
    public String getDescription() {
        return "A test plugin.";
    }

}
