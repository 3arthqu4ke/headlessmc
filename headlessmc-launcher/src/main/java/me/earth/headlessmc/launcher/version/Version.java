package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonObject;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.version.family.HasParent;

import java.io.File;
import java.util.List;

// TODO: support logging.xml file?
public interface Version extends HasName, HasId, HasParent<Version> {
    File getFolder();

    JsonObject getJson();

    int getId();

    String getParentName();

    String getAssets();

    String getType();

    String getAssetsUrl();

    int getJava();

    String getMainClass();

    List<Library> getLibraries();

    List<Argument> getArguments();

    boolean isNewArgumentFormat();

    String getClientDownload();

}
