package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import java.io.File;

public interface ParsesVersions extends ParsesLibraries {
    default VersionFactory getVersionFactory() {
        return new DefaultVersionFactory();
    }

    @SneakyThrows
    default Version parseVersion(JsonObject jsonObject) {
        return getVersionFactory()
            .parse(jsonObject, new File("dummy"), () -> 0);
    }

}
