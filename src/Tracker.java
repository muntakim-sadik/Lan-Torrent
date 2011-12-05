/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Tracker;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.sql.SQLException;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Tracker {
    RequestFormat requests;
    ServerSocket server;
    Socket sock;
    final int PORT = 10000;
   
    public void runTracker(){
        try{
            server = new ServerSocket(PORT,100);
            while(true){
                sock = server.accept();
                Thread t = new Thread(new ConnectionProcessor(sock));
                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class ConnectionProcessor implements Runnable{
    Socket tmp;
    ObjectInputStream in;
    ObjectOutputStream out;

    ConnectionProcessor(Socket s){
        tmp =s;
    }

    public void run(){
        try{
            out = new ObjectOutputStream(tmp.getOutputStream());
            out.flush();
            in = new ObjectInputStream(tmp.getInputStream());
            RequestFormat r =(RequestFormat) in.readObject();
            if(r.updateDatabase){
                Update u = new Update(tmp.getInetAddress().getHostAddress(),tmp.getPort(),r.fileName,r.hasList);
            }else{
                Query q = new Query(r.fileName);
                ArrayList <QueryData> list = q.get();
                out.writeObject(list);
                out.flush();
            }

        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException c){
            c.printStackTrace();
        }catch(SQLException s){
            s.printStackTrace();
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class RequestFormat implements Serializable{
    String fileName;
    boolean updateDatabase;
    ArrayList<Range> hasList;
    
    RequestFormat(String name, int size, ArrayList<Range> r ){
        fileName = name;
        hasList = r;
    }
}