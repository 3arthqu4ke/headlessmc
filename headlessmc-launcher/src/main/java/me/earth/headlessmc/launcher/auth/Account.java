package me.earth.headlessmc.launcher.auth;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.earth.headlessmc.api.HasName;

@Data
@AllArgsConstructor
public class Account implements HasName {
    private final String type = "msa";

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @ToString.Exclude
    @SerializedName("token")
    private String token;

    @ToString.Exclude
    @SerializedName("refreshToken")
    private String refreshToken;

    @ToString.Exclude
    @SerializedName("xuid")
    private String xuid;

    @ToString.Exclude
    @SerializedName("clientId")
    private String clientId;

}
