package me.earth.headlessmc.runtime.mapping;

public interface Mapping {
    String getClassName(String className);

    String getMethodName(Class<?> owner, String name);

    String getFieldName(Class<?> owner, String name);

}
