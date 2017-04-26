package mvUtils.security;

import org.apache.poi.util.HexDump;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.print.DocFlavor;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * User: M Viswanathan
 * Date: 21-Apr-17
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GCMCipher {
    String xform = "AES/GCM/NoPadding";

    public SecretKey makeKey(byte[] seed) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            SecureRandom sR = new SecureRandom(seed);
            kg.init(128, sR); // 56 is the keysize. Fixed for DES
            return kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public byte[] encrypt(byte[] inpBytes) {
        byte[] retVal;
        try {
            retVal = encrypt(inpBytes, makeKey(new byte[]{100, 78}), xform);
        } catch (Exception e) {
            retVal = new byte[0];
        }
        return retVal;
    }

    /**
     *
     * @param inpString
     * @param keyBytes are themselves encrypted
     * @return
     */
    public byte[] encrypt2(String inpString,
                          byte[] keyBytes) {
        byte[] trueKeyBytes = decrypt(keyBytes);
        if (trueKeyBytes.length > 0)
            return encrypt(inpString, trueKeyBytes);
        else
            return new byte[0];
    }

    public byte[] encrypt(String inpString,
                          byte[] keyBytes) {
        SecretKey key = makeKey(keyBytes);
        if (key != null)
            return encrypt(inpString, key);
        else
            return new byte[0];
    }

    public String encryptStringWithKey(String inpString, String key) {
        String encBytesStr;
        byte[] keyBytes =  byteStringToBytes(key);
        if (keyBytes.length > 0) {
            byte[] encryptedBytes = encrypt(inpString, keyBytes);
            encBytesStr = bytesToByteString(encryptedBytes);
        }
        else
            encBytesStr = "";
        return encBytesStr;
    }
    
    public String decryptStringWithKey2(String inpString, String key) {
        String encBytesStr;
        byte[] encryptedKeyBytes =  byteStringToBytes(key);
        byte[] encryptedData = byteStringToBytes(inpString);
        if (encryptedKeyBytes.length > 0 && encryptedData.length > 0) {
            encBytesStr = decrypt2(encryptedData, encryptedKeyBytes);
        }
        else
            encBytesStr = "";
        return encBytesStr;
    }

    public byte[] encrypt(String inpString,
                          SecretKey key) {
        byte[] retVal;
        try {
            retVal = encrypt(inpString.getBytes(), key, xform);
        } catch (Exception e) {
            retVal = new byte[0];
        }
        return retVal;
    }

    public byte[] encrypt(byte[] inpBytes,
                                  SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        byte[] ivBytes = new byte[256 / Byte.SIZE];
        GCMParameterSpec gcmSpecWithIV = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpecWithIV);
        return cipher.doFinal(inpBytes);
    }

    public String decryptStringWithKey(String encrypedString, String key) {
        String decStr;
        byte[] keyBytes =  byteStringToBytes(key);
        byte[] encryptedBytes = byteStringToBytes(encrypedString);
        if (keyBytes.length > 0 && encryptedBytes.length > 0) {
            decStr = decrypt(encryptedBytes, keyBytes);
        }
        else
            decStr = "";
        return decStr;
    }


    public byte[] decrypt(byte[] inpBytes) {
        byte[] retVal;
        try {
            retVal = decrypt(inpBytes, makeKey(new byte[]{100, 78}), xform);
        } catch (Exception e) {
            retVal = new byte[0];
        }
        return retVal;
    }

    /**
     *
     * @param inpBytes
     * @param keyBytes  are themselves encrypted
     * @return
     */
    public String decrypt2(byte[] inpBytes,
                          byte[] keyBytes) {
        byte[] trueKeyBytes = decrypt(keyBytes);
        if (trueKeyBytes.length > 0)
            return decrypt(inpBytes, trueKeyBytes);
        else
            return "";
    }

    public String decrypt(byte[] inpBytes,
                          byte[] keyBytes) {
        SecretKey key = makeKey(keyBytes);
        if (key != null)
            return decrypt(inpBytes, key);
        else
            return "";
    }

    public String decrypt(byte[] inpBytes,
                          SecretKey key){
        String retVal = "";
        try {
            retVal = new String(decrypt(inpBytes, key, xform));
        } catch (Exception e) {
            ;
        }
        return retVal;
    }

    public byte[] decrypt(byte[] inpBytes,
                                  SecretKey key, String xform) throws Exception {
        Cipher cipher = Cipher.getInstance(xform);
        byte[] ivBytes = new byte[256 / Byte.SIZE];
        GCMParameterSpec gcmSpecWithIV = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpecWithIV);
        return cipher.doFinal(inpBytes);
    }

    public byte[] byteStringToBytes(String byteString)  {
        byte[] bytes;
        int len = byteString.length();
        if (len > 1) {
            bytes = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                bytes[i / 2] = (byte) ((Character.digit(byteString.charAt(i), 16) << 4)
                        + Character.digit(byteString.charAt(i + 1), 16));
            }
        }
        else
            bytes = new byte[0];
        return bytes;
    }

    public String bytesToByteString(byte[] bytes) {
        String encBytesStr;
        if (bytes.length > 0) {
            encBytesStr = HexDump.toHex(bytes).replace("[", "").replace("]", "").replace(", ", "");
        }
        else
            encBytesStr = "";
        return encBytesStr;
    }
}
