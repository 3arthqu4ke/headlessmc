package me.earth.headlessmc.launcher.launch;

import lombok.Cleanup;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.os.OS;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;

@CustomLog
@RequiredArgsConstructor
public class XvfbService {
    private final ConfigService configService;
    private final OS os;

    private boolean checked;
    private boolean runningWithXvfb;

    public boolean isRunningWithXvfb() {
        if (!checked) {
            try {
                runningWithXvfb = checkRunningWithXvfb();
            } catch (IOException e) {
                log.error(e);
            } finally {
                checked = true;
            }
        }

        return runningWithXvfb;
    }

    private boolean checkRunningWithXvfb() throws IOException {
        if (os.getType() != OS.Type.LINUX || !configService.getConfig().get(LauncherProperties.CHECK_XVFB, false)) {
            return false;
        }

        Process process = new ProcessBuilder().command("ps", "aux").start();
        @Cleanup
        BufferedReader reader = IOUtil.reader(process.getInputStream());
        String output = IOUtil.read(reader);
        boolean result = output.toLowerCase(Locale.ENGLISH).contains("xvfb");
        log.info(result ? "Running with xvfb" : "Not running with xvfb");
        return result;
    }

}
