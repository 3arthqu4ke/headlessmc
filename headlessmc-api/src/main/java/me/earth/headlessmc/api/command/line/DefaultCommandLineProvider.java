package me.earth.headlessmc.api.command.line;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.process.InAndOutProvider;

import java.io.Console;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DefaultCommandLineProvider implements Supplier<CommandLine> {
    private final InAndOutProvider inAndOutProvider;

    @Override
    public CommandLine get() {
        return hmc -> {
            Console console = inAndOutProvider.getConsole().get();
            if (console == null) {
                new BufferedCommandLine().listen(hmc);
            } else {
                new ConsoleCommandLine(console).listen(hmc);
            }
        };
    }

}
