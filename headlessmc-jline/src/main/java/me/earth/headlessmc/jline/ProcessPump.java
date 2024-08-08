package me.earth.headlessmc.jline;

import lombok.RequiredArgsConstructor;
import org.jline.reader.LineReader;

import java.io.*;

@RequiredArgsConstructor
public class ProcessPump {
    private final LineReader lineReader;
    private final Process process;

    public void start() {
        Thread outThread = new Thread(() -> listen(process.getInputStream()));
        outThread.setName("Out Listener");
        outThread.start();

        Thread errThread = new Thread(() -> listen(process.getErrorStream()));
        errThread.setName("Err Listener");
        errThread.start();
    }

    private void listen(InputStream is) {
        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
                lineReader.printAbove(line);
            }
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

}
