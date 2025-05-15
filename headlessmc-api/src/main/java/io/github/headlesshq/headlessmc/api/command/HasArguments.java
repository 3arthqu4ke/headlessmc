package io.github.headlesshq.headlessmc.api.command;

import java.util.Map;

public interface HasArguments {
    Iterable<String> getArgs();

    String getArgDescription(String arg);

    Iterable<Map.Entry<String, String>> getArgs2Descriptions();

}
