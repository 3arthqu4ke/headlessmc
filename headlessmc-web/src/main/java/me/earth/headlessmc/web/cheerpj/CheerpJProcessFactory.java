package me.earth.headlessmc.web.cheerpj;

import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OS;

public class CheerpJProcessFactory extends ProcessFactory {
    public CheerpJProcessFactory(FileManager files, HasConfig config, OS os) {
        super(files, config, os);
    }


}
