package me.earth.headlessmc.logging;

public class NoThreadFormatter extends ThreadFormatter {
    @Override
    protected String getThread(long threadId) {
        return "["; // CheerpJ does not support JVM_GetAllThreads
    }

}
