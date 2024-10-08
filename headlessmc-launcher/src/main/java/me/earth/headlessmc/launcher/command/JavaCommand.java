package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.util.Table;
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
    public void execute(String line, String... args) {
        if (CommandUtil.hasFlag("-current", args)) {
            Java current = ctx.getJavaService().getCurrent();
            ctx.log("Current: Java " + current.getVersion() + " at " + current.getPath());
            return;
        }

        ctx.getJavaService().refresh();
        ctx.log(new Table<Java>()
                    .withColumn("version", java -> String.valueOf(java.getVersion()))
                    .withColumn("path", Java::getPath)
                    .withColumn("current", java -> java.equals(ctx.getJavaService().getCurrent()) ? "<------" : "")
                    .addAll(ctx.getJavaService())
                    .build());
    }

}
