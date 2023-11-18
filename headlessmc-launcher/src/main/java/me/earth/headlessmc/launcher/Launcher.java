package me.earth.headlessmc.launcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.version.VersionService;

@Getter
@RequiredArgsConstructor
public class Launcher implements HeadlessMc {
    public static final String VERSION = "1.8.0";

    @Delegate
    private final HeadlessMc headlessMc;
    private final VersionService versionService;
    private final FileManager mcFiles;
    private final FileManager fileManager;
    private final ProcessFactory processFactory;
    private final ConfigService configService;
    private final JavaService javaService;
    private final AccountManager accountManager;
    private final AccountValidator validator;

}
