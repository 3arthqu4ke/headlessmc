package me.earth.headlessmc.gradle

class Requires {
    final String pkg
    final int access
    final String version

    Requires(String pkg, int access, String version) {
        this.pkg = pkg.replace('/', '.')
        this.access = access
        this.version = version
    }

}
