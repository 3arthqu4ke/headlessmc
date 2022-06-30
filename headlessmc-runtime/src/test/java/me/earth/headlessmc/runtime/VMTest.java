package me.earth.headlessmc.runtime;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VMTest {
    private static final int SIZE = 128;
    private final VM vm = new VM(SIZE);

    @Test
    public void testCheckSegfault() {
        Assertions.assertEquals(SIZE, vm.size());
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.checkSegfault(-1));
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.checkSegfault(SIZE));
        Assertions.assertDoesNotThrow(() -> vm.checkSegfault(10));
    }

    @Test
    @SneakyThrows
    public void testSetAndGet() {
        Assertions.assertEquals(SIZE, vm.size());
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.set(vm, -1));
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.set(vm, SIZE));
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.get(-1));
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.get(SIZE));

        Assertions.assertDoesNotThrow(() -> vm.set(vm, 10));
        Assertions.assertEquals(vm, vm.get(10));
    }

    @Test
    @SneakyThrows
    public void testPop() {
        Assertions.assertEquals(SIZE, vm.size());
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.pop(-1));
        Assertions.assertThrows(SegmentationFault.class,
                                () -> vm.pop(SIZE));

        Assertions.assertNull(vm.pop(100));
        vm.set(vm, 10);
        Assertions.assertNotNull(vm.get(10));
        Assertions.assertEquals(vm, vm.pop(10));
        Assertions.assertNull(vm.get(10));
    }

}
