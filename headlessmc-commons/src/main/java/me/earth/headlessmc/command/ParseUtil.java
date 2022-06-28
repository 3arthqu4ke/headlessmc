package me.earth.headlessmc.command;

import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.command.CommandException;

@UtilityClass
public class ParseUtil {
    public static byte parseB(String number) throws CommandException {
        return (byte) parseL(number, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public static short parseS(String number) throws CommandException {
        return (short) parseL(number, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public static int parseI(String number) throws CommandException {
        return (int) parseL(number, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static long parseL(String number) throws CommandException {
        return parseL(number, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static long parseL(String number, long min, long max)
        throws CommandException {
        if (number == null) {
            throw new CommandException("Couldn't parse null!");
        }

        try {
            long result = Long.parseLong(number);
            if (result < min || result > max) {
                throw new CommandException("That number is too big/small!");
            }

            return result;
        } catch (NumberFormatException numberFormatException) {
            throw new CommandException("Couldn't parse '" + number + "'!");
        }
    }

    public static float parseF(String number) throws CommandException {
        return (float) parseD(number, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public static double parseD(String number) throws CommandException {
        return parseD(number, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static double parseD(String number, double min, double max)
        throws CommandException {
        if (number == null) {
            throw new CommandException("Couldn't parse null!");
        }

        try {
            double result = Double.parseDouble(number);
            if (result < min || result > max) {
                throw new CommandException("That number is too big/small!");
            }

            return result;
        } catch (NumberFormatException numberFormatException) {
            throw new CommandException("Couldn't parse '" + number + "'!");
        }
    }

}
