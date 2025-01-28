package me.earth.headlessmc.java;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JavaVersionParser {
    private static final Pattern PATTERN = Pattern.compile("version \"(\\d+)[.-]?(\\d*)");

    private final boolean addFilePermissions;

    public JavaVersionParser() {
        this(false);
    }

    public int parseVersionCommand(String path) throws IOException {
        if (addFilePermissions) {
            Set<PosixFilePermission> executePermissions = new HashSet<>();
            executePermissions.add(PosixFilePermission.OWNER_READ);
            //executePermissions.add(PosixFilePermission.OWNER_WRITE);
            executePermissions.add(PosixFilePermission.OWNER_EXECUTE);
            executePermissions.add(PosixFilePermission.GROUP_READ);
            //executePermissions.add(PosixFilePermission.GROUP_WRITE);
            executePermissions.add(PosixFilePermission.GROUP_EXECUTE);
            executePermissions.add(PosixFilePermission.OTHERS_READ);
            //executePermissions.add(PosixFilePermission.OTHERS_WRITE);
            executePermissions.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(Paths.get(path), executePermissions);
        }

        Process prcs = new ProcessBuilder().command(path, "-version").start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(prcs.getErrorStream()))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String output = builder.toString();
            return parseVersion(output);
        }
    }

    public int parseVersion(String output) throws IOException {
        Matcher matcher = PATTERN.matcher(output);
        if (!matcher.find()) {
            throw new IOException("Couldn't parse '" + output + "'");
        }
        if (matcher.group(1).equals("1")) {
            return Integer.parseInt(matcher.group(2));
        }
        return Integer.parseInt(matcher.group(1));
    }

    public static String getMajorVersion(String versionString) {
        String[] split = versionString.split("\\.");
        if (split[0].equals("1")) {
            return split[1];
        }

        return split[0];
    }

}
