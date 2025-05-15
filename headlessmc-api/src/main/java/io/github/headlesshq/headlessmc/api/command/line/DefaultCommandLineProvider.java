package io.github.headlesshq.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import io.github.headlesshq.headlessmc.api.process.InAndOutProvider;

import java.io.Console;
import java.util.function.Supplier;

/**
 * A {@link CommandLineReader} that decides whether to use a {@link BufferedCommandLineReader} or a {@link ConsoleCommandLineReader}.
 * If the {@link InAndOutProvider}{@code .getConsole()} is available a {@link ConsoleCommandLineReader} will be used always.
 *
 * @see BufferedCommandLineReader
 * @see ConsoleCommandLineReader
 */
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
