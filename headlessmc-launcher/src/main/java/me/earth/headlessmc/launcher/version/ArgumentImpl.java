package me.earth.headlessmc.launcher.version;

import lombok.Data;

@Data
class ArgumentImpl implements Argument {
    private final String value;
    private final String type;
    private final Rule rule;

}
