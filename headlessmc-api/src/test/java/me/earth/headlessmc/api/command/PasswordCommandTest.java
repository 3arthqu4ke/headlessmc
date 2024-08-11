package me.earth.headlessmc.api.command;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.api.MockedHeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordCommandTest {
    @Test
    @SneakyThrows
    public void passwordCommandTest() {
        val clm = MockedHeadlessMc.INSTANCE.getCommandLine();

        clm.setHidingPasswordsSupported(true);
        assertTrue(clm.isHidingPasswordsSupported());

        val command = new PasswordCommand(MockedHeadlessMc.INSTANCE);

        clm.setHidingPasswords(false);
        assertFalse(clm.isHidingPasswords());

        command.execute("password", "password");
        assertTrue(clm.isHidingPasswords());
        command.execute("password", "password");
        assertFalse(clm.isHidingPasswords());

        clm.setHidingPasswordsSupported(false);
        assertFalse(clm.isHidingPasswordsSupported());
        assertThrows(CommandException.class, () -> command.execute("password", "password"));

        clm.setHidingPasswordsSupported(true);
        assertTrue(clm.isHidingPasswordsSupported());
    }

}
