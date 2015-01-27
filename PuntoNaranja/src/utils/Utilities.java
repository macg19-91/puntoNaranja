
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import org.w3c.dom.Document;

/**
 *
 * @author rlobo
 */
public class Utilities {
    String host = "190.26.241.209";
    int port = 7001;
    private ObjectOutputStream outputStream = null;
    private ObjectInputStream inputStream = null;
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
        Document docu;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(doc);
        //send msg to server
        socket.shutdownOutput();
        while(true){
            if(socket.getInputStream() != null){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                docu = (Document) ois.readObject();
                break;
            }
        }
        return docu;
    }
    
    public String SendToServerXML(String doc) throws Exception{
        //create output stream attached to socket
        String docu;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(doc);
        //send msg to server
        socket.shutdownOutput();
        while(true){
            if(socket.getInputStream() != null){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                docu = (String) ois.readObject();
                break;
            }
        }
        return docu;
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
