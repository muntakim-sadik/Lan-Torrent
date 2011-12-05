package lantorrent;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.sql.SQLException;

public class Client {
    Socket sock;
    ArrayList <Range> list;
    ArrayList <QueryData> q;
    String fileName;
    ObjectInputStream in;
    ObjectOutputStream out;
    final String trackerIP;
    final int trackerPort;
    Client(String ip, int port){
        trackerIP = ip;
        trackerPort = port;
    }

    
    void getSeederList(String file){
        try{
            sock = new Socket(trackerIP, trackerPort);
            out = new ObjectOutputStream(sock.getOutputStream());
            out.flush();
            in = new ObjectInputStream(sock.getInputStream());
            
            RequestFormat r = new RequestFormat(file,list,false);
            out.writeObject(r);
            out.flush();
            q = (ArrayList<QueryData>)in.readObject();
            for(int i=0;i<q.size();i++){
                Update u = new Update(q.get(i).ip,q.get(i).port,file,q.get(i).segments);
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

    RequestFormat(String name, ArrayList<Range> r, boolean queryType ){
        fileName = name;
        hasList = r;
        updateDatabase = queryType;
    }
}