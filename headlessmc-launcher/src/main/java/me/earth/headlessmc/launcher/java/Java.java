package me.earth.headlessmc.launcher.java;

import lombok.Data;
import lombok.var;

@Data
public class Java implements Comparable<Java> {
    private final String executable;
    private final int version;

    @Override
    public int compareTo(Java o) {
        return Integer.compare(this.getVersion(), o.getVersion());
    }

    public static Java current() {
        return new Java("java", parseSystemProperty(System.getProperty("java.version")));
    }

    private static int parseSystemProperty(String versionIn) {
        var version = versionIn;
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }

        return Integer.parseInt(version);
    }

}
