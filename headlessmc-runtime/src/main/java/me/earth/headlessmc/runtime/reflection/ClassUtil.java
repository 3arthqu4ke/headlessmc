package me.earth.headlessmc.runtime.reflection;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class ClassUtil {
    public static Class<?> getPrimitiveClass(String name)
        throws ClassNotFoundException {
        switch (name.toLowerCase(Locale.ENGLISH)) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
            case "void":
                return void.class;
            default:
                throw new ClassNotFoundException(
                    "Couldn't find primitive class " + name);
        }
    }

}
