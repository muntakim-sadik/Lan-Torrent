/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class FileSender extends Thread {

    DownloadRequest req;
    String fileName;
    Socket sock;
    ObjectInputStream oin;
    ObjectOutputStream oout;
    List pool;
    Server ss;

//    FileSender(List s) {
//        pool = s;
//    }
    FileSender(Socket s,Server x){
        sock = s;
        ss = x;
    }

    public void run() {
        ss.upCount();
//        if (Server.mark) {
//            return;
//        }
//        synchronized (pool) {
//            while (pool.isEmpty()) {
//                try {
//                    pool.wait();
//                } catch (InterruptedException e) {
//                }
//            }
//            sock = (Socket) pool.remove(pool.size() - 1);
//        }
        try {
            if(ss.getState()){
                try {
                    wait();
                } catch (InterruptedException ex) {
//                    Logger.getLogger(FileSender.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());

            req = (DownloadRequest) oin.readObject();
            System.out.println(req.fileName);
            System.out.println(req.rangeIndex);
            System.out.println(Server.map.get(req.fileName));
            
//            if (Server.map.get(req.fileName) == null || !Server.map.get(req.fileName).get(req.rangeIndex).has) { //if the file not found or the segment not found
//                oout.writeObject("NOT_FOUND");
//                
//                oout.flush();
//            } else {
                System.out.println("FOUND");
                long start = Server.map.get(req.fileName).get(req.rangeIndex).from, end = Server.map.get(req.fileName).get(req.rangeIndex).to  ;
                int size =(int) (end - start+1);
                System.out.println("1 "+size+" start "+start+ " ed "+end);
                byte ary[] = new byte[1024*100];
                for(long i = start;i<end;i+=1024*100){                    
                    FileSendingFormat ff = FileStreamManager.readFromFile(FileStreamManager.getStream(req.fileName),ary,i);
                    System.out.println("in sender "+(ff.from+ff.from));
                    oout.writeObject(ff);
                    oout.flush();
//                    if(ff.length<=0)
//                        break;
                }
//            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } finally {
            try {
                System.out.println("Closing connection.......");
                
                closeConnection();
                ss.downCount();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void closeConnection() throws IOException {
        oout.close();
        oin.close();
        sock.close();
    }
//    public static void main(String[] ag) {
//        ArrayList<Range> ar = new ArrayList<Range>();
//        ar.add(new Range(1, 1024 ));
//        ar.get(0).has = true;
//        HashMap<String, ArrayList<Range>> map = new HashMap<String, ArrayList<Range>>();
//        map.put("file.pdf", ar);
//        Server s = new Server(map);
//        (new Thread(s)).start();
//    }
}