package io.github.headlesshq.headlessmc.runtime.reflection;

public class VM {
    private final Object[] memory;

    public VM(int size) {
        this.memory = new Object[size];
    }

    public int size() {
        return memory.length;
    }

    public void set(Object object, int address) throws SegmentationFault {
        checkSegfault(address);
        memory[address] = object;
    }

    public Object get(int address) throws SegmentationFault {
        checkSegfault(address);
        return memory[address];
    }

    public Object pop(int address) throws SegmentationFault {
        Object obj = get(address);
        memory[address] = null;
        return obj;
    }

    public void checkSegfault(int address) throws SegmentationFault {
        if (address < 0 || address >= memory.length) {
            throw new SegmentationFault(address + " not in 0-" + memory.length);
        }
    }

}
