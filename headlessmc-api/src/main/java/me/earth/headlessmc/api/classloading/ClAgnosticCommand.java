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
 * A {@link Command} implementation that delegates to another command.
 * This delegation happens via reflection so any object that exposes the same methods as {@link Command} can be used.
 * This is generally meant for environments where the Command class has been loaded multiple times from multiple ClassLoaders.
 * Through the Reflection delegation this allows you to communicate commands between ClassLoaders.
 *
 * @see ApiClassloadingHelper
 */
@Data
public class ClAgnosticCommand implements Command {
    /**
     * A remote instance of {@link Command}, potentially loaded by another classloader.
     * Because of that we cannot cast it to a Command directly,
     * but need to use reflection to access its methods.
     */
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
