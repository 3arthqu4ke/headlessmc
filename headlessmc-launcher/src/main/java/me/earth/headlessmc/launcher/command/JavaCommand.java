package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.java.Java;

public class JavaCommand extends AbstractLauncherCommand {
    public JavaCommand(Launcher launcher) {
        super(launcher, "java", "Reloads the config property "
            + LauncherProperties.JAVA + ".");
    }

    @Override
    public void execute(String... args) {
        ctx.getJavaService().refresh();
        for (Java java : ctx.getJavaService()) {
            ctx.log(java.toString());
        }
    }

}
