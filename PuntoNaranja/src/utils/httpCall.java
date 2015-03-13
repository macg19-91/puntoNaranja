/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONObject;

/**
 *
 * @author rlobo
 */
public class httpCall {
    private JSONObject obj;
    

    public httpCall() {
//        try {
//            call(type);
//        } catch (IOException ex) {
//            Logger.getLogger(httpCall.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
    }
    
    public JSONObject call(String type) throws IOException{
        String content = "";
        try {
            URL data = new URL("http://66.165.180.245:8029/"+type+".json");
            
            URLConnection dataConnection = data.openConnection();

            // Find out charset, default to ISO-8859-1 if unknown
            String charset = "UTF-8";
            String contentType = dataConnection.getContentType();
            if (contentType != null) {
                int pos = contentType.indexOf("charset=");
                if (pos != -1) {
                    charset = contentType.substring(pos + "charset=".length());
                }
            }

            // Create reader and read string data
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(dataConnection.getInputStream(), charset));
            String line;
            while ((line = r.readLine()) != null) {
                content += line + "\n";
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(httpCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new JSONObject(content);
    }

}
