package io.github.headlesshq.headlessmc.api.process;

import lombok.Getter;
import lombok.Setter;
import io.github.headlesshq.headlessmc.api.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.function.Supplier;

@Getter
@Setter
public class InAndOutProvider {
    private volatile Supplier<PrintStream> out = new Lazy<>(() -> new PrintStream(new FileOutputStream(FileDescriptor.out), true), null);
    private volatile Supplier<PrintStream> err = new Lazy<>(() -> new PrintStream(new FileOutputStream(FileDescriptor.err), true), null);
    private volatile Supplier<InputStream> in = new Lazy<>(() -> new FileInputStream(FileDescriptor.in), null);
    private volatile Supplier<@Nullable Console> console = System::console;

}
