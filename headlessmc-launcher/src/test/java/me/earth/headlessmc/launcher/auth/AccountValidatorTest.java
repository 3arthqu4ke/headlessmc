package me.earth.headlessmc.launcher.auth;

import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountValidatorTest implements UsesResources {
    @Test
    public void testEntitlementsParsing() {
        AccountValidator.Entitlements entitlements = JsonUtil.GSON.fromJson(getJsonObject("entitlements.json"), AccountValidator.Entitlements.class);
        assertEquals(2, entitlements.getItems().size());

        assertEquals("game_minecraft", entitlements.getItems().get(0).getName());
        assertEquals("test.ewogICJzaWduZXJJZCIgOiAiMTIzNDU2NyIsCiAgIm5hbWUiIDogImdhbWVfbWluZWNyYWZ0Igp9.random", entitlements.getItems().get(0).getSignature());

        assertEquals("game_minecraft_bedrock", entitlements.getItems().get(1).getName());
        assertEquals("game_minecraft_bedrock_signature", entitlements.getItems().get(1).getSignature());

        assertEquals("test", entitlements.getSignature());
        assertEquals("1", entitlements.getKeyId());
    }

    @Test
    public void testXuidParsing() throws AuthException {
        AccountValidator.Entitlements entitlements = JsonUtil.GSON.fromJson(getJsonObject("entitlements.json"), AccountValidator.Entitlements.class);
        assertEquals("1234567", entitlements.getItems().get(0).parseXuid());
    }

}
