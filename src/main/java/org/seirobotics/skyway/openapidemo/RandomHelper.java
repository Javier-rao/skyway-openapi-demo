package org.seirobotics.skyway.openapidemo;

import java.util.Random;

/**
 * @author Javier
 * @date 2020/9/9 - 14:12
 */
class RandomHelper {
    static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
