package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

// TODO: check if we could make this easier using @SerializedName
@RequiredArgsConstructor
class VersionFactory {
    private final LibraryFactory libraryFactory;
    private final JavaMajorVersionParser javaParser;
    private final ArgumentFactory argumentFactory;

    public Version parse(JsonObject json, File folder, Supplier<Integer> id)
        throws VersionParseException {
        val parentName = JsonUtil.getString(json, "inheritsFrom");
        val assets = JsonUtil.getString(json, "assets");
        val mainClass = JsonUtil.getString(json, "mainClass");
        val name = JsonUtil.getString(json, "id");
        val libraries = libraryFactory.parseLibraries(json.get("libraries"));
        val majorVersion = javaParser.parse(json.get("javaVersion"));
        var argumentElement = json.get("arguments");
        val type = JsonUtil.getString(json, "type");
        if (argumentElement == null) {
            argumentElement = json.get("minecraftArguments");
        }

        val assetsUrl = JsonUtil.getString(json, "assetIndex", "url");
        val clientUrl = JsonUtil.getString(json, "downloads", "client", "url");
        val newFormat = new AtomicBoolean();
        val arguments = argumentFactory.parse(argumentElement, newFormat::set);
        return VersionImpl.builder()
                          .folder(folder)
                          .json(json)
                          .type(type == null ? "unknown" : type)
                          .arguments(arguments)
                          .java(majorVersion)
                          .id(id.get())
                          .assets(assets)
                          .assetsUrl(assetsUrl)
                          .name(name)
                          .parentName(parentName)
                          .mainClass(mainClass)
                          .newArgumentFormat(newFormat.get())
                          .libraries(libraries)
                          .clientDownload(clientUrl)
                          .build();
    }

}
