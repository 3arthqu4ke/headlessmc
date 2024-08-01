package me.earth.headlessmc.launcher.java;

import lombok.Data;

@Data
public class Java implements Comparable<Java> {
    private final String executable;
    private final int version;

    @Override
    public int compareTo(Java o) {
        return Integer.compare(this.getVersion(), o.getVersion());
    }

    public String getPath() {
        if (executable.endsWith("/bin/java") || executable.endsWith("\\bin\\java")) {
            return executable.replace("\\", "/").substring(0, executable.length() - "/bin/java".length());
        }

        return executable;
    }

}
