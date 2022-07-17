package me.earth.headlessmc.api;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HasNameTest {
    @Test
    public void testGetByName() {
        val names = new ArrayList<HasName>();
        for (int i = 0; i < 10; i++) {
            val name = "Test" + i;
            val hasName = new HasName() {
                @Override
                public String getName() {
                    return name;
                }
            };

            assertNull(HasName.getByName(name, names));

            names.add(hasName);

            val getByName = HasName.getByName(name, names);
            assertNotNull(getByName);
            assertEquals(name, getByName.getName());
        }
    }

}
