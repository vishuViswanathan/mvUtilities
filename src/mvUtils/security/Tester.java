package mvUtils.security;

import org.apache.poi.util.HexDump;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

/**
 * User: M Viswanathan
 * Date: 24-Apr-17
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class Tester {
    public static void main(String[] unused) throws Exception {
        GCMCipher cipher = new GCMCipher();
        byte[] seed = {17, 04, 21, 100, 64, 15}; // this has to be changed  could YY, mm, dd, constants
         String data = "J2EExx&*2#";
        byte[] encBytes = cipher.encrypt(data, seed);
        String decString = cipher.decrypt(encBytes, seed);

        boolean expected = decString.equals(data);
        System.out.println(HexDump.toHex(encBytes));
//        String encBytesStr =  HexDump.toHex(encBytes).replace(", ", "");
        String encBytesStr =  cipher.bytesToByteString(encBytes);
        System.out.println(encBytesStr);
        encBytesStr = encBytesStr.replace("[", "").replace("]", "");
        byte[] d = cipher.byteStringToBytes(encBytesStr);

        String keyString = cipher.bytesToByteString(seed);
        String encStr = cipher.encryptStringWithKey(data, keyString);
        System.out.println("encrypted with String " + keyString + ":" +  encStr);

        String decStr = cipher.decryptStringWithKey2(encStr, keyString); //cipher.bytesToByteString(cipher.encrypt(seed)));
        System.out.println("The decrypted String : " + decStr);
//        int len = encBytesStr.length();
//        byte[] d = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            d[i / 2] = (byte) ((Character.digit(encBytesStr.charAt(i), 16) << 4)
//                    + Character.digit(encBytesStr.charAt(i+1), 16));
//        }
//        System.out.println("Again: " + HexDump.toHex(d));



//        System.out.println(decString);
//        System.out.println("Test " + (expected ? "SUCCEEDED!" : "FAILED!"));
//        // now encrypted keyBytes
//        byte[] encBytes2 = cipher.encrypt2(data, cipher.encrypt(seed));
//        String decString2 = cipher.decrypt2(encBytes2, cipher.encrypt(seed));
//        expected = decString2.equals(data);
//        System.out.println(HexDump.toHex(encBytes2));
//        System.out.println(decString2);
//        System.out.println("Test2 " + (expected ? "SUCCEEDED!" : "FAILED!"));
    }
}
