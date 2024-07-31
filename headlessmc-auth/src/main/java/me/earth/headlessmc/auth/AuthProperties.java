package me.earth.headlessmc.auth;

import me.earth.headlessmc.api.config.Property;

import static me.earth.headlessmc.config.PropertyTypes.bool;
import static me.earth.headlessmc.config.PropertyTypes.string;

public interface AuthProperties {
    Property<String> WEBVIEW_DEVICE_TOKEN = string("hmc.auth.webview.device.token");
    Property<Boolean> WEBVIEW_NO_DEVICE_TOKEN = bool("hmc.auth.webview.no.device.token");

}
