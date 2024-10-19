package com.project.shahbazi.spring_multiThread_pro.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {
    private static  final String algorithm = "AES";
    private static final String transformation = "AES/ECB/PKCS5Padding";
    private static final String AESSecretKey = "SixteenByteKey00";  // 16-byte AES key for encryption

    public static String decrypt(String encryptedValue) throws Exception {
        SecretKey key = new SecretKeySpec(AESSecretKey.getBytes(), algorithm);
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decryptedBytes = cipher.doFinal(decodedValue);
        return new String(decryptedBytes);
    }
}
