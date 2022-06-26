package me.earth.headlessmc.command;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordCommandTest {
    @Test
    @SneakyThrows
    public void passwordCommandTest() {
        val hmc = MockedHeadlessMc.INSTANCE;
        val command = new PasswordCommand(hmc);
        hmc.setHidingPasswords(false);
        Assertions.assertFalse(hmc.isHidingPasswords());
        command.execute("password");
        Assertions.assertTrue(hmc.isHidingPasswords());
        command.execute("password");
        Assertions.assertFalse(hmc.isHidingPasswords());
    }

}
