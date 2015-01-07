/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author rlobo
 */
public class utils {
        //String name="";
        String host = "localhost";
        int port = 6789;
        Socket socket = null;
        public static void main(String args[]) throws Exception{
//            Example of how to call the send and recieve functions
//            utils client = new utils();
//            client.SendToServer("Hey dude 1");
//            System.out.println("Server Said(1): "+client.RecieveFromServer());
//            client.close();
        }

        utils(String _host, int _port) throws Exception{
            host = _host;
            port = _port;
            socket = new Socket(host, port);
        }
        utils() throws Exception{
            socket = new Socket(host, port);
        }
        void SendToServer(String msg) throws Exception{
            //create output stream attached to socket
            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            //send msg to server
            outToServer.print(msg + '\n');
            outToServer.flush();
            socket.shutdownOutput();
        }
        String RecieveFromServer() throws Exception{
            //create input stream attached to socket
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            //read line from server
            String res = inFromServer.readLine(); // if connection closes on server end, this throws java.net.SocketException 
            return res;
        }
        void close() throws IOException{
            socket.close();
        }
}
