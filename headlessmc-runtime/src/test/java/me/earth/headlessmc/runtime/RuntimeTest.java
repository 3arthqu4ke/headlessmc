package me.earth.headlessmc.runtime;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.config.ConfigImpl;

public interface RuntimeTest {
    default Runtime getRuntime() {
        return RuntimeApi.init(ConfigImpl.empty(), new PasswordAwareImpl());
    }

    final class PasswordAwareImpl implements PasswordAware {
        @Getter
        @Setter
        private boolean hidingPasswords;

        @Override
        public boolean isHidingPasswordsSupported() {
            return true;
        }
    }

}
