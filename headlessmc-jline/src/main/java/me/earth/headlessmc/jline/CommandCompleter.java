package me.earth.headlessmc.jline;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Completion;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

@RequiredArgsConstructor
public class CommandCompleter implements Completer {
    private final HeadlessMc hmc;

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        for (Completion completion : hmc.getCommandLine().getCommandContext().getCompletions(line.line())) {
            candidates.add(new Candidate(completion.getValue(), completion.getValue(), null, completion.getDescription(), null, null, true, 0));
        }
    }

}
