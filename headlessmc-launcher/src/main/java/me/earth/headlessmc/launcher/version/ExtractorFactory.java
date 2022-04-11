package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import lombok.Cleanup;
import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.jar.JarFile;

@CustomLog
class ExtractorFactory {
    public Extractor parse(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return Extractor.NO_EXTRACTION;
        }

        val extractJo = element.getAsJsonObject();
        val array = JsonUtil.toArray(extractJo.get("exclude"));
        val ex = new ArrayList<String>(array.size());
        for (JsonElement exclusion : array) {
            ex.add(exclusion.getAsString());
        }

        return (from, fileManager) -> {
            @Cleanup
            val jar = new JarFile(from);
            val enumeration = jar.entries();
            while (enumeration.hasMoreElements()) {
                val je = enumeration.nextElement();
                if (ex.stream().noneMatch(e -> je.getName().startsWith(e))) {
                    log.debug(
                        String.format("Extracting  : %s from %s to %s%s%s",
                                      je.getName(), jar.getName(),
                                      fileManager.getBase(), File.separator,
                                      je.getName()));
                    @Cleanup
                    val is = jar.getInputStream(je);
                    File file = fileManager.create(je.getName());
                    @Cleanup
                    OutputStream os = new FileOutputStream(file);
                    IOUtil.copy(is, os);
                }
            }
        };
    }

}
