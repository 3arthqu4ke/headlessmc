package me.earth.headlessmc.launcher.command.forge;

public interface ForgeRepoFormat {
    // E.g. https://maven.neoforged.net/releases/net/neoforged/neoforge/20.4.195/neoforge-20.4.195-installer.jar
    String NEO_FORGE_URL = "https://maven.neoforged.net/releases/net/neoforged/neoforge/";
    // Exception for 1.20.1: https://maven.neoforged.net/releases/net/neoforged/forge/1.20.1-47.1.104/forge-1.20.1-47.1.104-installer.jar
    String NEO_FORGE_ALTERNATIVE_URL = "https://maven.neoforged.net/releases/net/neoforged/forge/";
    // E.g. https://maven.minecraftforge.net/net/minecraftforge/forge/1.20.2-48.1.0/forge-1.20.2-48.1.0-installer.jar
    String LEX_FORGE_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/";

    String getFileName(ForgeVersion version);

    String getUrl(String baseUrl, ForgeVersion version);

    static ForgeRepoFormat lexForge() {
        return new ForgeRepoFormat() {
            @Override
            public String getFileName(ForgeVersion version) {
                return "forge-" + version.getFullName() + "-installer.jar";
            }

            @Override
            public String getUrl(String baseUrl, ForgeVersion version) {
                return baseUrl + version.getFullName() + "/" + getFileName(version);
            }
        };
    }

    static ForgeRepoFormat neoForge() {
        return new ForgeRepoFormat() {
            @Override
            public String getFileName(ForgeVersion version) {
                // Exception for 1.20.1: https://maven.neoforged.net/releases/net/neoforged/forge/1.20.1-47.1.104/forge-1.20.1-47.1.104-installer.jar
                if ("1.20.1".equals(version.getVersion())) {
                    return "forge-" + version.getFullName() + "-installer.jar";
                }

                return "neoforge-" + version.getName() + "-installer.jar";
            }

            @Override
            public String getUrl(String baseUrl, ForgeVersion version) {
                // Exception for 1.20.1: https://maven.neoforged.net/releases/net/neoforged/forge/1.20.1-47.1.104/forge-1.20.1-47.1.104-installer.jar
                if ("1.20.1".equals(version.getVersion()) && baseUrl.equals(NEO_FORGE_URL)) {
                    return NEO_FORGE_ALTERNATIVE_URL + version.getFullName() + "/" + getFileName(version);
                }

                return baseUrl + version.getName() + "/" + getFileName(version);
            }
        };
    }

}
