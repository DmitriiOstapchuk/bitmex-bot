package com.dmitrii.ostapchuk.bitmexBot.util.generator;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class SignatureGenerator {
    public String getSignature(String secretKey, String message) throws NoSuchAlgorithmException,
            InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);

        byte[] hashedBytes = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexStringBuilder = new StringBuilder();

        for (byte b : hashedBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}
