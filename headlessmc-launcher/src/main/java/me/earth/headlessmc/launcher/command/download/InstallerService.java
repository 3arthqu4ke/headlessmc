package me.earth.headlessmc.launcher.command.download;

import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.FabricCommand;
import me.earth.headlessmc.launcher.command.forge.ForgeCommand;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class InstallerService {
    private final Map<Modlauncher, ModLauncherCommand> modLauncherCommands = new EnumMap<>(Modlauncher.class);
    private final Launcher launcher;
    private final DownloadCommand downloadCommand;

    public InstallerService(Launcher launcher) {
        this.launcher = launcher;
        // TODO: this is super bad!!!
        this.downloadCommand = findCommand(launcher, DownloadCommand.class, DownloadCommand::new);
        this.modLauncherCommands.put(Modlauncher.FABRIC, findCommand(launcher, FabricCommand.class, FabricCommand::new));
        this.modLauncherCommands.put(Modlauncher.LEXFORGE, findCommand(launcher, ForgeCommand.class, ForgeCommand::lexforge));
        this.modLauncherCommands.put(Modlauncher.NEOFORGE, findCommand(launcher, ForgeCommand.class, ForgeCommand::neoforge));
    }

    public void install(VersionArgument versionArgument, String... args) throws CommandException {
        Version vanilla = launcher.getVersionService().getVersionByName(versionArgument.getName());
        if (vanilla == null) {
            downloadCommand.download(versionArgument.getName());
        }

        installModLauncher(versionArgument, args);
    }

    public void installModLauncher(VersionArgument versionArgument, String... args) throws CommandException {
        if (versionArgument.getModlauncher() == null) {
            return;
        }

        ModLauncherCommand command = modLauncherCommands.get(versionArgument.getModlauncher());
        if (command == null) {
            throw new CommandException("Unknown mod launcher " + versionArgument.getModlauncher());
        }

        Version vanilla = launcher.getVersionService().getVersionByName(versionArgument.getName());
        if (vanilla == null) {
            throw new CommandException("Failed to find version " + versionArgument.getName());
        }

        command.install(vanilla, versionArgument.getModLauncherVersion(), CommandUtil.hasFlag("-modlauncherInMemory", args));
    }

    private <C extends Command> @Nullable C findCommand(Launcher launcher, Class<C> clazz, Function<Launcher, C> fallback) {
        for (Command command : launcher.getCommandLine().getCommandContext()) {
            if (clazz.isAssignableFrom(command.getClass())) {
                return clazz.cast(command);
            }
        }

        return fallback.apply(launcher);
    }

}
