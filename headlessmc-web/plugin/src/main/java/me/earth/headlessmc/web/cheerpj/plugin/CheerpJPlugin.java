package me.earth.headlessmc.web.cheerpj.plugin;

import lombok.SneakyThrows;
import me.earth.headlessmc.api.command.line.BufferedCommandLineReader;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;
import me.earth.headlessmc.logging.Logger;
import me.earth.headlessmc.logging.LoggerFactory;
import me.earth.headlessmc.logging.LoggingService;

import java.awt.*;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class CheerpJPlugin implements HeadlessMcPlugin {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    @SneakyThrows
    public void init(Launcher launcher) {
        CheerpJGUI gui = CheerpJGUI.getInstance();
        if (gui.isInitialized()) {
            return;
        }

        Logger logger = LoggerFactory.getLogger("GUI");
        if (GraphicsEnvironment.isHeadless()) {
            logger.error("GraphicsEnvironment is headless!");
            return;
        }

        gui.init();
        CheerpJMain.setupInAndOutProvider(gui, launcher.getCommandLine().getInAndOutProvider());
        LoggingService loggingService = launcher.getLoggingService();
        loggingService.setStreamFactory(() -> launcher.getCommandLine().getInAndOutProvider().getOut().get());
        loggingService.init(); // reinitialize

        logger.info("HeadlessMc GUI initialized.");

        PipedInputStream inputStream = new PipedInputStream();
        PrintStream outPipe = new PrintStream(new PipedOutputStream(inputStream));
        gui.getCommandHandler().set(outPipe::println);

        launcher.getCommandLine().getInAndOutProvider().setIn(() -> inputStream);
        launcher.getCommandLine().setCommandLineProvider(BufferedCommandLineReader::new);
        // TODO: when starting a process we gotta pump its in and outputstreams to the gui!
    }

    @Override
    public String getName() {
        return "CheerpJ";
    }

    @Override
    public String getDescription() {
        return "Makes HeadlessMc run in your browser.";
    }

}
