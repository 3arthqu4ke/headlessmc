package io.github.headlesshq.headlessmc.launcher.instrumentation;

import lombok.AccessLevel;
import lombok.Setter;

public abstract class AbstractTransformer implements Transformer {
    @Setter(AccessLevel.PROTECTED)
    protected boolean run;

    @Override
    public boolean hasRun() {
        return run;
    }

}
