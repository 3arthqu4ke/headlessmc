package me.earth.headlessmc.launcher.command.forge;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import me.earth.headlessmc.api.HasName;

import java.util.List;

@Data
public class ForgeVersion implements HasName {
    @SerializedName("requires")
    private List<Requires> requires;

    @SerializedName("version")
    private String name;

    @Data
    public static class Requires {
        @SerializedName("equals")
        private String equals;

        // @SerializedName("uid") <- check this
        // private String netMinecraft; seems to always be "net.minecraft"
    }

    public String getVersion() {
        return getRequires().get(0).equals;
    }

    public String getFullName() {
        return getVersion() + "-" + getName();
    }

}
