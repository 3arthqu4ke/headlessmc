package me.earth.headlessmc.testplugin;

import lombok.CustomLog;
import me.earth.headlessmc.api.process.ReadablePrintStream;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.platform.commons.util.ExceptionUtils;

import static org.junit.jupiter.api.Assertions.*;

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
        launcher.getInAndOutProvider().setIn(() -> in);

        LaunchTest.build(launcher.getJavaService().getCurrent(), launcher, in);
    }

}
