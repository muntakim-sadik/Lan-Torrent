package lantorrent;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable{

    ServerSocket server;
    final static int SERVER_PORT = 9999;
    Socket sock;
    static HashMap<String, ArrayList<Range>> map; // here string is the filename and the list is the ranges of the downloaded parts
    ObjectOutputStream oout;
    ObjectInputStream oin;
    final static int MAX_THREAD = 500;
    static int threadCount = 0;
    static boolean mark = false;

    Server(HashMap<String, ArrayList<Range>> x) {
        map = x;
    }
    
    Server(ArrayList<FileStructure> fs){
        map = new HashMap();
        for(int i=0;i<fs.size();i++){
            map.put(fs.get(i).fileName, fs.get(i).segmentList);
        }
    }
    
    
    void refreshList(FileStructure fs){
        synchronized(map){
            map.remove(fs.fileName);
            map.put(fs.fileName, fs.segmentList);
        }
    }

    public void run() {
        try {
            server = new ServerSocket(SERVER_PORT, 50);
            System.out.println("Server started");
            while (true && !lantorrent.MainWindow.stopServices) {
                System.out.println("server-run-before-accept\n");
                sock = server.accept();
                System.out.println("server-run-after-accept\n");
                (new FileSender(sock,this)).start();
            }
            mark = true;
            System.out.println("hic version 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    synchronized void upCount(){
       threadCount++;
   }
   
   synchronized void downCount(){
       threadCount--;
       notify();
   }
   synchronized boolean getState(){
       return threadCount>=MAX_THREAD;
   }
}


