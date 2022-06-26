package me.earth.headlessmc.lwjgl;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.lwjgl.api.RedirectionApi;
import me.earth.headlessmc.lwjgl.api.RedirectionManager;
import me.earth.headlessmc.lwjgl.lwjgltestclasses.AbstractLwjglClass;
import me.earth.headlessmc.lwjgl.lwjgltestclasses.Lwjgl;
import me.earth.headlessmc.lwjgl.lwjgltestclasses.LwjglClassLoader;
import me.earth.headlessmc.lwjgl.lwjgltestclasses.LwjglInterface;
import me.earth.headlessmc.lwjgl.redirections.DefaultRedirections;
import me.earth.headlessmc.lwjgl.redirections.ObjectRedirection;
import me.earth.headlessmc.lwjgl.transformer.LwjglTransformer;
import me.earth.headlessmc.lwjgl.util.DescriptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LwjglInstrumentationTest {
    private static final RedirectionManager MANAGER =
        RedirectionApi.getRedirectionManager();

    @Test
    @SneakyThrows
    public void testLwjglInterface() {
        Assertions.assertNull(LwjglInterface.factoryMethod("test"));
        var aMethod = LwjglInterface.class.getMethod("abstractMethod");
        Assertions.assertTrue(Modifier.isAbstract(aMethod.getModifiers()));

        val lwjglInterface = load(LwjglInterface.class);

        aMethod = lwjglInterface.getMethod("abstractMethod");
        Assertions.assertTrue(
            Modifier.isAbstract(aMethod.getModifiers()),
            "Abstract method in an interface should still be abstract");

        val obj = callFactoryMethod(lwjglInterface);
        Assertions.assertNotNull(obj);
        testRedirections(obj, lwjglInterface);
    }

    @Test
    @SuppressWarnings({"ResultOfMethodCallIgnored", "Convert2MethodRef"})
    public void testLwjglClass() {
        Assertions.assertNull(Lwjgl.factoryMethod("test"));
        assertThrows(NoSuchMethodException.class, Lwjgl.class::getConstructor);
        val lwjglClass = load(Lwjgl.class);
        assertDoesNotThrow(() -> lwjglClass.getConstructor(),
                           "A no-args constructor should've been added!");
        val obj = callFactoryMethod(lwjglClass);
        Assertions.assertNotNull(obj);
        testRedirections(obj, lwjglClass);
    }

    @Test
    @SneakyThrows
    public void testArrayReturnValue() {
        val lwjgl = new Lwjgl("test");
        Assertions.assertNull(lwjgl.returnsByteArray("test"));
        Assertions.assertNull(lwjgl.returns2dIntArray("test"));

        val lwjglClass = load(Lwjgl.class);
        val obj = callFactoryMethod(lwjglClass);
        Assertions.assertNotNull(obj);
        var method = lwjglClass.getMethod("returnsByteArray", String.class);
        method.setAccessible(true);
        var result = method.invoke(obj, "test");
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(byte[].class, result);
        Assertions.assertArrayEquals(new byte[0], (byte[]) result);

        method = lwjglClass.getMethod("returns2dIntArray", String.class);
        method.setAccessible(true);
        result = method.invoke(obj, "test");
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(int[][].class, result);
        Assertions.assertArrayEquals(new int[][]{}, (int[][]) result);
    }

    @Test
    @SneakyThrows
    public void testCastRedirection() {
        val lwjglClass = load(Lwjgl.class);
        val ctr = lwjglClass.getConstructor(Object.class);
        ctr.setAccessible(true);

        // MANAGER.redirect("",  (object, desc, type, args) -> byteBuffer);
        val argument = new Object();
        var lwjglInstance = ctr.newInstance(argument); // not a String!!!
        val field = lwjglClass.getField("string");
        field.setAccessible(true);
        var value = field.get(lwjglInstance);
        Assertions.assertEquals("", value); // DefaultRedirection for String

        val called = new boolean[]{false};
        val expected = "test";
        MANAGER.redirect("<init> <cast> java/lang/String",
                         (object, desc, type, args) -> {
                             called[0] = true;
                             Assertions.assertEquals(argument, object);
                             return expected;
                         });

        lwjglInstance = ctr.newInstance(argument);
        Assertions.assertTrue(called[0]);
        called[0] = false;
        value = field.get(lwjglInstance);
        Assertions.assertEquals(expected, value);

        val nextExpected = "test2";
        val called2 = new boolean[]{false};
        MANAGER.redirect("<cast> java/lang/String",
                         (object, desc, type, args) -> {
                             called2[0] = true;
                             Assertions.assertEquals(argument, object);
                             return nextExpected;
                         });

        lwjglInstance = ctr.newInstance(argument);
        Assertions.assertTrue(called2[0]);
        value = field.get(lwjglInstance);
        Assertions.assertEquals(nextExpected, value);

        val called3 = new boolean[]{false};
        MANAGER.redirect(DescriptionUtil.getDesc(lwjglClass)
                                        .concat("<init>(Ljava/lang/Object;)V"),
                         (object, desc, type, args) -> {
                             called3[0] = true;
                             Assertions.assertEquals(1, args.length);
                             Assertions.assertEquals(args[0], argument);
                             return null;
                         });

        ctr.newInstance(argument);
        Assertions.assertTrue(called3[0]);
    }

    @Test
    @SneakyThrows
    public void testLwjglAbstractClass() {
        Assertions.assertNull(AbstractLwjglClass.factoryMethod("test"));
        Assertions.assertTrue(Modifier.isAbstract(
            AbstractLwjglClass.class.getModifiers()));
        var aMethod = AbstractLwjglClass.class.getMethod("abstractMethod");
        Assertions.assertTrue(Modifier.isAbstract(aMethod.getModifiers()));

        val abstractClass = load(AbstractLwjglClass.class);

        Assertions.assertFalse(
            Modifier.isAbstract(abstractClass.getModifiers()));
        aMethod = abstractClass.getMethod("abstractMethod");
        Assertions.assertFalse(
            Modifier.isAbstract(aMethod.getModifiers()),
            "Abstract method in class should get implemented");

        val obj = callFactoryMethod(abstractClass);
        Assertions.assertNotNull(obj);
        testRedirections(obj, abstractClass);
    }

    @Test
    @SneakyThrows
    public void testAbstractReturnValue() {
        val abstractClass = load(AbstractLwjglClass.class);
        val obj = callFactoryMethod(abstractClass);

        val returnsAbstractByteBuffer = abstractClass.getMethod(
            "returnsAbstractByteBuffer", String.class);
        returnsAbstractByteBuffer.setAccessible(true);
        var result = returnsAbstractByteBuffer.invoke(obj, "test");
        Assertions.assertNull(result);

        val descriptor = DescriptionUtil.getDesc(abstractClass)
            + DescriptionUtil.getDesc(returnsAbstractByteBuffer);
        val byteBuffer = ByteBuffer.wrap(new byte[0]);
        MANAGER.redirect(descriptor, (object, desc, type, args) -> byteBuffer);
        result = returnsAbstractByteBuffer.invoke(obj, "test");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(byteBuffer, result);
    }

    @SneakyThrows
    private void testRedirections(Object object, Class<?> clazz) {
        val called = new boolean[]{false};
        for (val method : clazz.getDeclaredMethods()) {
            if (method.getParameterTypes().length > 0) {
                continue;
            }

            val descriptor = DescriptionUtil.getDesc(clazz)
                + DescriptionUtil.getDesc(method);
            MANAGER.redirect(descriptor, (obj, desc, type, args) -> {
                called[0] = true;
                if (type.isPrimitive()) {
                    val fb = DefaultRedirections.fallback(
                        type, new ObjectRedirection(MANAGER));
                    Assertions.assertNotNull(fb);
                    return fb.invoke(obj, desc, type, args);
                }

                return null;
            });

            called[0] = false;
            val result = method.invoke(object);
            if (!method.getReturnType().isPrimitive()) {
                Assertions.assertNull(result);
            }

            Assertions.assertTrue(called[0]);
        }
    }

    @SneakyThrows
    private <T> T callFactoryMethod(Class<T> clazz) {
        val method = clazz.getDeclaredMethod("factoryMethod", String.class);
        method.setAccessible(true);
        return clazz.cast(method.invoke(null, "dummy"));
    }

    @SneakyThrows
    private Class<?> load(Class<?> toLoad) {
        val transformer = new LwjglTransformer();
        val lwjglClassLoader = new LwjglClassLoader(transformer);
        return lwjglClassLoader.loadClass(toLoad.getName());
    }

}
