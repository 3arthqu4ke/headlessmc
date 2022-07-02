package me.earth.headlessmc.lwjgl.api;

@FunctionalInterface
public interface Redirection {
    String CAST_PREFIX = "<cast> ";
    String METHOD_NAME = "invoke";
    String METHOD_DESC = "(Ljava/lang/Object;Ljava/lang/String;" +
        "Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;";

    static Redirection of(Object value) {
        return (obj, desc, type, args) -> value;
    }

    Object invoke(Object obj, String desc, Class<?> type, Object... args)
        throws Throwable;

}
