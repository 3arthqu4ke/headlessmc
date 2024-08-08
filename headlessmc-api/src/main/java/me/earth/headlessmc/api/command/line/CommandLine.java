package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.*;
import me.earth.headlessmc.api.process.InAndOutProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOError;
import java.util.Collections;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@Setter
@CustomLog
public class CommandLine implements PasswordAware, QuickExitCli, HasCommandContext, CommandLineListener {
    private final InAndOutProvider inAndOutProvider = new InAndOutProvider();
    private volatile Supplier<CommandLineListener> commandLineProvider = new DefaultCommandLineProvider(inAndOutProvider);
    /**
     * The initial CommandContext, so that we do not lose it...
     */
    private volatile CommandContext baseContext = EmptyCommandContext.INSTANCE;
    /**
     * The currently active CommandContext which executes commands written on the CommandLine managed by this CommandLineManager.
     */
    private volatile CommandContext commandContext = EmptyCommandContext.INSTANCE;
    private volatile Consumer<String> commandLineReader = line -> getCommandContext().execute(line);
    private volatile @Nullable CommandLineListener commandLineListener;

    private volatile boolean quickExitCli;
    private volatile boolean waitingForInput;
    private volatile boolean hidingPasswords;
    private volatile boolean hidingPasswordsSupported;

    public CommandLine() {
        setHidingPasswordsSupported(inAndOutProvider.getConsole().get() != null);
    }

    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        if (this.commandLineListener != null) {
            log.warn("Listen called, but a CommandLineListener already exists!");
        }

        CommandLineListener commandLineListener = commandLineProvider.get();
        this.commandLineListener = commandLineListener;
        commandLineListener.listen(hmc);
    }

    private static final class EmptyCommandContext implements CommandContext {
        private static final EmptyCommandContext INSTANCE = new EmptyCommandContext();

        @Override
        public void execute(String command) {
            log.warn("Did you forge to set the CommandContext? EmptyCommandContext: " + command);
        }

        @NotNull
        @Override
        public Iterator<Command> iterator() {
            return Collections.emptyIterator();
        }
    }

}
