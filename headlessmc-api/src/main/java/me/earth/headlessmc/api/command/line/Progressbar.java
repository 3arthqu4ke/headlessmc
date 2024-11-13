package me.earth.headlessmc.api.command.line;

import lombok.Data;

public interface Progressbar extends AutoCloseable {
    void stepBy(long n);

    void stepTo(long n);

    void step();

    void maxHint(long n);

    boolean isDummy();

    @Override
    void close();

    static Progressbar dummy() {
        return new Progressbar() {
            @Override
            public void close() {

            }

            @Override
            public void stepBy(long n) {

            }

            @Override
            public void stepTo(long n) {

            }

            @Override
            public void step() {

            }

            @Override
            public void maxHint(long n) {

            }

            @Override
            public boolean isDummy() {
                return true;
            }
        };
    }

    @Data
    class Configuration {
        private final String taskName;
        private final long initialMax;
    }

}
