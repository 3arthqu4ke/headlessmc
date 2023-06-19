package me.earth.headlessmc.lwjgl.redirections;

import me.earth.headlessmc.lwjgl.api.RedirectionManager;

/**
 * org.lwjgl.system.CustomBuffer returns a generic SELF type everywhere.
 */
public class CustomBufferRedirection {
    private static final String[] DESCRIPTORS = new String[] {
        "self()", "position(I)", "limit(I)", "mark()", "reset()",
        "flip()", "rewind()", "slice()", "slice(II)", "duplicate()",
        "put(Lorg/lwjgl/system/CustomBuffer;)", "compact()"
    };

    public static void redirect(RedirectionManager manager) {
        for (String descriptor : DESCRIPTORS) {
            manager.redirect("Lorg/lwjgl/system/CustomBuffer;" + descriptor +
                                 "Lorg/lwjgl/system/CustomBuffer;",
                             (obj, desc, type, args) -> obj
            );
        }
    }

}
