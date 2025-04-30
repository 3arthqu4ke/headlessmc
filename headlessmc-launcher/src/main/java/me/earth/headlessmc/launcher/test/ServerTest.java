package me.earth.headlessmc.launcher.test;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@CustomLog
@RequiredArgsConstructor
public class ServerTest {
    private final AtomicBoolean successful = new AtomicBoolean(false);
    private final Process process;

    public Thread start() {
        log.info("Starting Server Test");
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                    if (line.endsWith("For help, type \"help\"")) {
                        successful.set(true);
                        stop();
                        return;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        thread.setDaemon(true);
        thread.setName("Server Test");
        thread.start();
        return thread;
    }

    public boolean wasSuccessful() {
        return successful.get();
    }

    public void stop() throws IOException {
        log.info("Stopping Server!");
        process.getOutputStream().write(("stop" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        process.getOutputStream().flush();
    }

    public void awaitExitOrKill() throws InterruptedException {
        if (!process.waitFor(2, TimeUnit.MINUTES)) {
            process.destroyForcibly();
        }
    }

}
