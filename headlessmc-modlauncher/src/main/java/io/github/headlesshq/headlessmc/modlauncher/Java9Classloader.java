package io.github.headlesshq.headlessmc.modlauncher;

/**
 * Because the {@link #findClass(String, String)} does not exist on Java 8 classloaders we have this interface,
 * so that when compiling with Java 8 still have a method to override in {@link ModuleURLClassLoader}.
 */
@SuppressWarnings("unused")
public interface Java9Classloader {
    /**
     * Documentation from the Java 9 Classloader:
     * <p>Finds the class with the given <a href="#binary-name">binary name</a>
     * in a module defined to this class loader.
     * Class loader implementations that support loading from modules
     * should override this method.
     * <p>
     * ApiNode: This method returns {@code null} rather than throwing
     *          {@code ClassNotFoundException} if the class could not be found.
     * <p>
     * ImplSpec: The default implementation attempts to find the class by
     * invoking Classloader#findClass(String) when the {@code moduleName} is
     * {@code null}. It otherwise returns {@code null}.
     *
     * @param  moduleName
     *         The module name; or {@code null} to find the class in the
     *         unnamed module for this
     *         class loader
     * @param  name
     *         The <a href="#binary-name">binary name</a> of the class
     *
     * @return The resulting {@code Class} object, or {@code null}
     *         if the class could not be found.
     *
     * @since 9
     */
    Class<?> findClass(String moduleName, String name);

}
