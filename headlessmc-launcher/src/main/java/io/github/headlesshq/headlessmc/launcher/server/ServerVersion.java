package io.github.headlesshq.headlessmc.launcher.server;

import lombok.Data;

@Data
public class ServerVersion {
    private final ServerType serverType;
    private final String version;
    private final String typeVersion;

}
