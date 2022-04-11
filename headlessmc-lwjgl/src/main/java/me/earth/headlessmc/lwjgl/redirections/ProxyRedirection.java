package me.earth.headlessmc.lwjgl.redirections;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.lwjgl.api.Redirection;
import me.earth.headlessmc.lwjgl.api.RedirectionManager;
import me.earth.headlessmc.lwjgl.util.DescriptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ProxyRedirection implements InvocationHandler {
    private final RedirectionManager manager;
    private final String internalName;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
        // TODO: do we really want to add the proxy: prefix?
        String desc = "proxy:" + internalName + DescriptionUtil.getDesc(method);
        Supplier<Redirection> fb = () -> manager;
        if (desc.endsWith(";equals(Ljava/lang/Object;)Z")) {
            fb = () -> DefaultRedirections.EQUALS;
        } else if (desc.endsWith(";hashCode()I")) {
            fb = () -> DefaultRedirections.HASHCODE;
        }

        return manager.invoke(desc, method.getReturnType(), proxy, fb, args);
    }

}
