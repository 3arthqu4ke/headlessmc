package me.earth.headlessmc.launcher.server.commands;

import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandContextImpl;
import me.earth.headlessmc.api.command.impl.HelpCommand;
import me.earth.headlessmc.api.command.impl.QuitCommand;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;

import java.util.Locale;

public class ServerCommandContext extends CommandContextImpl {
    public ServerCommandContext(Launcher launcher, boolean serverMode) {
        super(launcher);
        add(new AddServerCommand(launcher));
        add(new LaunchServerCommand(launcher));
        add(new RemoveServerCommand(launcher));
        add(new ListServersCommand(launcher));
        add(new EulaCommand(launcher));
        if (launcher.getConfig().get(LauncherProperties.SERVER_TEST_CACHE, false)) {
            add(new CacheServerCommand(launcher));
        }

        if (serverMode) {
            add(new HelpCommand(launcher));
            add(new QuitCommand(launcher));
            add(new ClientCommand(launcher));
        }
    }

    @Override
    public void execute(String message) {
        if (message.toLowerCase(Locale.ENGLISH).startsWith("server ")) {
            this.execute(message.substring("server ".length()));
        } else {
            super.execute(message);
        }
    }

    private static class ClientCommand extends AbstractLauncherCommand {
        private final CommandContext originalContext;

        public ClientCommand(Launcher ctx) {
            super(ctx, "client", "Return to client mode.");
            this.originalContext = ctx.getCommandLine().getBaseContext();
        }

        @Override
        public void execute(String line, String... args) {
            ctx.getCommandLine().setAllContexts(originalContext);
        }
    }

}
