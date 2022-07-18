package me.earth.headlessmc.command;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import me.earth.headlessmc.api.command.CommandException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordCommandTest {
    @Test
    @SneakyThrows
    public void passwordCommandTest() {
        val hmc = MockedHeadlessMc.INSTANCE;

        hmc.setHidingPasswordsSupported(true);
        assertTrue(hmc.isHidingPasswordsSupported());

        val command = new PasswordCommand(hmc);

        hmc.setHidingPasswords(false);
        assertFalse(hmc.isHidingPasswords());

        command.execute("password");
        assertTrue(hmc.isHidingPasswords());
        command.execute("password");
        assertFalse(hmc.isHidingPasswords());

        hmc.setHidingPasswordsSupported(false);
        assertFalse(hmc.isHidingPasswordsSupported());
        assertThrows(CommandException.class, () -> command.execute("password"));

        hmc.setHidingPasswordsSupported(true);
        assertTrue(hmc.isHidingPasswordsSupported());
    }

}
