package mvUtils.http;
import mvUtils.mvXML.XMLmv;
import org.apache.http.*;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.formula.functions.T;

/**
 * User: M Viswanathan
 * Date: 28-Apr-17
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostToWebSite {
    String basePath;

    public PostToWebSite(String basePath) {
        this.basePath = basePath;
    }

    public String getByPOSTRequest(String destination, HashMap<String, String> params, int maxResponseLength) {
        List<NameValuePair> list = new ArrayList<>(params.size());
        Set keys = params.keySet();
        for (Object o:keys)
            list.add(new BasicNameValuePair((String)o, params.get(o)));
        return getByPOSTRequest(destination, list, maxResponseLength);
    }

    public String getByPOSTRequest(String destination, List<NameValuePair> params, int maxResponseLength) {
        String retVal = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(basePath + destination);
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
//Execute and get the response.
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // The try-with-resources Statement (InputStream isi AutoClosable and closed automatically
                // on completion of try() statement
                try(InputStream instream = entity.getContent()) {
                    byte[] data = new byte[maxResponseLength + 1]; // maximum expected
                    int len = instream.read(data);
                    if (len > 0)
                        retVal = new String(data);
                }
            }
        } catch (IOException e) {
            retVal = XMLmv.putTag("Status", "ERROR") +
                    XMLmv.putTag("msg", e.getMessage());
//            retVal =  e.getMessage();
        }
        return retVal;


    }
}