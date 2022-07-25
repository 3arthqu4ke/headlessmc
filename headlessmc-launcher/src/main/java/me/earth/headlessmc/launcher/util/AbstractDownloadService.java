package me.earth.headlessmc.launcher.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Cleanup;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.launcher.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@CustomLog
@RequiredArgsConstructor
public abstract class AbstractDownloadService<T> extends Service<T> {
    private final URL url;

    protected abstract List<T> read(JsonElement element)
        throws IOException, JsonParseException;

    @Override
    protected Collection<T> update() {
        List<T> result;
        log.debug("Downloading from " + url);
        try {
            val con = URLs.get(url);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            @Cleanup
            val reader = URLs.reader(con);
            val je = JsonParser.parseReader(reader);
            result = read(je);
        } catch (JsonParseException | IOException e) {
            log.error("Couldn't download from " + url + " : " + e.getMessage());
            result = new ArrayList<>(0);
        }

        return result;
    }

}
