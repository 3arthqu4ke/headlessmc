package me.earth.headlessmc.launcher.download;

import net.lenni0451.commons.httpclient.HttpResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DownloadServiceTest {
    @Test
    public void testDownload() throws IOException {
        DownloadService downloadService = new DownloadService() {
            @Override
            public HttpResponse get(URL url) {
                return new HttpResponse(url, 404, new byte[0], new HashMap<>());
            }
        };

        DownloadService finalDownloadService = downloadService;
        assertThrows(IOException.class, () -> finalDownloadService.download("http://example.com", Paths.get("build").resolve("test")));
        byte[] bytes = { 1, 2, 3, 4};
        downloadService = new DownloadService() {
            @Override
            public HttpResponse get(URL url) {
                return new HttpResponse(url, 200, bytes, new HashMap<>());
            }
        };

        String sha1 = "12dada1fff4d4787ade3333147202c3b443e376f";
        assertEquals(sha1, downloadService.getChecksumService().hash(bytes));
        assertThrows(IOException.class, () -> finalDownloadService.download("http://example.com", Paths.get("build").resolve("test"), null, "wronghash"));
        assertSame(bytes, downloadService.download(new URL("http://example.com"), null, sha1));
        assertSame(bytes, downloadService.download(new URL("http://example.com"), null, null));
        assertSame(bytes, downloadService.download(new URL("http://example.com"), 4L, null));
    }

}
