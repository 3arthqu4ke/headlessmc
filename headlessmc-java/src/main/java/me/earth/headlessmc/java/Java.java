package me.earth.headlessmc.java;

import lombok.Data;

/**
 * Represents a Java executable.
 */
@Data
public class Java implements Comparable<Java> {
    private final String executable;
    private final int version;

    public boolean isCurrent() {
        return executable.equals("current/bin/java");
    }

    @Override
    public int compareTo(Java o) {
        return Integer.compare(this.getVersion(), o.getVersion());
    }

    /**
     * @return the path to the JAVA_HOME of the executable, so without the bin/java extension
     */
    public String getPath() {
        if (executable.endsWith("/bin/java") || executable.endsWith("\\bin\\java")) {
            return executable.replace("\\", "/").substring(0, executable.length() - "/bin/java".length());
        }

        if (executable.endsWith("/bin/java.exe") || executable.endsWith("\\bin\\java.exe")) { // just to be sure
            return executable.replace("\\", "/").substring(0, executable.length() - "/bin/java.exe".length());
        }

        return executable;
    }

}
