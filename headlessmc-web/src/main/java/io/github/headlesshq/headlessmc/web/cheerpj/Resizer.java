package io.github.headlesshq.headlessmc.web.cheerpj;

import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

@Getter
@Setter
public class Resizer {
    public static final String EXPECTED_INDEX_HTML_VERSION = "1.0.0";

    @Getter
    private static final Resizer instance = new Resizer();

    private volatile BiConsumer<Integer, Integer> updateListener = (width, height) -> {};
    private volatile String version = null;
    private volatile int width = 800;
    private volatile int height = 600;

    @SuppressWarnings("unused") // called by CheerpJ
    public synchronized void setSize(int widthIn, int heightIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.updateListener.accept(widthIn, heightIn);
    }

}
