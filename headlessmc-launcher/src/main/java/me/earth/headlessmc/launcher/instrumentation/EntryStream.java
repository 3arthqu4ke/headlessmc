package me.earth.headlessmc.launcher.instrumentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarEntry;

@Getter
@Setter
@RequiredArgsConstructor
public class EntryStream extends InputStream {
    @Delegate
    private final InputStream stream;
    private final List<Target> targets;
    private final JarEntry entry;
    private boolean transformed;
    private boolean skipped;

    public static EntryStream of(byte[] bytes, List<Target> ts, JarEntry je) {
        InputStream is = new ByteArrayInputStream(bytes);
        EntryStream res =  new EntryStream(is, ts, je);
        res.setTransformed(true);
        return res;
    }

}
