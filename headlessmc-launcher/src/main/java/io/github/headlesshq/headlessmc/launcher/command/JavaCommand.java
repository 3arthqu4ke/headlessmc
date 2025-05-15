package io.github.headlesshq.headlessmc.launcher.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.CommandUtil;
import io.github.headlesshq.headlessmc.api.command.ParseUtil;
import io.github.headlesshq.headlessmc.api.util.Table;
import io.github.headlesshq.headlessmc.java.Java;
import io.github.headlesshq.headlessmc.java.download.JavaDownloadRequest;
import io.github.headlesshq.headlessmc.java.download.JavaDownloaderManager;
import io.github.headlesshq.headlessmc.jline.JLineProperties;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.LauncherProperties;

import java.io.IOException;

public class JavaCommand extends AbstractLauncherCommand {
    public JavaCommand(Launcher launcher) {
        super(launcher, "java", "Reloads the config property "
            + LauncherProperties.JAVA.getName() + ".");
        args.put("<version>", "Specify a version of Java to download.");
        args.put("<distribution>", "Specify a distribution of Java to download.");
        args.put("-jdk", "Download the Java JDK instead of the JRE.");
        args.put("-current", "Output the java version of the JVM headlessmc is running in.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (CommandUtil.hasFlag("-current", args)) {
            Java current = ctx.getJavaService().getCurrent();
            if (current != null) {
                ctx.log("Current: Java " + current.getVersion() + " at " + current.getPath());
            } else {
                ctx.log("Current Java unknown.");
            }

            return;
        }

        if (args.length == 1) {
            ctx.getJavaService().refresh();
            ctx.log(new Table<Java>()
                    .withColumn("version", java -> String.valueOf(java.getVersion()))
                    .withColumn("path", Java::getPath)
                    .withColumn("current", java -> java.equals(ctx.getJavaService().getCurrent()) ? "<------" : "")
                    .addAll(ctx.getJavaService())
                    .build());
        } else {
            int javaVersion = ParseUtil.parseI(args[1]);
            String distribution = args.length > 2 ? args[2] : JavaDownloaderManager.DEFAULT_DISTRIBUTION;
            boolean jdk = args.length > 3 && args[3].equalsIgnoreCase("-jdk");

            try {
                if (!ctx.getConfig().get(JLineProperties.ENABLE_PROGRESS_BAR, true)
                    || !ctx.getConfig().get(JLineProperties.ENABLED, true)) {
                    ctx.log("Downloading " + javaVersion + (jdk ? " (JDK)" : "") + " from " + distribution + ".");
                }

                ctx.getJavaDownloaderManager().download(
                        ctx.getFileManager().getDir("java").toPath(),
                        new JavaDownloadRequest(
                            ctx.getDownloadService(),
                            ctx.getCommandLine(),
                            javaVersion,
                            distribution,
                            ctx.getProcessFactory().getOs(),
                            jdk
                        )
                );

                ctx.getJavaService().refreshHeadlessMcJavaVersions();
                if (!ctx.getConfig().get(JLineProperties.ENABLE_PROGRESS_BAR, true)
                        || !ctx.getConfig().get(JLineProperties.ENABLED, true)) {
                    ctx.log("Success.");
                }
            } catch (IOException e) {
                throw new CommandException("Failed to download Java version " + javaVersion + (jdk ? " (JDK)" : "") + " " + distribution + ": " + e.getMessage(), e);
            }
        }
    }

}
