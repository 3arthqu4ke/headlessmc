package me.earth.headlessmc.launcher.command.forge;

import com.google.gson.JsonElement;
import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.launcher.util.AbstractDownloadService;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static me.earth.headlessmc.launcher.util.URLs.url;

@CustomLog
public class ForgeIndexCache extends AbstractDownloadService<ForgeVersion> {
    public static final URL LEX_FORGE_INDICES = url("https://meta.prismlauncher.org/v1/net.minecraftforge/index.json");
    public static final URL NEO_FORGE_INDICES = url("https://meta.prismlauncher.org/v1/net.neoforged/index.json");

    public ForgeIndexCache(URL url) {
        super(url);
    }

    @Override
    protected List<ForgeVersion> read(JsonElement element) throws IOException {
        val array = element.getAsJsonObject().get("versions").getAsJsonArray();
        val result = new ArrayList<ForgeVersion>(array.size());
        for (val je : array) {
            val version = JsonUtil.GSON.fromJson(je, ForgeVersion.class);
            result.add(version);
        }

        return result;
    }

}
