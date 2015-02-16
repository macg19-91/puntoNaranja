
package utils;

import java.io.BufferedReader;
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
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;

/**
 *
 * @author rlobo
 */
public class Utilities {
    String host = "190.26.241.209";
    int port = 7001;
    Socket socket = null;
    
    public Utilities(String _host, int _port) throws Exception{
        host = _host;
        port = _port;
        socket = new Socket(host, port);
    }
    
    public Utilities() throws Exception{
        socket = new Socket(host, port);
    }
    
    public Map<String, String> SendToServer(String msg) throws Exception{
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
    
    
    public void open() throws IOException{
        socket.close();
    }
    
    public void close() throws IOException{
        socket.close();
    }
}
