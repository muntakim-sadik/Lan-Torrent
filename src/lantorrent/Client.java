package lantorrent;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;

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
 */
class SegmentDownloader{
    ArrayList<Range> toDownload;
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

}


////////////////////////////////////////////////////////////////////////////////////////////////////
/// class to be written is to download a segment avaiable in the pool