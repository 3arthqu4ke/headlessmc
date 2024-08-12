package me.earth.headlessmc.jline;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;
import org.jetbrains.annotations.Nullable;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CommandCompleter implements Completer {
    private final HeadlessMc hmc;

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        for (Map.Entry<String, @Nullable String> completion : hmc.getCommandLine().getCommandContext().getCompletions(line.line())) {
            candidates.add(new Candidate(completion.getKey(), completion.getKey(), null, completion.getValue(), null, null, true));
        }
    }

}
