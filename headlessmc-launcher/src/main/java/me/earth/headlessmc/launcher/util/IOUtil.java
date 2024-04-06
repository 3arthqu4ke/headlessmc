package me.earth.headlessmc.launcher.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.*;
import java.net.URL;
import java.util.function.Consumer;
import java.util.jar.JarOutputStream;

@UtilityClass
public class IOUtil {
    public static void copy(InputStream i, OutputStream o) throws IOException {
        int length;
        val bytes = new byte[1024];
        while ((length = i.read(bytes)) != -1) {
            o.write(bytes, 0, length);
        }
    }

    public static JarOutputStream jarOutput(File file) throws IOException {
        return new JarOutputStream(new FileOutputStream(file));
    }

    public static BufferedReader reader(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream));
    }

    public static String read(BufferedReader br) throws IOException {
        val sb = new StringBuilder();
        read(br, sb::append);
        return sb.toString();
    }

    public static void read(BufferedReader br, Consumer<String> callback)
        throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            callback.accept(line);
        }
    }

    public static byte[] downloadBytes(String from) throws IOException {
        val url = new URL(from);
        @Cleanup
        val is = url.openStream();
        val baos = new ByteArrayOutputStream();
        IOUtil.copy(is, baos);
        return baos.toByteArray();
    }

    public static void download(String from, String path) throws IOException {
        val url = new URL(from);
        @Cleanup
        val is = url.openStream();
        val to = new File(path);
        //noinspection ResultOfMethodCallIgnored
        to.getParentFile().mkdirs();
        @Cleanup
        val os = new FileOutputStream(to);
        IOUtil.copy(is, os);
    }

}
