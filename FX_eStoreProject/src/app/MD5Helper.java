package app;

import java.security.MessageDigest;

public class MD5Helper {

    /**
     * دالة لتشفير النصوص باستخدام MD5
     * @param input النص العادي
     * @return النص بعد التشفير
     */
    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
