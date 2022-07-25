package me.earth.headlessmc.launcher.util;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.launcher.auth.AccountValidator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility for {@link URL}s.
 */
@UtilityClass
public class URLs {
    @SneakyThrows
    public static URL url(String url) {
        return new URL(url);
    }

    public static HttpURLConnection get(URL url) throws IOException {
        return get(url.toString());
    }

    public static HttpURLConnection get(String url) throws IOException {
        val con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        con.setConnectTimeout(60_000);
        con.setReadTimeout(60_000);
        return con;
    }

    public static Reader reader(HttpURLConnection con) throws IOException {
        int status = con.getResponseCode();
        Reader reader;
        if (status > 299) {
            reader = new InputStreamReader(con.getErrorStream());
        } else {
            reader = new InputStreamReader(con.getInputStream());
        }

        return reader;
    }

}
