package me.earth.headlessmc.wrapper.plugin;

import org.jetbrains.annotations.NotNull;

public interface TransformerPlugin extends Comparable<TransformerPlugin> {
    Transformer getTransformer();

    String getName();

    int getPriority();

    @Override
    default int compareTo(@NotNull TransformerPlugin o) {
        int result = Integer.compare(this.getPriority(), o.getPriority());
        return result == 0 ? this.getName().compareTo(o.getName()) : result;
    }

}
