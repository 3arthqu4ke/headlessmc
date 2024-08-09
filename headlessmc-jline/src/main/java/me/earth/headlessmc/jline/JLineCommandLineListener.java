package me.earth.headlessmc.jline;

import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.command.line.CommandLineListener;
import me.earth.headlessmc.api.process.InAndOutProvider;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOError;
import java.io.IOException;

@Getter
@CustomLog
public class JLineCommandLineListener implements CommandLineListener {
    @Setter
    private volatile String readPrefix;
    private volatile LineReader lineReader;
    private volatile Terminal terminal;

    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        long nanos = System.nanoTime();
        if (hmc.getConfig().get(JLineProperties.PREVENT_DEPRECATION_WARNING, true)) {
            System.setProperty("org.jline.terminal.disableDeprecatedProviderWarning", "true");
        }

        CommandLine commandLine = hmc.getCommandLine();
        boolean dumb = !hmc.getConfig().get(JLineProperties.FORCE_NOT_DUMB, false)
                && (hmc.getConfig().get(JLineProperties.DUMB, false)
                    || System.console() == null && hmc.getConfig().get(JLineProperties.DUMB_WHEN_NO_CONSOLE, true)
                    || System.getProperty("java.class.path").contains("idea_rt.jar"));

        String providers = hmc.getConfig().get(JLineProperties.PROVIDERS, "jni");
        InAndOutProvider io = commandLine.getInAndOutProvider();
        // terribly complicated TerminalBuilder because on Windows JLine cannot be trusted to find the correct provider?
        try (Terminal terminal = buildTerminal(hmc, dumb, providers, io).build()) {
            log.info("JLine Terminal type: " + terminal.getType() + ", name: " + terminal.getName() + " (" + terminal + ")");
            LineReader reader = LineReaderBuilder.builder().appName("HeadlessMC").terminal(terminal).completer(new CommandCompleter(hmc)).build();
            reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);
            if (!hmc.getConfig().get(JLineProperties.BRACKETED_PASTE, true)) {
                reader.unsetOpt(LineReader.Option.BRACKETED_PASTE);
            }

            reader.unsetOpt(LineReader.Option.INSERT_TAB);
            this.readPrefix = hmc.getConfig().get(JLineProperties.READ_PREFIX, null);
            this.terminal = terminal;
            this.lineReader = reader;

            nanos = System.nanoTime() - nanos;
            log.info("JLine terminal took " + (nanos / 1_000_000.0) + "ms to get ready.");
            String line;
            while (true) {
                try {
                    line = commandLine.isHidingPasswords() ? reader.readLine(readPrefix, '*') : reader.readLine(readPrefix);
                } catch (EndOfFileException ignored) {
                    // Continue reading after EOT
                    continue;
                }

                if (line == null) {
                    break;
                }

                line = line.trim();
                commandLine.getCommandLineReader().accept(line);
                if (commandLine.isQuickExitCli()) {
                    if (!commandLine.isWaitingForInput()) {
                        break;
                    }

                    log.debug("Waiting for more input...");
                }
            }
        } catch (UserInterruptException | IOException e) {
            // TODO: on UserInterruptException kill Mc process?
            throw new IOError(e);
        }
    }

    protected TerminalBuilder buildTerminal(HeadlessMc hmc, boolean dumb, String providers, InAndOutProvider io) throws IOException {
        TerminalBuilder terminalBuilder = TerminalBuilder
                .builder()
                .streams(hmc.getConfig().get(JLineProperties.JLINE_IN, false) ? io.getIn().get() : null,
                        hmc.getConfig().get(JLineProperties.JLINE_OUT, false) ? io.getOut().get() : null)
                .exec(hmc.getConfig().get(JLineProperties.EXEC, false))
                .jna(hmc.getConfig().get(JLineProperties.JNI, true))
                .jansi(hmc.getConfig().get(JLineProperties.JANSI, false))
                .jna(hmc.getConfig().get(JLineProperties.JNA, true))
                .system(hmc.getConfig().get(JLineProperties.SYSTEM, true))
                .dumb(dumb)
                .type(hmc.getConfig().get(JLineProperties.TYPE, null));
        try {
            terminalBuilder.ffm(hmc.getConfig().get(JLineProperties.FFM, false));
            terminalBuilder.providers(providers);
        } catch (NoSuchMethodError ignored) { // e.g. 1.12.2 ships an older version of JLine which does not have this
            log.debug("Running an older version of JLine, FFM and/or providers not supported.");
        }

        return terminalBuilder;
    }

}
