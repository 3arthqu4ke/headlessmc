package me.earth.headlessmc.launcher.auth;

import com.google.gson.JsonObject;
import lombok.Data;
import me.earth.headlessmc.api.HasName;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;

@Data
public class ValidatedAccount implements HasName {
    private final StepFullJavaSession.FullJavaSession session;
    private final String xuid;

    @Override
    public String getName() {
        return session.getMcProfile().getName();
    }

    public LaunchAccount toLaunchAccount() {
        return new LaunchAccount(session.getMcProfile().getMcToken().getTokenType(),
                                 session.getMcProfile().getName(),
                                 session.getMcProfile().getId().toString(),
                                 session.getMcProfile().getMcToken().getAccessToken(),
                                 xuid);
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("session", MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.toJson(session));
        jsonObject.addProperty("xuid", xuid);
        return jsonObject;
    }

    public static ValidatedAccount fromJson(JsonObject jsonObject) {
        StepFullJavaSession.FullJavaSession session = MinecraftAuth.JAVA_DEVICE_CODE_LOGIN.fromJson(jsonObject.get("session").getAsJsonObject());
        return new ValidatedAccount(session, jsonObject.get("xuid").getAsString());
    }

}
