package io.github.headlesshq.headlessmc.launcher.version;

public class DefaultVersionFactory extends VersionFactory {
    private static final RuleFactory RF = new RuleFactory();
    private static final ExtractorFactory EF = new ExtractorFactory();
    private static final NativesFactory NF = new NativesFactory();
    private static final LibraryFactory LF = new LibraryFactory(RF, EF, NF);
    private static final ArgumentFactory AF = new ArgumentFactory(RF);

    public DefaultVersionFactory() {
        super(LF, new JavaMajorVersionParser(), AF);
    }

}
