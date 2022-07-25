package me.earth.headlessmc.gradle

class ExportsOrOpens {
    final String pkg
    final int access
    final String[] to

    ExportsOrOpens(String pkg, int access, String... to) {
        this.pkg = pkg.replace('\\.', '/')
        this.access = access
        this.to = to == null ? null : Arrays.stream(to as String[])
                .map(s -> s.replace('\\.', '/'))
                .toArray(String[]::new)
    }

}
