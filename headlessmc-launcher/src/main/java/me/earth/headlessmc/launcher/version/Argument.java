package me.earth.headlessmc.launcher.version;

import lombok.val;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@FunctionalInterface
public interface Argument {
    // NO ON ANDROID WE HAVE TO ESCAPE THE LAST CURLY BRACE!
    @SuppressWarnings("RegExpRedundantEscape")
    Pattern INPUT = Pattern.compile("(\\$\\{[^}]*\\})");

    String getValue();

    default String getType() {
        return "game";
    }

    default Rule getRule() {
        return Rule.ALLOW;
    }

    default Set<String> getInputs() {
        val result = new HashSet<String>();
        val matcher = INPUT.matcher(getValue());
        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

}
