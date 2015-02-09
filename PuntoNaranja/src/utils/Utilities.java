
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
    
    public Document SendToServer(Document doc) throws Exception{
        //create output stream attached to socket
        String serverName = host;
        try
        {
           System.out.println("Connecting to " + serverName + " on port " + port);
           System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
           InputStreamReader inputstreamreader = new InputStreamReader(socket.getInputStream());
           BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
           StringBuilder sb = new StringBuilder();
           sb.append("<isomsg>\r\n<field id=\"0\" value=\"0800\" />\r\n<field id=\"3\" value=\"990000\" />\r\n<field id=\"11\" value=\"000001\" />\r\n<field id=\"41\" value=\"50600084\" />\r\n</isomsg>");
           PrintWriter printwriter = new PrintWriter(socket.getOutputStream(),true);
           printwriter.println(sb);
           String lineread = "";
           lineread = bufferedreader.readLine();
//           System.out.println("Received from Server: " + lineread);
            while ((lineread = bufferedreader.readLine()) != null){
              System.out.println("entra");
              System.out.println("Received from Server: " + lineread);
            }
            
        }catch(IOException e)
        {
           e.printStackTrace();
        }
        return null;
    }
    
//    public String SendToServerXML(String doc) throws Exception{
//        //create output stream attached to socket
//        String docu;
//        outputStream = new ObjectOutputStream(socket.getOutputStream());
//        outputStream.writeObject(doc);
//        //send msg to server
//        socket.shutdownOutput();
//        while(true){
//            if(socket.getInputStream() != null){
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                docu = (String) ois.readObject();
//                break;
//            }
//        }
//        return docu;
//    }
    
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    public String ReadBigStringIn(BufferedReader buffIn) throws IOException {
        StringBuilder everything = new StringBuilder();
        String line;
        while( (line = buffIn.readLine()) != null) {
           everything.append(line);
        }
        return everything.toString();
    }
    
    public Document RecieveFromServer() throws Exception{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Document doc = (Document) ois.readObject();
        return doc;
    }
    
    public void open() throws IOException{
        socket.close();
    }
    
    public void close() throws IOException{
        socket.close();
    }
}
