package io.github.headlesshq.headlessmc.api.classloading;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.HeadlessMcApi;
import io.github.headlesshq.headlessmc.api.HeadlessMcImpl;
import io.github.headlesshq.headlessmc.api.command.Command;
import io.github.headlesshq.headlessmc.api.command.CommandContext;
import io.github.headlesshq.headlessmc.api.command.CommandContextImpl;
import io.github.headlesshq.headlessmc.api.command.line.CommandLine;
import io.github.headlesshq.headlessmc.api.config.ConfigImpl;
import io.github.headlesshq.headlessmc.api.exit.ExitManager;
import io.github.headlesshq.headlessmc.logging.LoggingService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApiClassloadingHelperTest {
    private static String execute;
    private static String[] commandArgs;

    @Test
    public void testApiClassloadingHelper() throws Exception {
        try (IsolatedClassLoader isolatedClassLoader = new IsolatedClassLoader()) {
            HeadlessMc headlessMc = headlessMc();
            HeadlessMcApi.setInstance(headlessMc);
            headlessMc.getCommandLine().setListening(true);
            headlessMc.getCommandLine().setCommandContext(new CommandContextImpl(headlessMc));
            assertFalse(headlessMc.getCommandLine().getCommandContext() instanceof ClAgnosticCommandContext);
            Class<?> classLoadingHelperTest = isolatedClassLoader.findClass(ApiClassloadingHelperTest.class.getName());
            assertNotEquals(ApiClassloadingHelperTest.class, classLoadingHelperTest);
            assertInstanceOf(IsolatedClassLoader.class, classLoadingHelperTest.getClassLoader());
            Method runTest = classLoadingHelperTest.getMethod("runTest", boolean.class);
            runTest.invoke(null, true);

            CommandContext cc = headlessMc.getCommandLine().getCommandContext();
            assertInstanceOf(ClAgnosticCommandContext.class, cc);

            cc.execute("test");
            assertNull(execute);

            List<Map. Entry<String, String>> completions = cc.getCompletions("t");
            assertEquals(1, completions.size());
            assertEquals("testname", completions.get(0).getKey());
            assertEquals("testdesc", completions.get(0).getValue());

            Command command = cc.iterator().next();
            assertTrue(command.matches("test", "test"));
            assertFalse(command.matches("notTest", "notTest"));
            assertEquals("testname", command.getName());
            assertEquals("testarg", command.getArgs().iterator().next());
            assertEquals("0", command.getArgDescription("dummy"));
            assertEquals("testargdesc", command.getArgDescription("testarg"));
            assertInstanceOf(ArrayList.class, command.getArgs2Descriptions());
            assertEquals("testdesc", command.getDescription());
            command.execute("arg1", "arg1");
            assertNull(commandArgs);

            Method checkAssertions = classLoadingHelperTest.getMethod("checkAssertions");
            checkAssertions.invoke(null);
        } finally {
            HeadlessMcApi.setInstance(null);
        }
    }

    @Test
    public void testWhenNotListening() throws Exception {
        try (IsolatedClassLoader isolatedClassLoader = new IsolatedClassLoader()) {
            HeadlessMc headlessMc = headlessMc();
            HeadlessMcApi.setInstance(headlessMc);
            // headlessMc.getCommandLine().setListening(true); Not listening
            headlessMc.getCommandLine().setCommandContext(new CommandContextImpl(headlessMc));
            assertFalse(headlessMc.getCommandLine().getCommandContext() instanceof ClAgnosticCommandContext);
            Class<?> classLoadingHelperTest = isolatedClassLoader.findClass(ApiClassloadingHelperTest.class.getName());
            assertNotEquals(ApiClassloadingHelperTest.class, classLoadingHelperTest);
            assertInstanceOf(IsolatedClassLoader.class, classLoadingHelperTest.getClassLoader());
            Method runTest = classLoadingHelperTest.getMethod("runTest", boolean.class);
            runTest.invoke(null, false);
            assertFalse(headlessMc.getCommandLine().getCommandContext() instanceof ClAgnosticCommandContext);
        } finally {
            HeadlessMcApi.setInstance(null);
        }
    }

    public static HeadlessMc headlessMc() {
        HeadlessMc headlessMc = new HeadlessMcImpl(ConfigImpl::empty, new CommandLine(), new ExitManager(), new LoggingService());
        headlessMc.getLoggingService().init();
        return headlessMc;
    }

    @SuppressWarnings("unused") // called by reflection
    public static void checkAssertions() {
        assertEquals("test", execute);
        assertEquals(1, commandArgs.length);
        assertEquals("arg1", commandArgs[0]);
    }

    @SuppressWarnings("unused") // called by reflection
    public static void runTest(boolean assertTrue) {
        HeadlessMc headlessMc = headlessMc();
        HeadlessMcApi.setInstance(headlessMc);
        if (assertTrue) {
            assertNotNull(ApiClassloadingHelper.installOnOtherInstances(headlessMc));
        } else {
            assertNull(ApiClassloadingHelper.installOnOtherInstances(headlessMc));
        }

        headlessMc.getCommandLine().setCommandContext(new CommandContext() {
            @Override
            public void execute(String command) {
                execute = command;
            }

            @Override
            public Iterator<Command> iterator() {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command() {
                    @Override
                    public void execute(String line, String... args) {
                        commandArgs = args;
                    }

                    @Override
                    public boolean matches(String line, String... args) {
                        return args[0].equals("test");
                    }

                    @Override
                    public String getName() {
                        return "testname";
                    }

                    @Override
                    public Iterable<String> getArgs() {
                        return Collections.singleton("testarg");
                    }

                    @Override
                    public String getArgDescription(String arg) {
                        return "testarg".equals(arg) ? "testargdesc" : "0";
                    }

                    @Override
                    public Iterable<Map.Entry<String, String>> getArgs2Descriptions() {
                        return new ArrayList<>();
                    }

                    @Override
                    public String getDescription() {
                        return "testdesc";
                    }
                });

                return commands.iterator();
            }
        });
    }

}
