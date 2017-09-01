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
    enum ForTest {AVAILABLE, NOTVAILBLE};

    public static void main(String[] unused) throws Exception {
        System.out.println("ForTest = " + ForTest.NOTVAILBLE);
        GCMCipher cipher = new GCMCipher();
        byte[] seed = {0}; // this has to be changed  could YY, mm, dd, constants
        String data = "viswanathanm";
        String encyptedKey = cipher.bytesToByteString(cipher.encrypt(seed));
        String encryptedData = cipher.encryptStringWithKey2(data, encyptedKey);
        System.out.println("encryptedData = :[" + encryptedData + "]");
        System.out.println("encyptedKey = :[" + encyptedKey + "]");
        MachineCheck mc = new MachineCheck();
        String machineID = mc.getMachineID(true);
        String encryptedMachineID = cipher.encryptStringWithKey2(machineID, encyptedKey);
        System.out.print("encryptedMachineID = [" + encryptedMachineID + "]");
        System.out.println("decryptedData = " + cipher.decryptStringWithKey2(encryptedData, encyptedKey));
        System.out.println("resp :" + cipher.decryptStringWithKey2("81E8D74D093BC887403C903046A84DBC2B6E"  , encyptedKey));

        System.out.println("sofwarekey :" + cipher.decryptStringWithKey2("5920257ED046AA12DA11346540BF2CBCF871A9C7A35CE6F4687E85E53708A698", encyptedKey));
/*
        byte[] encBytes = cipher.encrypt(data, seed);
        String decString = cipher.decrypt(encBytes, seed);
        System.out.println("Encrypted seed :" + cipher.bytesToByteString(cipher.encrypt(seed)));
        boolean expected = decString.equals(data);
        System.out.println(HexDump.toHex(encBytes));
//        String encBytesStr =  HexDump.toHex(encBytes).replace(", ", "");
        String encBytesStr =  cipher.bytesToByteString(encBytes);
        System.out.println(encBytesStr);
        encBytesStr = encBytesStr.replace("[", "").replace("]", "");
        byte[] d = cipher.byteStringToBytes(encBytesStr);

        String keyString = "0462F36937A98A192E21159541CF5A41"; // cipher.bytesToByteString(seed);
        String encStr = "71DDE0BAA45FF367C80A5F4F796C9139AEF0444E852B0987619950B2" ; // cipher.encryptStringWithKey(data, keyString);
        System.out.println("encrypted with String " + keyString + ":" +  encStr);

        String decStr = cipher.decryptStringWithKey2(encStr, keyString); //cipher.bytesToByteString(cipher.encrypt(seed)));
        System.out.println("The decrypted String : " + decStr);
*/
    }
}
