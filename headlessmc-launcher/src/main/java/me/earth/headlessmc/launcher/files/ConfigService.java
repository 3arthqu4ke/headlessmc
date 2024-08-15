package me.earth.headlessmc.launcher.files;

import lombok.*;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// TODO: for convenience we could add a 'hmc.' to every property
@CustomLog
@RequiredArgsConstructor
public class ConfigService extends Service<Config> implements HasConfig {
    private static final String ENDING = ".properties";
    private final FileManager fileManager;
    @Getter
    @Setter
    private Config config;

    @Override
    protected List<Config> update() {
        val result = new ArrayList<Config>();
        load(fileManager.create("config.properties"), 0, result);
        val files = fileManager.getDir("configs").listFiles();
        if (files != null) {
            for (int i = 1; i < files.length; i++) {
                val file = files[i - 1];
                if (!file.getName().endsWith(ENDING)) {
                    continue;
                }

                load(file, i, result);
            }
        } else {
            log.warning("Couldn't create the 'configs' directory!");
        }

        if (result.isEmpty()) {
            log.warning("No configs found, adding empty config.");
            result.add(ConfigImpl.empty());
        }

        setConfig(result.get(0));
        log.debug("Found " + result.size() + " config(s), active config: "
                      + getConfig().getName());
        return result;
    }

    private void load(File file, int id, List<Config> configs) {
        log.debug("Loading " + file.getAbsolutePath());
        try {
            configs.add(load(file, id));
        } catch (IOException e) {
            log.warn("Couldn't read '" + file.getAbsolutePath() + "'", e);
        }
    }

    private Config load(File file, int id) throws IOException {
        log.debug("Reading config file: " + file.getAbsolutePath());
        val name = StringUtil.cutOfEnd(file.getName(), ENDING.length());
        @Cleanup
        InputStream is = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(is);
        return new ConfigImpl(properties, name, id);
    }

}
