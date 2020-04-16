/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Niklas
 */
public class Encryption {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    private static String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j< bytes.length; j++){
            int v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String encrypt(String data){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("NoSuchAlgorithmException");
            return null;
        }
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }
    
}
