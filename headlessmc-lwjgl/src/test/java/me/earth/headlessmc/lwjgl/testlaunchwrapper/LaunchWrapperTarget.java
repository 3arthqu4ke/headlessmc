package me.earth.headlessmc.lwjgl.testlaunchwrapper;

import lombok.val;
import me.earth.headlessmc.lwjgl.LwjglInstrumentationTest;
import me.earth.headlessmc.lwjgl.launchwrapper.LaunchWrapperLwjglTransformer;
import me.earth.headlessmc.lwjgl.launchwrapper.LaunchWrapperTest;
import org.lwjgl.AbstractLwjglClass;
import org.lwjgl.Lwjgl;
import org.lwjgl.LwjglInterface;
import net.minecraft.launchwrapper.Launch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Called by the {@link LaunchWrapperTest}
 */
public class LaunchWrapperTarget {
    public static void main(String[] args) {
        assertNotEquals(Launch.classLoader,
                        LaunchWrapperLwjglTransformer.class.getClassLoader());

        assertEquals(Launch.classLoader,
                     Lwjgl.class.getClassLoader());
        assertEquals(Launch.classLoader,
                     AbstractLwjglClass.class.getClassLoader());
        assertEquals(Launch.classLoader,
                     LwjglInterface.class.getClassLoader());

        val lwjgl = Lwjgl.factoryMethod("test");
        assertNotNull(lwjgl);
        LwjglInstrumentationTest.testRedirections(lwjgl, lwjgl.getClass());

        //noinspection ConstantConditions
        assertNull(AbstractLwjglClass.returnsAbstractByteBuffer("test"));
        val aLwjgl = AbstractLwjglClass.factoryMethod("test");
        assertNotNull(aLwjgl);
        LwjglInstrumentationTest.testRedirections(aLwjgl, aLwjgl.getClass());

        val iLwjgl = LwjglInterface.factoryMethod("test");
        assertNotNull(iLwjgl);
        LwjglInstrumentationTest.testRedirections(iLwjgl, LwjglInterface.class);

        System.setProperty(LaunchWrapperTest.PASSED, "true");
    }

}
