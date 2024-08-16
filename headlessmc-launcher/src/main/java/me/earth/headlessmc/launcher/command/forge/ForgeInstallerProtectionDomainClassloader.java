package me.earth.headlessmc.launcher.command.forge;

import me.earth.headlessmc.launcher.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

/**
 * Creates a ProtectionDomain for
 * <a href=https://github.com/3arthqu4ke/ForgeCLI/blob/703c0d27dde9a50e98f2ba309fbff08074c94cbc/src/main/java/net/kunmc/lab/forgecli/Main.java#L30>
 *     https://github.com/3arthqu4ke/ForgeCLI/blob/master/src/main/java/net/kunmc/lab/forgecli/Main.java</a>
 */
public class ForgeInstallerProtectionDomainClassloader extends URLClassLoader {
    private static final String CLASS_NAME = "net.kunmc.lab.forgecli.Main";
    private final URL url;

    public ForgeInstallerProtectionDomainClassloader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader());
        this.url = urls[0];
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (CLASS_NAME.equals(name)) {
            try (InputStream is = getResourceAsStream(name.replace('.', '/').concat(".class"))) {
                if (is == null) {
                    throw new ClassNotFoundException(name);
                }

                byte[] bytes = IOUtil.toBytes(is);
                return defineClass(name, bytes, 0, bytes.length, new ProtectionDomain(new CodeSource(url, (Certificate[]) null), null));
            } catch (IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        }

        return super.findClass(name);
    }

}
