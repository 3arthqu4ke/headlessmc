package me.earth.headlessmc.runtime.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.LogsMessages;
import me.earth.headlessmc.api.util.ReflectionUtil;
import me.earth.headlessmc.api.util.Table;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClassHelper {
    private final Class<?> clazz;
    private final Set<Constructor<?>> constructors;
    private final Set<Method> methods;
    private final Set<Field> fields;

    public static Table<Field> getFieldTable(Iterable<Field> fields,
                                             boolean verbose) {
        return new Table<Field>()
            .addAll(fields)
            .withColumn("name", Field::getName)
            .withColumn("type", f -> getName(f.getType(), verbose))
            .withColumn("access", ClassHelper::getAccess);
    }

    public static Table<Constructor<?>> getConstructorTable(
        Iterable<Constructor<?>> constructors, boolean verbose) {
        return new Table<Constructor<?>>()
            .addAll(constructors)
            .withColumn("access", ClassHelper::getAccess)
            .withColumn("args", c -> getArgs(verbose, c.getParameterTypes()));
    }

    public static Table<Method> getMethodTable(Iterable<Method> methods,
                                               boolean verbose) {
        return new Table<Method>()
            .addAll(methods)
            .withColumn("name", Method::getName)
            .withColumn("type", m -> m.getReturnType().getName())
            .withColumn("args", m -> getArgs(verbose, m.getParameterTypes()))
            .withColumn("access", ClassHelper::getAccess);
    }

    public static String getAccess(Member member) {
        val sb = new StringBuilder();
        if (Modifier.isPrivate(member.getModifiers())) {
            sb.append("private");
        } else if (Modifier.isPublic(member.getModifiers())) {
            sb.append("public");
        } else if (Modifier.isProtected(member.getModifiers())) {
            sb.append("protected");
        } else {
            sb.append("package");
        }

        if (Modifier.isAbstract(member.getModifiers())) {
            sb.append(" abstract");
        } else if (Modifier.isStatic(member.getModifiers())) {
            sb.append(" static");
        } else if (Modifier.isFinal(member.getModifiers())) {
            sb.append(" final");
        }

        return sb.toString();
    }

    public static String getArgs(boolean verbose, Class<?>... types) {
        return Arrays.stream(types)
                     .map(c -> getName(c, verbose))
                     .collect(Collectors.joining(","));
    }

    public static String getName(Class<?> type, boolean verbose) {
        return verbose ? type.getName() : type.getSimpleName();
    }

    public static ClassHelper of(Class<?> clazz) {
        val constructors = clazz.getConstructors();
        val methods = new LinkedHashSet<Method>();
        ReflectionUtil.iterate(clazz, c -> {
            methods.addAll(Arrays.asList(c.getDeclaredMethods()));
        });

        val fields = new LinkedHashSet<Field>();
        ReflectionUtil.iterate(clazz, c -> {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        });

        return new ClassHelper(
            clazz, new HashSet<>(Arrays.asList(constructors)), methods, fields);
    }

    public void dump(LogsMessages out, boolean verbose) {
        out.log("-----------------------------------");
        Class<?> superClass = clazz.getSuperclass();
        out.log(clazz.getName() + " : "
                    + (superClass == null ? "null" : superClass.getName())
                    + ", " + getArgs(verbose, clazz.getInterfaces()));
        out.log("--------------Fields---------------");
        out.log(getFieldTable(verbose).build());
        out.log("-----------Constructors------------");
        out.log(getConstructorTable(verbose).build());
        out.log("--------------Methods--------------");
        out.log(getMethodTable(verbose).build());
        out.log("-----------------------------------");
    }

    public Table<Field> getFieldTable(boolean verbose) {
        return getFieldTable(fields, verbose);
    }

    public Table<Constructor<?>> getConstructorTable(boolean verbose) {
        return getConstructorTable(constructors, verbose);
    }

    public Table<Method> getMethodTable(boolean verbose) {
        return getMethodTable(methods, verbose);
    }

}
