package me.earth.headlessmc.launcher.launch;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.version.Version;

import java.io.File;

@UtilityClass
class ArgumentAdapterHelper {
    public static ArgumentAdapter create(Launcher launcher,
                                         Version version,
                                         String natives)
        throws AuthException {
        var account = launcher.getAccountManager().getLastAccount();
        if (account == null) {
            account = launcher.getAccountManager().login(launcher.getConfig());
        }

        val config = launcher.getConfig();
        val adapter = new ArgumentAdapter(version.getArguments());

        adapter.remove("-cp");
        adapter.remove("${classpath}");
        adapter.remove("-Djava.library.path\\u003d${natives_directory}");

        adapter.add("${natives_directory}", natives);

        adapter.add("${classpath_separator}", File.pathSeparatorChar + "");
        // TODO: check this
        adapter.add("${library_directory}", launcher.getMcFiles()
                                                    .getDir("libraries")
                                                    .getAbsolutePath());

        adapter.add("${version_name}", version.getName());
        adapter.add("${version_type}", "release");
        val dir = new File(config.get(LauncherProperties.GAME_DIR,
                                      launcher.getMcFiles().getPath()));
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        adapter.add("${game_directory}", dir.getAbsolutePath());
        adapter.add("${assets_index_name}", version.getAssets());
        adapter.add("${auth_access_token}", account.getToken());
        adapter.add("${user_type}", account.getType());
        adapter.add("${auth_player_name}", account.getName());
        adapter.add("${auth_uuid}", account.getId());
        adapter.add("${assets_root}", launcher.getMcFiles()
                                              .getDir("assets")
                                              .getAbsolutePath());

        adapter.add("${launcher_name}", config.get(
            LauncherProperties.LAUNCHER_NAME, "HeadlessMc"));
        adapter.add("${launcher_version}", config.get(
            LauncherProperties.LAUNCHER_VERSION, Launcher.VERSION));

        adapter.add("${auth_xuid}", config.get(
            LauncherProperties.XUID, account.getXuid()));
        adapter.add("${clientid}", config.get(
            LauncherProperties.CLIENT_ID, account.getClientId()));
        // TODO: this is {} for the normal launcher too
        adapter.add("${user_properties}", config.get(
            LauncherProperties.USER_PROPERTIES, "{}"));
        // TODO: find a version where this actually exists
        adapter.add("${profile_properties}", config.get(
            LauncherProperties.PROFILE_PROPERTIES, "{}"));

        return adapter;
    }

}
