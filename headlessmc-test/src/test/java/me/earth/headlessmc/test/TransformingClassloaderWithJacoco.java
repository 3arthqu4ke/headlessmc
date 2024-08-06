package me.earth.headlessmc.test;

import me.earth.headlessmc.wrapper.plugin.TransformerPlugin;
import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;
import org.jacoco.agent.rt.RT;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class TransformingClassloaderWithJacoco extends TransformingClassloader {
    private final IRuntime runtime = new LoggerRuntime();
    private final Instrumenter instrumenter = new Instrumenter(runtime);

    public TransformingClassloaderWithJacoco(URL[] urls, ClassLoader parent, URLClassLoader transformerClassloader, List<TransformerPlugin> plugins) {
        super(urls, parent, transformerClassloader, plugins);
        RT.getAgent().get
    }

    @Override
    protected byte[] instrument(String name, byte[] classBytes) throws ClassNotFoundException {
        byte[] result = super.instrument(name, classBytes);
        try {
            return instrumenter.instrument(result, name);
        } catch (IOException e) {
            throw new ClassNotFoundException("Jacoco failed to instrument " + name, e);
        }
    }

}
