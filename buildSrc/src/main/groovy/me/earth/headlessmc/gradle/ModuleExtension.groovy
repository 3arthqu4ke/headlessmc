//file:noinspection unused
package me.earth.headlessmc.gradle

import groovy.transform.PackageScope
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property

abstract class ModuleExtension {
    /** For Modules only. */
    public static final int ACC_OPEN = 0x0020
    /** For 'requires' */
    public static final int ACC_STATIC = 0x0040
    /** For 'requires' */
    public static final int ACC_TRANSITIVE = 0x0020
    /** For everything */
    public static final int ACC_SYNTHETIC = 0x1000
    /** For everything */
    public static final int ACC_MANDATED = 0x8000

    abstract Property<String> getName()

    abstract Property<String> getMainClass()

    abstract Property<Integer> getAccess()

    abstract Property<String> getVersion()

    @PackageScope
    final List<String> packages = []

    @PackageScope
    final List<String> uses = []

    @PackageScope
    final List<ExportsOrOpens> opens = []

    @PackageScope
    final List<Provides> provides = []

    @PackageScope
    final List<ExportsOrOpens> exports = []

    @PackageScope
    final List<Requires> requires = []

    abstract DirectoryProperty getDirectory()

    abstract Property<Boolean> getRequireJavaBase()

    void definesPackage(String pkg) {
        packages.add(pkg)
    }

    void uses(String pkg) {
        uses.add(pkg.replace('\\.', '/'))
    }

    void opens(String pkg, int access = 0, String[] to = null) {
        opens.add(new ExportsOrOpens(pkg, access, to))
    }

    void exports(String pkg, int access = 0, String[] to = null) {
        exports.add(new ExportsOrOpens(pkg, access, to))
    }

    void provides(String service, String... with) {
        provides.add(new Provides(service, with))
    }

    void requires(String pkg, int access = 0, String version = null) {
        requires.add(new Requires(pkg, access, version))
    }

}
