package me.earth.headlessmc.api.command;

import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.config.Property;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility for working with {@link Command}s and {@link CommandContext}s.
 */
@UtilityClass
public class CommandUtil {
    /**
     * Splits the given String at whitespaces. Parts containing whitespaces can
     * be escaped by using quotes. Quotes can be escaped like this: \"
     *
     * @param message the message to split.
     * @return the message split into arguments.
     */
    public static String[] split(String message) {
        List<String> result = new ArrayList<>();
        boolean quoted = false;
        boolean escaped = false;
        StringBuilder currentArg = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (escaped) {
                currentArg.append(ch);
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == '"') {
                if (quoted) {
                    result.add(currentArg.toString());
                    currentArg = new StringBuilder();
                }

                quoted = !quoted;
            } else if (ch == ' ') {
                if (quoted) {
                    currentArg.append(ch);
                } else {
                    if (currentArg.length() != 0) {
                        result.add(currentArg.toString());
                        currentArg = new StringBuilder();
                    }
                }
            } else {
                currentArg.append(ch);
            }
        }

        if (currentArg.length() > 0) {
            result.add(currentArg.toString());
        }

        return result.toArray(new String[0]);
    }

    /**
     * Computes the Levenshtein-Distance between two Strings.
     *
     * @param s1 the first String.
     * @param s2 the second String.
     * @return the Levenshtein-Distance between the given Strings.
     */
    public static int levenshtein(CharSequence s1, CharSequence s2) {
        int[] prev = new int[s2.length() + 1];
        for (int j = 0; j < s2.length() + 1; j++) {
            prev[j] = j;
        }

        for (int i = 1; i < s1.length() + 1; i++) {
            int[] curr = new int[s2.length() + 1];
            curr[0] = i;

            for (int j = 1; j < s2.length() + 1; j++) {
                int d1 = prev[j] + 1;
                int d2 = curr[j - 1] + 1;
                int d3 = prev[j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                    d3 += 1;
                }
                curr[j] = Math.min(Math.min(d1, d2), d3);
            }

            prev = curr;
        }

        return prev[s2.length()];
    }

    // TODO: accept with -- and -!
    public static boolean hasFlag(String arg, String... args) {
        return Arrays.stream(args).anyMatch(s -> s.equalsIgnoreCase(arg));
    }

    public static boolean flag(HasConfig ctx, boolean alwaysFlagOn, String flg, Property<Boolean> invertFlag, Property<Boolean> alwaysFlag, String... args) {
        return ctx.getConfig().get(alwaysFlag, alwaysFlagOn) || CommandUtil.hasFlag(flg, args) ^ ctx.getConfig().get(invertFlag, false);
    }

    public static boolean flag(HasConfig ctx, String flg, Property<Boolean> invertFlag, Property<Boolean> alwaysFlag, String... args) {
        return ctx.getConfig().get(alwaysFlag, false) || CommandUtil.hasFlag(flg, args) ^ ctx.getConfig().get(invertFlag, false);
    }

    public static @Nullable String getOption(String option, String... args) {
        for (int i = 0; i < args.length; i++) {
            if (option.equalsIgnoreCase(args[i]) && i < args.length - 1) {
                return args[i + 1];
            }
        }

        return null;
    }

}
