package me.earth.headlessmc.runtime.mapping;

public enum NoMapping implements Mapping {
    INSTANCE;

    @Override
    public String getClassName(String className) {
        return className;
    }

    @Override
    public String getMethodName(Class<?> owner, String name) {
        return name;
    }

    @Override
    public String getFieldName(Class<?> owner, String name) {
        return name;
    }

}
