package lantorrent;

import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable {

    Socket sock;
    ObjectInputStream oin;
    ObjectOutputStream oout;
    FileStructure struct;
    ArrayList<QueryData> q;
    Downloader[] dl;
    final int Max_thread = 5;
    static int segCount = 0;
    HashMap<String, Boolean> map;
    static int threadCount = 0;
    final static int MAX_THREAD = 500;
    Server ss;
    final int totalSegments;
    Window_test1 wnd;

    /*********************************************/
    Client(lantorrent.FileStructure f, Server s,Window_test1 ww) {
        struct = f;
        q = new ArrayList<QueryData>();
        totalSegments = struct.totalSegments;
        ss = s;
        wnd = ww;
    }

    /*********************************************/
    public void run() {
        System.out.print("client-run\n");
        getSeederList();
        for (int i = 0; i < q.size(); i++) {
            System.out.println(q.get(i).ip);
            System.out.println(q.get(i).port);
            System.out.println(q.get(i).segments.get(0).from);
            System.out.println(q.get(i).segments.get(0).to);
        }
        startProcessing();
    }

    /*********************************************/
    void getSeederList() {
        try {


            sock = new Socket(struct.trackerIP, struct.trackerPort);
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());

            oout.writeObject(new RequestFormat(struct.fileName, struct.segmentList, false));
            oout.flush();
            System.out.println("Client-get-seederlist\n");
            q = (ArrayList<QueryData>) oin.readObject();

            oout.writeObject("Success");
//            oout.close();/****************************************/
//            oin.close();/****************************************/
//            sock.close();/////////////////

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

    }

    /***************************************/
    void startProcessing() {

        for (int i = 0; i < q.size(); i++) {
            for (int j = 0; j < q.get(i).segments.size(); j++) {
                System.out.println("i " + i + " j " + j + " " + q.get(i).segments.get(j).from + " " + struct.segmentSize + " " + struct.segmentList.size());
                if (struct.segmentList.get((int) q.get(i).segments.get(j).from / struct.segmentSize).has) {
                    continue;
                }

                pair<String, Integer> x = new pair(q.get(i).ip, q.get(i).port);
                pair<DownloadRequest, pair<String, Integer>> p = new pair(new DownloadRequest((int) q.get(i).segments.get(j).from / struct.segmentSize, struct.fileName), x);
                (new Downloader(p, this)).start();
            }
        }
        
        ////notify the tracker if all parts downloaded
        



//       dl = new Downloader[Max_thread];
//        for(int i=0;i<Max_thread;i++){
//            dl[i] = new Downloader(pool);
//            dl[i].start();
//            try {
//                dl[i].join();
//            } catch (InterruptedException ex) {
//                //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//       
//        for(int i=0; i<Max_thread;i++)
//            dl[i].interrupt();        
    }

    /*********************************************/
    synchronized void updateList(int index) {
        try {
            struct.segmentList.get(index).has = true;
            ss.refreshList(struct);
            segCount++;
            wnd.UpdateTable(struct.fileName, (double) totalSegments/segCount);
            sock = new Socket(struct.trackerIP, struct.trackerPort);
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());

            oout.writeObject(new RequestFormat(struct.fileName, struct.segmentList, false));
            oout.flush();
            
            oout.writeObject("Success");
            
            
            //if(segCount==totalSegments)
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    synchronized void upCount() {
        threadCount++;
    }

    synchronized void downCount() {
        threadCount--;
        notify();
    }

    synchronized boolean getState() {
        return threadCount >= MAX_THREAD;
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class DownloadRequest implements Serializable {

    int rangeIndex;
    String fileName;

    DownloadRequest(int x, String s) {
        rangeIndex = x;
        fileName = s;
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