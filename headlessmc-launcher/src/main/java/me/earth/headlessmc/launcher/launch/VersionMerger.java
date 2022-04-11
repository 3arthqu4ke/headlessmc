package me.earth.headlessmc.launcher.launch;

import me.earth.headlessmc.launcher.version.Argument;
import me.earth.headlessmc.launcher.version.Library;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.family.FamilyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class VersionMerger extends DelegatingVersion {
    public VersionMerger(Version version) {
        super(version);
    }

    @Override
    public String getAssets() {
        return get(Version::getAssets);
    }

    @Override
    public int getJava() {
        return get(Version::getJava);
    }

    @Override
    public String getMainClass() {
        return get(Version::getMainClass);
    }

    @Override
    public String getAssetsUrl() {
        return get(Version::getAssetsUrl);
    }

    @Override
    public List<Library> getLibraries() {
        return merge(Version::getLibraries);
    }

    @Override
    public List<Argument> getArguments() {
        if (version.isNewArgumentFormat()) {
            return merge(Version::getArguments);
        } else {
            return get(Version::getArguments);
        }
    }

    @Override
    public String getClientDownload() {
        return get(Version::getClientDownload);
    }

    private <T> List<T> merge(Function<Version, List<T>> func) {
        List<T> result = new ArrayList<>();
        FamilyUtil.iterate(version, v -> result.addAll(func.apply(v)));
        return result;
    }

    private <T> T get(Function<Version, T> func) {
        return FamilyUtil.iterateParents(version, () -> null, func);
    }

}
