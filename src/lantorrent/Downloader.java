/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Downloader extends Thread{
   // List pool;
    byte ary[];
    Object o;
    Socket sock;
    ObjectOutputStream oout;
    ObjectInputStream oin;
    pair<DownloadRequest,pair<String,Integer> > p;
    Client cc;
//    Downloader(List s){
//        pool =s;
//    }
    Downloader(pair<DownloadRequest,pair<String,Integer> > x, Client c){
        p = x;
        cc =c;
    }

/*********************************************/
    public void run(){
        cc.upCount();
//        synchronized(pool){
//            System.out.println(" "+pool.isEmpty());
//
//
//            while(pool.isEmpty()){
//                try {
//                    pool.wait();
//                } catch (InterruptedException ex) {
//                    
//                }
//            }
//            p = (pair)pool.remove(pool.size()-1);
//        }
        try{
               if(cc.getState()){
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
               
            
            sock = new Socket((String) ((pair)p.second).first, (Integer) ((pair)p.second).second);
            oout = new ObjectOutputStream(sock.getOutputStream());
            oout.flush();
            oin = new ObjectInputStream(sock.getInputStream());
            
            oout.writeObject((DownloadRequest)p.first);
            oout.flush();
            System.out.println("1 Read byte client");

            o = oin.readObject();
            
            if(o instanceof String){
                String s= (String )o;
                if(s.equals("NOT_FOUND")){
//                    synchronized(pool){
//                        pool.add(p);
//                        pool.notifyAll();
//                    }
                }
            }else{
                FileSendingFormat ff = (FileSendingFormat)o;
                System.out.println("in dnlder "+(ff.from+ff.length));
                while(ff.length>0){
                    FileStreamManager.writeToFile(FileStreamManager.getStream(((DownloadRequest)p.first).fileName),
                        ff);
                    ff = (FileSendingFormat)oin.readObject();
                }
            }
        }catch(EOFException eox){
            
        }catch (IOException ex) {
            ex.printStackTrace();
        }catch(ClassNotFoundException c){
            c.printStackTrace();
        }finally{
            try {
                oout.close();
                oin.close();
                sock.close();
                cc.downCount();
                cc.updateList(p.first.rangeIndex);
            } catch (IOException ex) {
               
            }
        }
    }
    
    
   
}

