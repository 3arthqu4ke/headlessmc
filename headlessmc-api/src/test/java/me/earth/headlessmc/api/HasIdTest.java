package me.earth.headlessmc.api;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HasIdTest {
    @Test
    public void testGetById() {
        val names = new ArrayList<HasId>();
        for (int id = 0; id < 10; id++) {
            val hasName = Mockito.mock(HasId.class);
            Mockito.when(hasName.getId()).thenReturn(id);

            assertNull(HasId.getById(id, names));
            assertNull(HasId.getById(String.valueOf(id), names));

            names.add(hasName);

            val getById = HasId.getById(id, names);
            assertNotNull(getById);
            assertEquals(id, getById.getId());

            val getById2 = HasId.getById(String.valueOf(id), names);
            assertEquals(getById2, getById);
        }
    }

}
