package me.earth.headlessmc.api.classloading;

import lombok.Data;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.util.ReflectionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @see ApiClassloadingHelper
 */
@Data
public class ClAgnosticCommandContext implements CommandContext {
    /**
     * A remote instance of {@link CommandLine}, loaded by another classloader.
     * Because of that we cannot cast it to a CommandContext directly,
     * but need to use reflection to access its methods.
     * This CommandContext delegates to the commandContext provided by this command line.
     */
    private final Object commandLine;

    @Override
    public void execute(String command) {
        ReflectionUtil.invoke("execute", getCommandContext(), null, new Class<?>[]{String.class}, command);
    }

    @Override
    public List<Map.Entry<String, @Nullable String>> getCompletions(String line) {
        return ReflectionUtil.invoke("getCompletions", getCommandContext(), new ArrayList<>(), new Class<?>[]{String.class}, line);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Command> iterator() {
        List<Command> commands = new ArrayList<>();
        // CommandContext implements Iterable, it should be safe to cast to java.lang classes
        for (Object command : (Iterable<Object>) getCommandContext()) {
            commands.add(new ClAgnosticCommand(command));
        }

        return commands.iterator();
    }

    private Object getCommandContext() {
        return ReflectionUtil.invoke("getCommandContext", commandLine, null, new Class[0]);
    }

}
