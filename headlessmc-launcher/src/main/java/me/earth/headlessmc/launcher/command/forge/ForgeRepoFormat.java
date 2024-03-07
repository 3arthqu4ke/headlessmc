package me.earth.headlessmc.launcher.command.forge;

public interface ForgeRepoFormat {
    // E.g. https://maven.neoforged.net/releases/net/neoforged/neoforge/20.4.195/neoforge-20.4.195-installer.jar
    String NEO_FORGE_URL = " https://maven.neoforged.net/releases/net/neoforged/neoforge/";
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
                return "neoforge-" + version.getName() + "-installer.jar";
            }

            @Override
            public String getUrl(String baseUrl, ForgeVersion version) {
                return baseUrl + version.getName() + "/" + getFileName(version);
            }
        };
    }

}
