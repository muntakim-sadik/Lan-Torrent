package lantorrent;

import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{
    Socket sock;
    ObjectInputStream oin;
    ObjectOutputStream oout;
    static lantorrent.FileStructure struct;
    ArrayList<QueryData> q;
    Vector pool;
    Downloader[] dl;
    final int Max_thread = 5;
    static int segCount=0;
    HashMap<String,Boolean> map;

/*********************************************/
    Client(lantorrent.FileStructure f ){
        struct = f;
    }

    
/*********************************************/
    public void run(){
        getSeederList(); 
        startProcessing();
    }

/*********************************************/
    void getSeederList(){
        try{
            sock = new Socket(struct.trackerIP, struct.trackerPort);
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());

            oout.writeObject(new RequestFormat(struct.fileName,struct.segmentList,false));
            oout.flush();

            q = (ArrayList<QueryData>) oin.readObject();

            oout.writeObject("Success");
            sock.close();/////////////////
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException c){
            c.printStackTrace();
        }
       
    }
/***************************************/
    void startProcessing(){
        dl = new Downloader[Max_thread];
        for(int i=0;i<Max_thread;i++){
            dl[i] = new Downloader(pool);
        }
        
       while(true){
            for(int i=0;i<q.size();i++){
                for(int j=0;j<q.get(i).segments.size();i++){
                    if(struct.segmentList.get((int)q.get(i).segments.get(j).from/struct.segmentSize).has)
                        continue;
                    pool.add(new pair(new DownloadRequest((int)q.get(i).segments.get(j).from/struct.segmentSize,struct.fileName),new pair(q.get(i).ip,q.get(i).port)));
                    ///pair(downrqst() , pair(ip,port));
                    pool.notifyAll();
                }
            }
            
            if(segCount==struct.totalSegments) break;
            /////update the list
            getSeederList();
        }

        for(int i=0; i<Max_thread;i++)
            dl[i].interrupt();        
    }

/*********************************************/
    static synchronized void updateList(int index){
        struct.segmentList.get(index).has = true;
    }

}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class RequestFormat implements Serializable {

    String fileName;
    boolean updateDatabase;///true for update request, false for seederlist query
    ArrayList<Range> hasList;

    RequestFormat(String name, ArrayList<Range> r, boolean queryType) {
        fileName = name;
        hasList = r;
        updateDatabase = queryType;
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class DownloadRequest implements Serializable{
    int rangeIndex;
    String fileName;
    DownloadRequest(int x, String s){
        rangeIndex = x;
        fileName = s;
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class pair <E,X>{
    E first;
    X second;
    pair(E a, X b){
        first = a;
        second = b;
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Downloader extends Thread{
    List pool;
    byte ary[];
    Object o;
    
    Downloader(List s){
        pool =s;
    }

/*********************************************/
    public void run(){
        pair p;
        synchronized(pool){
            while(pool.isEmpty()){
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    
                }
            }
            p = (pair)pool.remove(pool.size()-1);
        }
        try {
            Socket sock = new Socket((String) ((pair)p.second).first, (Integer) ((pair)p.second).second);
            ObjectOutputStream oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            ObjectInputStream oin = new ObjectInputStream(sock.getInputStream());
            
            oout.writeObject((DownloadRequest)p.first);
            oout.flush();
            o = oin.readObject();
            if(o instanceof String){
                String s= (String )o;
                if(s.equals("NOT_FOUND")){
                    pool.add(p);
                    pool.notifyAll();
                }
            }else{
                ary = (byte[]) o;
                FileStreamManager.writeToFile(FileStreamManager.getStream(((DownloadRequest)p.first).fileName),
                        ary, ary.length, Client.struct.segmentList.get(((DownloadRequest)p.first).rangeIndex).from);
                Client.updateList(((DownloadRequest)p.first).rangeIndex);
            }
        }catch (IOException ex) {
            ex.printStackTrace();
        }catch(ClassNotFoundException c){
            c.printStackTrace();
        }
    }
}









































/*
public class Client implements Runnable{

    Socket sock;
    static ArrayList<Range> list;
    static int totalSegments = 0;
    ArrayList<QueryData> q;
    String fileName;
    ObjectInputStream in;
    ObjectOutputStream out;
    final String trackerIP;
    final int trackerPort;
    HashMap<String,Integer> availability;
    List threads;
    final int MAX_THREAD = 20;

    Client(String ip, int port, String f) {
        trackerIP = ip;
        trackerPort = port;
        fileName = f;
    }
    
    public void run(){
        getSeederList(fileName);
        
    }

    void getSeederList(String file) {
        try {
            sock = new Socket(trackerIP, trackerPort);
            out = new ObjectOutputStream(sock.getOutputStream());
            out.flush();
            in = new ObjectInputStream(sock.getInputStream());

            RequestFormat r = new RequestFormat(file, list, false);
            out.writeObject(r);
            out.flush();
            q = (ArrayList<QueryData>) in.readObject();
            out.writeObject("Success");
            out.flush();
            for (int i = 0; i < q.size(); i++) {
                Update u = new Update(q.get(i).ip, q.get(i).port, file, q.get(i).segments);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    
}
*/






































/*////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class SegmentData{
    String ip;
    int port;
    SegmentData(String s, int i){
        ip = s;
        port = i;
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * Lots of work to do here
 * 
 * @author paagol
 *
class SegmentDownloader{
/*    ArrayList<Range> toDownload;
    final static int maxConnection = 5;
    static int connections=0;
    HashMap<Range,ArrayList<SegmentData> > list;
    SegmentDownloader(ArrayList<QueryData> q){
        toDownload = new ArrayList();
        list = new HashMap();
        for(int i=0;i<q.size();i++){
            for(int j=0;j<q.get(i).segments.size();j++){
                if(list.get(q.get(i).segments.get(j))==null){
                    list.put(q.get(i).segments.get(j),new ArrayList());
                }
                list.get(q.get(i).segments.get(j)).add(new SegmentData(q.get(i).ip,q.get(i).port));
            }  
        }
    }
    void startDownload(){
        while(Client.list.size()!=Client.totalSegments){
            for(int i=maxConnection;i<5;i++){
                
            }
        }
    }
*/



////////////////////////////////////////////////////////////////////////////////////////////////////
/// class to be written is to download a segment avaiable in the pool