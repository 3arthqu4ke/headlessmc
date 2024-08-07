package me.earth.headlessmc.api.command.line;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.*;
import me.earth.headlessmc.api.process.InAndOutProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
public class CommandLineManager implements PasswordAware, QuickExitCli, HasCommandContext, CommandLine {
    private final InAndOutProvider inAndOutProvider = new InAndOutProvider();
    private volatile Supplier<CommandLine> commandLineProvider = new DefaultCommandLineProvider(inAndOutProvider);
    private volatile CommandContext commandContext = EmptyCommandContext.INSTANCE;
    private volatile Consumer<String> commandLineReader = line -> getCommandContext().execute(line);

    private volatile boolean quickExitCli;
    private volatile boolean waitingForInput;
    private volatile boolean hidingPasswords;
    private volatile boolean hidingPasswordsSupported;

    public CommandLineManager() {
        setHidingPasswordsSupported(inAndOutProvider.getConsole().get() != null);
    }

    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        commandLineProvider.get().listen(hmc);
    }

    private static final class EmptyCommandContext implements CommandContext {
        private static final EmptyCommandContext INSTANCE = new EmptyCommandContext();

        @Override
        public void execute(String command) {

        }

        @NotNull
        @Override
        public Iterator<Command> iterator() {
            return Collections.emptyIterator();
        }
    }

}
