package me.earth.headlessmc.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.command.HasCommandContext;

import java.io.InputStream;
import java.util.Scanner;

@RequiredArgsConstructor
public class CommandLineListener {
    private final InputStream inputStream;

    public void listen(HasCommandContext context) {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                context.getCommandContext().execute(scanner.nextLine());
            }
        }
    }

    public void listenAsync(HasCommandContext context) {
        Thread thread = new Thread(() -> listen(context));
        thread.setName("HeadlessMc-CommandLine");
        thread.setDaemon(true);
        thread.start();
    }

}
