package me.earth.headlessmc.launcher.command;

import com.google.gson.GsonBuilder;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.version.Version;

public class JsonCommand extends AbstractVersionCommand {
    public JsonCommand(Launcher launcher) {
        super(launcher, "json", "Dumps version.json files.");
    }

    @Override
    public void execute(Version version, String... args) {
        ctx.log(new GsonBuilder().setPrettyPrinting()
                                 .create()
                                 .toJson(version.getJson()));
    }

}
