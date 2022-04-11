package me.earth.headlessmc.launcher.auth;

import lombok.Data;
import me.earth.headlessmc.api.HasName;

@Data
public class Account implements HasName {
    private final String type = "msa";
    private final String token;
    private final String name;
    private final String id;

}
