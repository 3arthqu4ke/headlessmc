package me.earth.headlessmc.launcher.instrumentation;

import lombok.CustomLog;
import lombok.Getter;
import lombok.SneakyThrows;
import me.earth.headlessmc.launcher.util.IOUtil;
import org.objectweb.asm.tree.ClassNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@CustomLog
public class InstrumentationClassloader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final List<Target> targets = new ArrayList<>();
    private final AggregateTransformer transformer;

    @SneakyThrows
    public InstrumentationClassloader(URL[] urls, ClassLoader parent, List<AbstractClassTransformer> transformers) {
        super(urls, parent);
        this.transformer = new AggregateTransformer(transformers);
        for (URL url : urls) {
            targets.add(new Target(false, Paths.get(url.toURI()).toAbsolutePath().toString()));
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        try (InputStream is = this.getResourceAsStream(path)) {
            if (is == null) {
                throw new ClassNotFoundException(name);
            }

            byte[] classBytes = transformer.maybeTransform(new EntryStream(is, targets, () -> path));
            if (classBytes == null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtil.copy(is, baos);
                classBytes = baos.toByteArray();
            }

            Path debugPath = Paths.get("build").resolve("instrumentation").resolve(path);
            Files.createDirectories(debugPath.getParent());
            try (OutputStream outputStream = Files.newOutputStream(debugPath)) {
                outputStream.write(classBytes);
            }

            log.debug("Defining class " + name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            log.error("Failed to define class " + name, e);
            throw new ClassNotFoundException(name, e);
        }
    }

    @Getter
    public static final class AggregateTransformer extends AbstractClassTransformer {
        private final List<AbstractClassTransformer> transformers;
        public AggregateTransformer(List<AbstractClassTransformer> transformers) {
            super("");
            this.transformers = transformers;
        }

        // TODO: could we use the this ClassLoader for the EntryClassWriter?

        @Override
        protected void transform(ClassNode cn) {
            transformers.forEach(t -> t.transform(cn));
        }

        @Override
        protected boolean matches(EntryStream stream) {
            return transformers.stream().anyMatch(t -> t.matches(stream));
        }
    }

}
