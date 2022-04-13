package me.earth.headlessmc.launcher.version;

import lombok.*;
import me.earth.headlessmc.launcher.os.OS;

import java.io.File;
import java.util.Map;

@Data
class LibraryImpl implements Library {
    private final Map<String, String> natives;
    private final Extractor extractor;
    private final String name;
    private final Rule rule;
    private final String baseUrl;
    private final String url;
    private final String path;
    private final boolean nativeLibrary;

    @Override
    public String getPath(OS os) {
        if (path != null) {
            return path.replace("${arch}", os.isArch() ? "64" : "32");
        }

        val split = name.split(":");
        val sb = new StringBuilder()
            .append(split[0].replace(".", File.separator))
            .append(File.separator)
            .append(split[1])
            .append(File.separator)
            .append(split[2])
            .append(File.separator)
            .append(split[1])
            .append("-")
            .append(split[2]);

        var n = natives.get(os.getType().getName());
        if (n != null && nativeLibrary) {
            sb.append("-")
              .append(n.replace("${arch}", os.isArch() ? "64" : "32"));
        }

        return sb.append(".jar").toString();
    }

    @Override
    public String getUrl(String path) {
        if (url != null) {
            return url;
        }

        return baseUrl + path;
    }

}
