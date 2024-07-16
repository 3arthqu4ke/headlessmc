package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.java.Java;

public class JavaCommand extends AbstractLauncherCommand {
    public JavaCommand(Launcher launcher) {
        super(launcher, "java", "Reloads the config property "
            + LauncherProperties.JAVA.getName() + ".");
        args.put("-current", "Output the java version of the JVM headlessmc is running in.");
    }

    @Override
    public void execute(String... args) {
        if (CommandUtil.hasFlag("-current", args)) {
            ctx.log(Java.current().toString());
            return;
        }

        ctx.getJavaService().refresh();
        for (Java java : ctx.getJavaService()) {
            ctx.log(java.toString());
        }
    }

}
