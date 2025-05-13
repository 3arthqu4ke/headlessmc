package me.earth.headlessmc.launcher.mods.files;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PaperModFileReader implements ModFileReader {
    private final String[] ymls;

    public PaperModFileReader(String... ymls) {
        this.ymls = ymls;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ModFile> read(Supplier<Integer> id, Path path) throws IOException {
        try (JarFile jarFile = new JarFile(path.toFile())) {
            for (String yml : ymls) {
                JarEntry entry = jarFile.getJarEntry(yml);
                if (entry == null) {
                    return Collections.emptyList();
                }

                try (InputStream is = jarFile.getInputStream(entry)) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> obj = yaml.load(is);
                    List<String> authors = new ArrayList<>();
                    if (obj.get("author") == null) {
                        authors = (List<String>) obj.get("authors");
                        if (authors == null) {
                            authors = new ArrayList<>();
                        }
                    } else if (obj.get("author") instanceof List) {
                        authors = (List<String>) obj.get("author");
                    } else {
                        authors.add((String) obj.get("author"));
                    }

                    return Collections.singletonList(new ModFile(
                        (String) obj.get("name"),
                            id.get(),
                            (String) obj.get("description"),
                            authors,
                            path
                    ));
                } catch (IOException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        }

        return Collections.emptyList();
    }

    public static PaperModFileReader paper() {
        return new PaperModFileReader("plugin.yml", "paper-plugin.yml");
    }

}
