package me.earth.headlessmc.launcher.launch;

import lombok.val;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.version.Features;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

public class ArgumentAdapterTest implements UsesResources {
    @Test
    public void testArgumentAdapter() {
        val child = getVersion("version_child.json", "child_file", 0);
        val parent = getVersion("version_parent.json", "parent_file", 1);
        child.setParent(parent);
        val merger = new VersionMerger(child);

        val mac_os = new OS("mac", OS.Type.OSX, "1", true);
        // val windows = new OS("win", OS.Type.WINDOWS, "11", true);
        val featureMap = new HashMap<String, Boolean>();
        val features = new Features(featureMap);

        val adapter = new ArgumentAdapter(merger.getArguments());
        Assertions.assertEquals(Arrays.asList(
            "-DThing=Thing", "-XstartOnFirstThread",
                "-DSomeSystemProperty=${some_arg}", "-cp", "${classpath}"),
            adapter.build(mac_os, features, "jvm"));

        Assertions.assertEquals(Arrays.asList(
            "--username", "${auth_player_name}",
            "--versionType", "${version_type}"),
            adapter.build(mac_os, features, "game"));

        // TODO: too lazy to add more rn, but test around with features, os etc.
    }

}
