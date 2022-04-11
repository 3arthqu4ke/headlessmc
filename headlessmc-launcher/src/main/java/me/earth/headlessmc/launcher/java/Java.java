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

}
