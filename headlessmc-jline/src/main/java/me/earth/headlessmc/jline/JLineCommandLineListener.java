package me.earth.headlessmc.jline;

import lombok.CustomLog;
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

@CustomLog
public class JLineCommandLineListener implements CommandLineListener {
    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        if (hmc.getConfig().get(JLineProperties.PREVENT_DEPRECATION_WARNING, true)) {
            System.setProperty("org.jline.terminal.disableDeprecatedProviderWarning", "true");
        }

        CommandLine commandLine = hmc.getCommandLine();
        boolean dumb = hmc.getConfig().get(JLineProperties.DUMB, false) || System.getProperty("java.class.path").contains("idea_rt.jar");
        String providers = dumb ? "dumb" : hmc.getConfig().get(JLineProperties.PROVIDERS, "jni");
        InAndOutProvider io = commandLine.getInAndOutProvider();
        //try (Terminal terminal = TerminalBuilder.builder().streams(io.getIn().get(), io.getOut().get()).providers(providers).type(dumb ? Terminal.TYPE_DUMB : null).build()) {
        //io.setIn(() -> System.in);
        //io.setOut(() -> System.out);
        try (Terminal terminal = TerminalBuilder.builder()
                                                //.streams(hmc.getConfig().get(JLineProperties.JLINE_IN, true) ? io.getIn().get() : null,
                                                //         hmc.getConfig().get(JLineProperties.JLINE_OUT, true) ? io.getOut().get() : null)
                                                /*.jni(providers.contains("jni"))
                                                .jna(providers.contains("jna"))
                                                .jansi(providers.contains("jansi"))
                                                .ffm(providers.contains("ffm"))
                                                .exec(providers.contains("exec"))*/
                                                .ffm(hmc.getConfig().get(JLineProperties.FFM, false))
                                                .jansi(false)
                                                .jna(true)
                                                //.dumb(dumb)
                                                .providers("jna")
                                                //.providers(providers)
                                                //.type(dumb ? Terminal.TYPE_DUMB : null)
                                                .build()) {
            log.info("JLine Terminal type: " + terminal.getType() + ", name: " + terminal.getName() + " (" + terminal + ")");
            LineReader reader = LineReaderBuilder.builder().appName("HeadlessMC").terminal(terminal).completer(new CommandCompleter(hmc)).build();
            reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);
            if (!hmc.getConfig().get(JLineProperties.BRACKETED_PASTE, true)) {
                reader.unsetOpt(LineReader.Option.BRACKETED_PASTE);
            }

            reader.unsetOpt(LineReader.Option.INSERT_TAB);

            String readPrefix = hmc.getConfig().get(JLineProperties.READ_PREFIX, null);
            String line;
            while (true) {
                try {
                    line = commandLine.isHidingPasswords() ? reader.readLine(readPrefix, '*') :  reader.readLine(readPrefix);
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

}
