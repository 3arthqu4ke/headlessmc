package me.earth.headlessmc.testplugin;

import me.earth.headlessmc.api.process.ReadableOutputStream;
import me.earth.headlessmc.api.process.ReadablePrintStream;
import me.earth.headlessmc.api.process.WritableInputStream;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
        launcher.getExitManager().setExitManager(i -> System.out.println("Test plugin active, exited with code " + i));
        // TransformerPlugin will not let us load this class
        assertThrows(ClassNotFoundException.class, () -> Class.forName("me.earth.headlessmc.testplugin.DummyClassThatCantBeLoaded"));
        //@SuppressWarnings("resource") ReadablePrintStream out = new ReadablePrintStream();
        //launcher.getInAndOutProvider().setOut(() -> out);
        //launcher.getInAndOutProvider().setErr(() -> out);
        @SuppressWarnings("resource") WritableInputStream in = new WritableInputStream();
        launcher.getInAndOutProvider().setIn(() -> in);

        in.getPrintStream().println("download 1.12.2");
        in.getPrintStream().println("forge 1.12.2");
        in.getPrintStream().println("y");
    }

}
