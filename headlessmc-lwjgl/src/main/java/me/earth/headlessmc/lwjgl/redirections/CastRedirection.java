package me.earth.headlessmc.lwjgl.redirections;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.lwjgl.api.Redirection;
import me.earth.headlessmc.lwjgl.api.RedirectionManager;

@RequiredArgsConstructor
public class CastRedirection implements Redirection {
    private final RedirectionManager manager;

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args)
        throws Throwable {
        if (obj == null || type.isInstance(obj)) {
            return obj;
        }

        return manager.invoke(obj, "<init> " + desc, type, args);
    }

}
