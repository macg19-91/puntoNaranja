
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import org.w3c.dom.Document;

/**
 *
 * @author rlobo
 */
public class Utilities {
    String host = "190.26.241.209";
    int port = 7001;
    //int port = 7009;
    Socket socket = null;
    
    public Utilities(String _host, int _port) throws Exception{
        host = _host;
        port = _port;
        socket = new Socket(host, port);
    }
    
    public Utilities() throws Exception{
        socket = new Socket(host, port);
    }
    
    public Map<String, String> SendToServerNoSsl(String msg) throws Exception{
        //create output stream attached to socket
        String serverName = host;
        Map<String, String> result = new HashMap<String, String>();
        try
        {
           System.out.println("Connecting to " + serverName + " on port " + port);
           System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
           System.out.println("send "+ msg);
           InputStreamReader inputstreamreader = new InputStreamReader(socket.getInputStream());
           BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
           StringBuilder sb = new StringBuilder();
           //sb.append("<isomsg>\r\n<field id=\"0\" value=\"0800\" />\r\n<field id=\"3\" value=\"990000\" />\r\n<field id=\"11\" value=\"000001\" />\r\n<field id=\"41\" value=\"50600084\" />\r\n</isomsg>");
           sb.append(msg);
           PrintWriter printwriter = new PrintWriter(socket.getOutputStream(),true);
           printwriter.println(sb);
           String lineread = "";
           String[] parts;
            while ((lineread = bufferedreader.readLine()) != null){
                System.out.println(lineread);
                if(lineread.equals("</isomsg>")){
                    break;
                }
                if(!lineread.equals("<isomsg>")){
                    parts = lineread.split("id=\"");
                    String part1 = parts[1];
                    result.put(part1.split("\"")[0],part1.split("\"")[2]);
                }
            }
            
        }catch(IOException e)
        {
           e.printStackTrace();
        }
        return result;
    }
    
    public static void disableCertificateValidation() throws KeyManagementException, NoSuchAlgorithmException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
 
        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
      }
    
    public Map<String, String> SendToServerSSL(String msg) throws Exception{
        //disableCertificateValidation();
        Map<String, String> result = new HashMap<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;
        //SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
           //SSLSocket c = (SSLSocket) f.createSocket(host, port);
            SSLSocketFactory factory = null;
            KeyManager[] km = null;
            TrustManager[] tm = {new RelaxedX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(null, tm, new java.security.SecureRandom());
            factory = sslContext.getSocketFactory();
            SSLSocket c = (SSLSocket)factory.createSocket(host, port);
            String[] supportedCiphers = factory.getDefaultCipherSuites();
            System.out.println("Ciphers: " + supportedCiphers.length);
            for(int i = 0; i < supportedCiphers.length; i++)
            {
                System.out.println("  " + supportedCiphers[i]);
            }

            System.out.println(); 
           printSocketInfo(c);
           //c.startHandshake();
           BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
           SSLSession ss = c.getSession();
           System.out.println(msg);
           w.append(msg);
           System.out.println("send..");
           w.flush();
           //w.write(msg);
           //w.flush();
           BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
           String lineread = "";
           String[] parts;
           try{
                lineread = r.readLine();
           }
           catch(Exception e){
               System.err.println(e.toString());
           }
           System.out.println(lineread);
//            while ((lineread = r.readLine()) != null){
//                System.out.println(lineread);
//                if(lineread.equals("</isomsg>")){
//                    break;
//                }
//                if(!lineread.equals("<isomsg>")){
//                    parts = lineread.split("id=\"");
//                    String part1 = parts[1];
//                    result.put(part1.split("\"")[0],part1.split("\"")[2]);
//                }
//            }
           w.close();
           r.close();
           c.close();
        } catch (IOException e) {
           System.err.println(e.toString());
        }
        return result;
    }
    
    public  Map<String, String> SendToServer(String msg) {
        Map<String, String> result = new HashMap<>();
        try {

            org.com.ssl.MySSLSocketFactory sslsocketfactory = (org.com.ssl.MySSLSocketFactory) getSunJSSESocketFactory();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("190.26.241.209", 7009);
            sslsocket.setSoLinger(true, 0);
            sslsocket.setSoTimeout(60000);

            printSocketInfo(sslsocket);

            InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);
            System.out.println(msg);
            StringBuilder sb = new StringBuilder();
            sb.append(msg);
            String request = "<isomsg>\n\r"
                    + "  <field id=\"0\" value=\"0800\"/>\n\r"
                    + "  <field id=\"3\" value=\"990000\"/>\n\r"
                    + "  <field id=\"11\" value=\"123456\"/>\n\r"
                    + "  <field id=\"41\" value=\"00000002\"/>\n\r"
                    + "</isomsg>\n\r";
            bufferedwriter.write(msg);
            bufferedwriter.flush();

            String lineread = "";
            String[] parts;
            while ((lineread = bufferedreader.readLine()) != null){
                System.out.println(lineread);
                if(lineread.equals("</isomsg>")){
                    break;
                }
                if(!lineread.equals("<isomsg>")){
                    parts = lineread.split("id=\"");
                    String part1 = parts[1];
                    result.put(part1.split("\"")[0],part1.split("\"")[2]);
                }
            }

            outputstream.close();
            inputstream.close();
            sslsocket.close();

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    private static org.com.ssl.MySSLSocketFactory getSunJSSESocketFactory() {
        try {
            org.com.ssl.MySSLSocketFactory sf = new org.com.ssl.MySSLSocketFactory();
            //sf.setKeyStore("/recurso/mykeystore.keystore");
            //sf.setKeyStore("./keystore/mykeystore.keystore");
            sf.setKeyPassword("ganicu");
            sf.setPassword("ganicu1711");
            sf.setServerAuthNeeded(false);
            sf.setClientAuthNeeded(false);
            return sf;
        } catch (Exception ex) {
            return null;
        }

    }
    
    
    public void open() throws IOException{
        socket.close();
    }
    
    public void close() throws IOException{
        socket.close();
    }

    private void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: "+s.getClass());
        System.out.println("   Remote address = "
           +s.getInetAddress().toString());
        System.out.println("   Remote port = "+s.getPort());
        System.out.println("   Local socket address = "
           +s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
           +s.getLocalAddress().toString());
        System.out.println("   Local port = "+s.getLocalPort());
        System.out.println("   Need client authentication = "
           +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = "+ss.getCipherSuite());
        System.out.println("   Protocol = "+ss.getProtocol());
    }
}


class RelaxedX509TrustManager implements X509TrustManager {
     public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
     }
     public void checkClientTrusted(
          java.security.cert.X509Certificate[] chain, String authType) {}
     public void checkServerTrusted(
          java.security.cert.X509Certificate[] chain, String authType) {}
}
