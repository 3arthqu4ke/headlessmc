package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.process.InAndOutProvider;

import java.io.Console;
import java.util.function.Supplier;

@CustomLog
@RequiredArgsConstructor
public class DefaultCommandLineProvider implements Supplier<CommandLineListener> {
    private final InAndOutProvider inAndOutProvider;

    @Override
    public CommandLineListener get() {
        return hmc -> {
            Console console = inAndOutProvider.getConsole().get();
            if (console == null) {
                log.warn("Your terminal cannot hide passwords!");
                new BufferedCommandLineListener().listen(hmc);
            } else {
                new ConsoleCommandLineListener(console).listen(hmc);
            }
        };
    }

}
