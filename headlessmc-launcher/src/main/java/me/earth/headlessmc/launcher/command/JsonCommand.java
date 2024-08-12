package me.earth.headlessmc.launcher.command;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.util.JsonUtil;
import me.earth.headlessmc.launcher.version.Version;

import java.io.File;
import java.io.IOException;

public class JsonCommand extends AbstractVersionCommand {
    public JsonCommand(Launcher launcher) {
        super(launcher, "json", "Dumps version.json files.");
        args.put("<version/file>", "The json file to dump.");
        args.put("-file", "If you are targeting a file instead of a version.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (CommandUtil.hasFlag("-file", args) && args.length > 1) {
            try {
                log(JsonUtil.fromFile(new File(args[1])));
            } catch (IOException e) {
                throw new CommandException("Unable to read file " + args[1]
                                               + ": " + e.getMessage());
            }
        } else {
            super.execute(line, args);
        }
    }

    @Override
    public void execute(Version version, String... args) {
        log(version.getJson());
    }

    private void log(JsonElement element) {
        ctx.log(new GsonBuilder().setPrettyPrinting()
                                 .create()
                                 .toJson(element));
    }

}
