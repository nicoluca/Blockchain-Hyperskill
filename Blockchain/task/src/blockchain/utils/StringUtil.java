package blockchain.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

public class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getMockMessages() {
        return Arrays.asList(
                "Tom: Hey, I'm first!",
                "Sarah: It's not fair!",
                "Sarah: You always will be first because it is your blockchain!",
                "Sarah: Anyway, thank you for this amazing chat.",
                "Tom: You're welcome :)",
                "Nick: Hey Tom, nice chat",
                "Tom: Hey Nick, nice to see you here",
                "Sarah: Hey Nick, nice to see you here",
                "Nick: Hey Sarah, nice to see you here",
                "Sarah: Hey Tom, nice to see you here",
                "Tom: Hey Sarah, nice to see you here",
                "Sarah: Hey Nick, nice to see you here",
                "Nick: Hey Sarah, nice to see you here",
                "Sarah: Hey Tom, nice to see you here",
                "Tom: Hey Sarah, nice to see you here"
        );
    }
}