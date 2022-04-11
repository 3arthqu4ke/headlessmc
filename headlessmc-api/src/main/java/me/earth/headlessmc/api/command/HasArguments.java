package me.earth.headlessmc.api.command;

// TODO: actually use this HelpCommand etc!!!!!
public interface HasArguments {
    Iterable<String> getArgs();

    String getArgDescription(String arg);

}
