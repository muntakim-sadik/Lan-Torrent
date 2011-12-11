/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lantorrent;

import java.io.*;
import java.util.*;

public class FileStreamManager {
    static HashMap<String,RandomAccessFile> map = new HashMap();
    
    synchronized static RandomAccessFile getStream(String s){
        RandomAccessFile f = map.get(s);
        if(f==null){
            try{
                f = new RandomAccessFile(s,"rw");
            }catch(IOException e){
                e.printStackTrace();
            }
            map.put(s, f);
        }
        
        return f;
    }

    static void writeToFile(RandomAccessFile rf,byte[] ary, int length, long startPos){
        synchronized(rf){
            try{
                rf.write(ary,(int)startPos,length);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    static int readFromFile(RandomAccessFile rf,byte[] ary,long startPos){
        int bytes=0;
        synchronized(rf){
            try{
                rf.seek(startPos);
                bytes = rf.read(ary);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
