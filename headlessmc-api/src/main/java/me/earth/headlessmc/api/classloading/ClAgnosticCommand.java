package me.earth.headlessmc.api.classloading;

import lombok.Data;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.util.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @see ApiClassloadingHelper
 */
@Data
public class ClAgnosticCommand implements Command {
    private final Object delegate;

    @Override
    public void execute(String line, String... args) throws CommandException {
        ReflectionUtil.invoke("execute", delegate, null, new Class<?>[]{ String.class, String[].class}, line, args);
    }

    @Override
    public boolean matches(String line, String... args) {
        return ReflectionUtil.invoke("matches", delegate, false, new Class<?>[]{String.class, String[].class}, line, args);
    }

    @Override
    public String getName() {
        return ReflectionUtil.invoke("getName", delegate, "unknown-command?", new Class<?>[0]);
    }

    @Override
    public Iterable<String> getArgs() {
        return ReflectionUtil.invoke("getArgs", delegate, Collections.emptyList(), new Class<?>[0]);
    }

    @Override
    public String getArgDescription(String arg) {
        return ReflectionUtil.invoke("getArgDescription", delegate, "", new Class<?>[]{String.class}, arg);
    }

    @Override
    public Iterable<Map.Entry<String, String>> getArgs2Descriptions() {
        return ReflectionUtil.invoke("getArgs2Descriptions", delegate, new ArrayList<>(), new Class<?>[0]);
    }

    @Override
    public String getDescription() {
        return ReflectionUtil.invoke("getDescription", delegate, "", new Class<?>[0]);
    }

    @Override
    public void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        ReflectionUtil.invoke("getCompletions", delegate, null, new Class<?>[]{String.class, List.class, String[].class}, line, completions, args);
    }

}
