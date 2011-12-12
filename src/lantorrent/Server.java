package lantorrent;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
    ServerSocket server;
    final static int SERVER_PORT = 6000;
    Socket sock;
    static HashMap<String, ArrayList<Range> > map; // here string is the filename and the list is the ranges of the downloaded parts
    ObjectOutputStream oout;
    ObjectInputStream oin;
    final static int MAX_THREAD = 20;
    static boolean mark = false;
    Vector pool;
    FileSender[] fs;

    Server(HashMap<String, ArrayList<Range> > x){
        map = x;
    }

    void runServer(){
        fs = new FileSender[MAX_THREAD];
        for(int i=0;i<MAX_THREAD;i++)
            fs[i] = new FileSender(pool);
        try{
            server = new ServerSocket(SERVER_PORT,50);
            while(true && !lantorrent.MainWindow.stopServices){
                sock = server.accept();
                pool.add(sock);
                pool.notifyAll();
            }
            mark = true;
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            for(int i=0;i<MAX_THREAD;i++) // for safety stop all the threads
                fs[i].interrupt();
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class FileSender extends Thread{
    DownloadRequest req;
    String fileName;
    Socket sock;
    ObjectInputStream oin;
    ObjectOutputStream oout;
    List pool;

   FileSender(List s){
       pool = s;
   }

    public void run(){
        if(Server.mark)
            return;
        synchronized(pool){
            while(!pool.isEmpty()){
                try{
                    this.wait();
                }catch(InterruptedException e){

                }
            }
            sock =(Socket) pool.remove(pool.size()-1);
        }
        try{
            
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());

            req = (DownloadRequest) oin.readObject();
            if(Server.map.get(req.fileName)==null || !Server.map.get(req.fileName).get(req.rangeIndex).has){ //if the file not found or the segment not found
                oout.writeObject("NOT_FOUND");
                oout.flush();
            }else{
                int size = (int)(Server.map.get(req.fileName).get(req.rangeIndex).to-Server.map.get(req.fileName).get(req.rangeIndex).from+1);
                byte[] ary = new byte[size];
                while(FileStreamManager.readFromFile(null, ary, Server.map.get(req.fileName).get(req.rangeIndex).from)!=size) ; //check whether desired number of bytes have been read
                   
                oout.write(ary);
                oout.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException c){
            c.printStackTrace();
        }finally{
            try{
                closeConnection();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    void closeConnection() throws IOException{
        oout.close();
        oin.close();
        sock.close();
    }

}