package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonObject;
import lombok.val;

import java.util.List;

public interface ParsesLibraries {
    default LibraryFactory getFactory() {
        val rf = new RuleFactory();
        val ef = new ExtractorFactory();
        val nf = new NativesFactory();
        return new LibraryFactory(rf, ef, nf);
    }

    default List<Library> parse(JsonObject jsonObject) {
        return getFactory().parse(jsonObject);
    }

}
