package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@CustomLog
@RequiredArgsConstructor
public final class VersionService extends Service<Version> {
    private final ParentVersionResolver resolver = new ParentVersionResolver();
    private final FileManager files;

    @Override
    protected Collection<Version> update() {
        val versionFolders = files.getDir("versions").listFiles();
        if (versionFolders == null) {
            log.warning("No Minecraft Version folder found!");
            return Collections.emptyList();
        }

        val fact = new DefaultVersionFactory();
        val versions = new LinkedHashMap<String, Version>();
        val id = new AtomicInteger();
        for (val versionFolder : versionFolders) {
            if (!versionFolder.isDirectory()) {
                continue;
            }

            val files = versionFolder.listFiles();
            if (files != null) {
                for (val versionFile : files) {
                    if (versionFile.getName().endsWith(".json")) {
                        read(versionFile, versionFolder, versions, id, fact);
                    }
                }
            } else {
                log.warning("Couldn't read " + versionFolder.getAbsolutePath());
            }
        }

        resolver.resolveParentVersions(versions);
        return versions.values();
    }

    private void read(File file, File folder, Map<String, Version> versions,
                      AtomicInteger id, VersionFactory factory) {
        try {
            log.debug("Reading " + file.getAbsolutePath());
            JsonElement je = JsonUtil.fromFile(file);
            val version = factory.parse(je.getAsJsonObject(), folder,
                                        id::getAndIncrement);
            if (version.getName() == null) {
                log.warning("Failed to read version " + file.getName() + ", it did not provide a name!");
            } else {
                versions.put(version.getName(), version);
            }
        } catch (IOException | JsonParseException | VersionParseException e) {
            log.warning(file.getName() + ", " + e.getClass() + ": "
                            + e.getMessage());
        }
    }

}
