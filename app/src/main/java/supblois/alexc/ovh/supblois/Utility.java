package supblois.alexc.ovh.supblois;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import supblois.alexc.ovh.supblois.network.Command;
import supblois.alexc.ovh.supblois.network.NetTask;

public class Utility {
    public static String hashSHA256(String input){
        String hash = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] h = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            hash = toHexString(h);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static Object getExpectedOrNull(Command cmd, int timeoutSeconds){
        NetTask netTask = new NetTask();
        netTask.execute(cmd);

        Object r = null;
        try {
            r = netTask.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return r;
        }
        if (r instanceof String){
            String s = (String) r;
            if (s.equals("ERROR"))
                return null;
        }
        return r;
    }
}
