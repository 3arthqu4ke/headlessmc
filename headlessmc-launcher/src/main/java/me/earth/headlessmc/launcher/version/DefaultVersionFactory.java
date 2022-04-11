package me.earth.headlessmc.launcher.version;

import me.earth.headlessmc.api.config.Config;

class DefaultVersionFactory extends VersionFactory {
    private static final RuleFactory RF = new RuleFactory();
    private static final ExtractorFactory EF = new ExtractorFactory();
    private static final NativesFactory NF = new NativesFactory();
    private static final LibraryFactory LF = new LibraryFactory(RF, EF, NF);
    private static final ArgumentFactory AF = new ArgumentFactory(RF);

    public DefaultVersionFactory(Config config) {
        super(LF, new JavaMajorVersionParser(config), AF);
    }

}
