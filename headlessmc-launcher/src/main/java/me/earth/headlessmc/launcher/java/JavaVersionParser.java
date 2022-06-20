package me.earth.headlessmc.launcher.java;

import lombok.Cleanup;
import me.earth.headlessmc.launcher.util.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JavaVersionParser {
    private static final Pattern PATTERN = Pattern.compile(
        "version \"([0-9]+\\.[0-9]+\\.[0-9_]+(?:\\.[0-9]+)*)\"");

    public int parseVersionCommand(String path) throws IOException {
        Process prcs = new ProcessBuilder().command(path, "-version").start();

        @Cleanup
        BufferedReader reader = IOUtil.reader(prcs.getErrorStream());
        String output = IOUtil.read(reader);
        return parseVersion(output);
    }

    public int parseVersion(String output) throws IOException {
        Matcher matcher = PATTERN.matcher(output);
        if (!matcher.find()) {
            throw new IOException("Couldn't parse '" + output + "'");
        }

        return Integer.parseInt(getMajorVersion(matcher.group(1)));
    }

    private String getMajorVersion(String versionString) {
        String[] split = versionString.split("\\.");
        if (split[0].equals("1")) {
            return split[1];
        }

        return split[0];
    }

}
