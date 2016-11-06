package com.database;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.logging.Logger;

/**
 * Utility class for generating a SHA256 encrypted password
 * Created by samlinz on 16.10.2016.
 */
public class StringCrypt {

    private static MessageDigest sha;
    private static Logger LOG;

    /**
     * Changes byte array to hex formatted string
     *
     * @param arr byte array
     * @return string representing the byte array in hex format converted to String
     */
    private static String byteArrToHexString(byte[] arr) {
        StringBuilder builder = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            // convert into signed integer
            int v = arr[i] & 0xff;
            // pad
            if (v < 16) {
                builder.append(0);
            }
            // convert into hex
            builder.append(Integer.toHexString(v));
        }
        return builder.toString().toUpperCase();
    }

    /**
     * Encrypts a string with SHA256-algorithm
     *
     * @param text unencrypted string to be encrypted
     * @return encryped String
     */
    public static String encrypt(String text) {
        String result = "";
        try {
            sha.update(text.getBytes("UTF-8"));
            result = byteArrToHexString(sha.digest());
        } catch (IOException e) {
            LOG.warning("Failed to encrypt password");
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    static {
        LOG = Logger.getLogger(StringCrypt.class.getName());

        // get an instance of the message digester
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            LOG.warning("Failed to get encrypter");
            e.printStackTrace();
        }
    }
}
