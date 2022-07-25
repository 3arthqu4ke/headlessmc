package me.earth.headlessmc.util;

import lombok.Cleanup;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceUtilTest extends AbstractUtilityTest<ResourceUtil> {
    @Test
    public void testResourceUtil() throws IOException {
        @Cleanup
        val br = new BufferedReader(new InputStreamReader(
            ResourceUtil.getHmcResource("resource_util_test.txt")));

        assertEquals("test", br.readLine());
    }

}
