package me.earth.headlessmc.api.process;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.util.Lazy;

import java.io.*;
import java.util.function.Supplier;

@Getter
@Setter
public class InAndOutProvider {
    private Supplier<PrintStream> out = new Lazy<>(() -> new PrintStream(new FileOutputStream(FileDescriptor.out)), null);
    private Supplier<PrintStream> err = new Lazy<>(() -> new PrintStream(new FileOutputStream(FileDescriptor.err)), null);
    private Supplier<InputStream> in = new Lazy<>(() -> new FileInputStream(FileDescriptor.in), null);

}
