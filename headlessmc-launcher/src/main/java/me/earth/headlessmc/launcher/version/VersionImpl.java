package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

@Data
@Builder
public class VersionImpl implements Version {
    private final File folder;
    private final JsonObject json;
    private final int id;
    private final String name;
    private final String parentName;
    private final String assets;
    private final String type;
    private final String assetsUrl;
    private final Integer java;
    private final String mainClass;
    private final List<Library> libraries;
    private final List<Argument> arguments;
    private final String clientDownload;
    private final @Nullable String clientSha1;
    private final @Nullable Long clientSize;
    private final boolean newArgumentFormat;
    private final @Nullable Logging logging;
    private Version parent;

}
