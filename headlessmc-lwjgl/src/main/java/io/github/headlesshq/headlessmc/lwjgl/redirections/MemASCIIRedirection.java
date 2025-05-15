package io.github.headlesshq.headlessmc.lwjgl.redirections;

import io.github.headlesshq.headlessmc.lwjgl.api.Redirection;

import java.nio.ByteBuffer;

public enum MemASCIIRedirection implements Redirection {
    INSTANCE;

    public static final String DESC = "Lorg/lwjgl/system/MemoryUtil;memASCII(Ljava/nio/ByteBuffer;I)Ljava/lang/String;";

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args)
        throws Throwable {
        ByteBuffer buffer = (ByteBuffer) args[0];
        int length = (int) args[1];
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) buffer.get());
        }

        return sb.toString();
    }

}
