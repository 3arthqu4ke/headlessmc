package me.earth.headlessmc.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;

import java.util.LinkedHashMap;
import java.util.Map;

@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AbstractCommand implements Command {
    protected final Map<String, String> args = new LinkedHashMap<>();
    protected final HeadlessMc ctx;
    @Getter
    protected final String name;
    @Getter
    protected final String description;

    @Override
    public boolean matches(String... args) {
        return args.length > 0 && args[0].equalsIgnoreCase(getName());
    }

    @Override
    public Iterable<String> getArgs() {
        return args.keySet();
    }

    @Override
    public String getArgDescription(String arg) {
        return args.get(arg);
    }

}
