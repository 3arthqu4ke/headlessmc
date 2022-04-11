package me.earth.headlessmc.api.mc.scheduling;

import lombok.val;
import me.earth.headlessmc.api.HasName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HasNameTest {
    @Test
    public void testGetByName() {
        val names = new ArrayList<HasName>();
        for (int i = 0; i < 10; i++) {
            val hasName = Mockito.mock(HasName.class);
            val name = "Test" + i;
            Mockito.when(hasName.getName()).thenReturn(name);

            assertNull(HasName.getByName(name, names));

            names.add(hasName);

            val getByName = HasName.getByName(name, names);
            assertNotNull(getByName);
            assertEquals(name, getByName.getName());
        }
    }

}
