package me.earth.headlessmc.launcher.launch;

import lombok.Builder;
import lombok.Data;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.version.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.earth.headlessmc.launcher.LauncherProperties.*;

@Data
@Builder
public class LaunchOptions {
    private final Version version;
    private final Launcher launcher;
    private final FileManager files;
    private final List<String> additionalJvmArgs;
    private final boolean runtime;
    private final boolean lwjgl;
    private final boolean jndi;
    private final boolean lookup;
    private final boolean paulscode;
    private final boolean noOut;
    private final boolean noIn;

    public static class LaunchOptionsBuilder {
        private LaunchOptionsBuilder() {
            this.additionalJvmArgs = Collections.emptyList();
        }

        public LaunchOptionsBuilder parseFlags(
            Launcher ctx, boolean quit, String... args) {
            return this
                .runtime(CommandUtil.hasFlag("-commands", args))    // if offline only allow launching with the lwjgl flag!
                .lwjgl(flag(ctx, "-lwjgl", INVERT_LWJGL_FLAG, args) || launcher.getAccountManager().getOfflineChecker().isOffline())
                .jndi(flag(ctx, "-jndi", INVERT_JNDI_FLAG, args))
                .lookup(flag(ctx, "-lookup", INVERT_LOOKUP_FLAG, args))
                .paulscode(flag(ctx, "-paulscode", INVERT_PAULS_FLAG, args))
                .noOut(quit || CommandUtil.hasFlag("-noout", args))
                .parseJvmArgs(args)
                .noIn(quit);
        }

        public LaunchOptionsBuilder parseJvmArgs(String... args) {
            String jvmArgs = CommandUtil.getOption("--jvm", args);
            if (jvmArgs != null) {
                this.additionalJvmArgs = new ArrayList<>(
                    Arrays.asList(CommandUtil.split(jvmArgs)));
            }

            return this;
        }

        private boolean flag(
            HasConfig ctx, String flg, Property<Boolean> inv, String... args) {
            return CommandUtil.hasFlag(flg, args)
                ^ ctx.getConfig().get(inv, false);
        }
    }

}
