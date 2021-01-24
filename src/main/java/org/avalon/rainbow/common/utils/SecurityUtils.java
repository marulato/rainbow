package org.avalon.rainbow.common.utils;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class SecurityUtils {

    private static final ECPublicKey ecPublicKey;
    private static final ECPrivateKey ecPrivateKey;

    static {

        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            keyPairGenerator = new KeyPairGenerator("EC") {
            };
        }
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        ecPublicKey = (ECPublicKey) keyPair.getPublic();
        ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();

    }

    public static ECPublicKey getEcPublicKey() {
        return ecPublicKey;
    }

    public static ECPrivateKey getEcPrivateKey() {
        return ecPrivateKey;
    }

}
