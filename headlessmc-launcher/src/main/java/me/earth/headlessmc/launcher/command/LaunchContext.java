package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.command.*;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.download.DownloadCommand;
import me.earth.headlessmc.launcher.command.forge.ForgeCommand;
import me.earth.headlessmc.launcher.command.login.LoginCommand;

public class LaunchContext extends CommandContextImpl {
    public LaunchContext(Launcher ctx) {
        super(ctx);
        add(new LaunchCommand(ctx));
        add(new QuitCommand(ctx));
        add(new FabricCommand(ctx));
        add(ForgeCommand.lexforge(ctx));
        add(ForgeCommand.neoforge(ctx));
        add(new JsonCommand(ctx));
        add(new HelpCommand(ctx));
        add(new JavaCommand(ctx));
        add(new MemoryCommand(ctx));
        add(new VersionsCommand(ctx));
        add(new LogLevelCommand(ctx));
        add(new ConfigCommand(ctx));
        add(new PasswordCommand(ctx));
        add(new LoginCommand(ctx));
        add(new DownloadCommand(ctx));
        add(new MultiCommand(ctx));
    }

}
