package org.seirobotics.skyway.openapidemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Javier
 * @date 2020/9/9 11:29
 */
class HMACSHA256 {
    private static Logger logger = LoggerFactory.getLogger(HMACSHA256.class);

    /**
     * sha256_HMAC
     *
     * @param message encryption content
     * @param secret  encrypt secret
     * @return the encrypted string
     */
    static String sha256_HMAC(String message, String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            logger.error("HMACSHA256 execute exception");
            e.printStackTrace();
        }
        return hash;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String tmp;
        for (int n = 0; b != null && n < b.length; n++) {
            tmp = Integer.toHexString(b[n] & 0XFF);
            if (tmp.length() == 1) {
                hs.append('0');
            }
            hs.append(tmp);
        }
        return hs.toString().toLowerCase();
    }
}