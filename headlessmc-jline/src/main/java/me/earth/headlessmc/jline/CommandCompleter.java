package me.earth.headlessmc.jline;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
public class CommandCompleter implements Completer {
    private final HeadlessMc hmc;

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String parsedLine = line.line().toLowerCase(Locale.ENGLISH);
        for (Command command : hmc.getCommandLine().getCommandContext()) {
            if (command.getName().toLowerCase(Locale.ENGLISH).startsWith(parsedLine)) {
                candidates.add(new Candidate(command.getName()));
            }
        }
    }

}
