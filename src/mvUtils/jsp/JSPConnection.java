package mvUtils.jsp;

import mvUtils.display.ErrorStatAndMsg;

//import javax.jnlp.BasicService;
//import javax.jnlp.ServiceManager;
//import javax.jnlp.UnavailableServiceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;

/**
 * User: M Viswanathan
 * Date: 02-Sep-16
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class JSPConnection {
    URL codeBase;
    public boolean allOK = true;
    String jspPath;

    public JSPConnection(URL codeBase) {
        this.codeBase = codeBase;
    }

    public JSPConnection(String path) throws Exception {
        String[] urlParts = path.split("/");
        String amAlive = "http://" + urlParts[0] + "/amAlive/amAlive.html";
        if (!isAvailable(new URL(amAlive)))
            throw new Exception("Unable to check if server is alive on " + amAlive);
        this.codeBase = new URL("http://" + path);
    }

    boolean isAvailable(URL url){
        boolean retVal = false;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(10000); // Timeout 10 seconds
            connection.connect();
            // If the web service is available
            if (connection.getResponseCode() == 200) {
                retVal = true;
            }
            connection.disconnect();
        } catch (Exception e) {
            ;
        }
        return retVal;
    }

    public ErrorStatAndMsg getData(String jspPath, Hashtable<String, String> query) {
        boolean inError = true;
        String msg = "";
        if (allOK) {
            UrlWithStatus urlWithStatus = getURL(jspPath, query);
            if (urlWithStatus.ok) {
                URL url = urlWithStatus.url;
//                trace("JSPConnection.48: url = " + url);
                try {
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(url.openStream()));
                    StringBuilder data = new StringBuilder();
                    String line;
                    try {
                        try {
                            while ((line = reader.readLine()) != null)
                                data.append(line);
                            msg = data.toString();
                            inError = false;
                        } catch (IOException e) {
                            msg = "Reading data lines: " + e.getMessage();
                        } finally {
                            reader.close();
                        }

                    } catch (IOException e) {
                        msg = "Reading data: " + e.getMessage();
                    } finally {
                        reader.close();
                    }
                } catch (IOException e) {
                    msg = "Opening Reader: " + e.getMessage();
                }
            } else
                msg = urlWithStatus.msg;
        } else
            msg = "jnlp Basic Service not available";
        return new ErrorStatAndMsg(inError, msg);
    }

    public ErrorStatAndMsg getData(String jspPath, String label, String value) {
        Hashtable<String, String> query  = new Hashtable<String, String>();
        query.put(label, value);
        return getData(jspPath, query);
    }

    public ErrorStatAndMsg getData(String jspPath) {
        Hashtable<String, String> query  = new Hashtable<String, String>();
        return getData(jspPath, query);
    }

    UrlWithStatus getURL(String jspPath, Hashtable<String, String> query) {
        UrlWithStatus retVal;
        try {
            StringBuilder queryStr = new StringBuilder("");
            boolean start = true;
            for (String key : query.keySet()) {
                if (start) {
                    queryStr.append("?");
                    start = false;
                }
                else
                    queryStr.append("&");
                queryStr.append(key + "=" + URLEncoder.encode(query.get(key), "utf-8"));
            }
            URL url = new URL("" + codeBase + jspPath + queryStr);
//            System.out.println("[" + "" + codeBase + jspPath + queryStr+ "]");
            retVal = new UrlWithStatus(url);
        } catch (MalformedURLException e) {
            retVal = new UrlWithStatus(e.getMessage());
        }
        catch (UnsupportedEncodingException e) {
            retVal = new UrlWithStatus(e.getMessage());
        }
        return retVal;
    }

    class UrlWithStatus {
        URL url;
        String msg = "";
        boolean ok = true;

        UrlWithStatus(URL url) {
            this.url = url;
        }

        UrlWithStatus(String msg) {
            this.ok = false;
            this.msg = msg;
        }
    }

    void trace(String msg) {
        System.out.println(msg);
    }
}
