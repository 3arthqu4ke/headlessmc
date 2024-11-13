package me.earth.headlessmc.launcher.launch;

import lombok.Builder;
import lombok.CustomLog;
import lombok.Data;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.auth.LaunchAccount;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.version.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.earth.headlessmc.api.command.CommandUtil.flag;
import static me.earth.headlessmc.launcher.LauncherProperties.*;

@Data
@Builder
@CustomLog
public class LaunchOptions {
    private final Version version;
    private final Launcher launcher;
    private final FileManager files;
    private final List<String> additionalJvmArgs;
    private final LaunchAccount account;
    private final boolean runtime;
    private final boolean lwjgl;
    private final boolean jndi;
    private final boolean lookup;
    private final boolean paulscode;
    private final boolean noOut;
    private final boolean noIn;
    private final boolean inMemory;
    private final boolean forceSimple;
    private final boolean forceBoot;
    private final boolean xvfb;
    private final boolean prepare;
    private final boolean closeCommandLine;

    @SuppressWarnings("unused")
    public static class LaunchOptionsBuilder {
        private LaunchOptionsBuilder() {
            this.additionalJvmArgs = Collections.emptyList();
        }

        public LaunchOptionsBuilder parseFlags(
            Launcher ctx, boolean quit, String... args) {
            boolean xvfb = false;
            boolean lwjgl = flag(ctx, "-lwjgl", INVERT_LWJGL_FLAG, ALWAYS_LWJGL_FLAG, args);
            // if offline only allow launching with the lwjgl flag!
            if (!lwjgl && launcher.getAccountManager().getOfflineChecker().isOffline()) {
                xvfb = new XvfbService(launcher.getConfigService(), launcher.getProcessFactory().getOs()).isRunningWithXvfb();
                if (!xvfb) {
                    log.warning("You are offline, game will start in headless mode!");
                    lwjgl = true;
                } else {
                    log.info("You are offline but running with xvfb, not using headless mode.");
                }
            }

            return this
                .runtime(CommandUtil.hasFlag("-commands", args))
                .lwjgl(lwjgl)
                .inMemory(CommandUtil.hasFlag("-inmemory", args) || launcher.getConfig().get(ALWAYS_IN_MEMORY, false))
                .jndi(flag(ctx, true, "-jndi", INVERT_JNDI_FLAG, ALWAYS_JNDI_FLAG, args))
                .lookup(flag(ctx, true, "-lookup", INVERT_LOOKUP_FLAG, ALWAYS_LOOKUP_FLAG, args))
                .paulscode(flag(ctx, "-paulscode", INVERT_PAULS_FLAG, ALWAYS_PAULS_FLAG, args))
                .noOut(quit || CommandUtil.hasFlag("-noout", args))
                .forceSimple(CommandUtil.hasFlag("-forceSimple", args))
                .forceBoot(CommandUtil.hasFlag("-forceBoot", args))
                .parseJvmArgs(args)
                .xvfb(xvfb)
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
    }

}
