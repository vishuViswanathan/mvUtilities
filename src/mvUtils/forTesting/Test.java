package mvUtils.forTesting;

import mvUtils.http.PostToWebSite;
import mvUtils.mvXML.ValAndPos;
import mvUtils.mvXML.XMLmv;
import mvUtils.security.GCMCipher;

import java.util.HashMap;

/**
 * User: M Viswanathan
 * Date: 06-Jun-16
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void wasteTime(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }

    public static void main(String[] args) {
        PostToWebSite jspReq =  new PostToWebSite("http://localhost:9080/fceCalculations/jsp/");
//        PostToWebSite jspReq =  new PostToWebSite("http://localhost:9080/fceCalculations/jsp/");
        HashMap<String, String> params = new HashMap<>();
        GCMCipher cipher = new GCMCipher();
//        String encrypedUSer = cipher.encrypt("viswanathanm");
//        System.out.println("encrypedUSer :" + encrypedUSer);
//        System.out.println("decryptyed encrypedUSer :" + cipher.decrypt(encrypedUSer));


        byte[] keyBytes = {11, 103};
        String key =  cipher.bytesToByteString(keyBytes);
        params.put("user", cipher.encryptStringWithKey("viswanathanm", key));
        params.put("appCode", "100");
        String ecryptedKey = cipher.bytesToByteString(cipher.encrypt(keyBytes));
//        System.out.println("ecryptedKey : " + ecryptedKey);
//        System.out.println("decryped ecryptedKey : " + cipher.decrypt(ecryptedKey));

        params.put("key", cipher.bytesToByteString(cipher.encrypt(keyBytes)));
        String response = jspReq.getByPOSTRequest("getAccessCode.jsp", params, 2000);
        System.out.println(response);
        if (response.length() > 0)  {
            ValAndPos vp = XMLmv.getTag(response, "accessIDEncrypt", 0);
            String accessIDEncrypt = vp.val;
            System.out.println("accessIDEncrypt = " + accessIDEncrypt);
            String accessID = cipher.decryptStringWithKey(accessIDEncrypt, key);
            System.out.println("accessID = " + accessID);
        }
    }
}

