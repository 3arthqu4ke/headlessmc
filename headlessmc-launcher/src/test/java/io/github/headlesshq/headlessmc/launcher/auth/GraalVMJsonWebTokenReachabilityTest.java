package io.github.headlesshq.headlessmc.launcher.auth;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms
 * is reached via reflection, so we need this for GraalVM to work.
 */
public class GraalVMJsonWebTokenReachabilityTest {
    @Test
    public void reachForStandartSecureDigestAlgorithms() {
        assertNotNull(Jwts.parser());
        assertNotNull(Jwts.builder());
        assertNotNull(Jwts.header());
        assertNotNull(Jwts.claims());
        assertNotNull(Jwts.ENC.get());
        assertNotNull(Jwts.SIG.get());
        assertNotNull(Jwts.KEY.get());
        assertNotNull(Jwts.ZIP.get());
    }

}
