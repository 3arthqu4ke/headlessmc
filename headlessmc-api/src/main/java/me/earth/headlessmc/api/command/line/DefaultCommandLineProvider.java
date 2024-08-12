package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.process.InAndOutProvider;

import java.io.Console;
import java.util.function.Supplier;

@CustomLog
@RequiredArgsConstructor
public class DefaultCommandLineProvider implements Supplier<CommandLineReader> {
    private final InAndOutProvider inAndOutProvider;

    @Override
    public CommandLineReader get() {
        Console console = inAndOutProvider.getConsole().get();
        if (console != null) {
            return new ConsoleCommandLineReader(console);
        } else { // e.g. in IntelliJ's run tab terminal
            log.warn("Your terminal cannot hide passwords!");
            return new BufferedCommandLineReader();
        }
    }

}
