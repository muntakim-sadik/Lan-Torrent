/*
// * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lantorrent;

import java.io.*;
import java.util.*;

public class FileStreamManager {
    public static void main(String[] a){
        byte[] b=new byte[1024*1024];
        
//        writeToFile(getStream("file.pdf"),b,1024*1024,0);
    }
            
    
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
     synchronized static RandomAccessFile getStream(String s,boolean x){
         return getStream("ff.mp4");
     }

    static void writeToFile(RandomAccessFile rf,FileSendingFormat ff){
        synchronized(rf){
            try{
                System.out.println(">>>>>"+ff.length+" >>>"+ff.ary.length);
                rf.seek(ff.from);
                rf.write(ff.ary,0,ff.length);
//                System.out.append(" "+)
            }catch(IOException e){
                e.printStackTrace();
            }catch(NullPointerException n){
                n.printStackTrace();
            }
//            System.out.println("rf "+rf.getFilePointer()+)
        }
    }

    static FileSendingFormat readFromFile(RandomAccessFile rf,byte[] ary,long startPos){
        int bytes=0;
        synchronized(rf){
            try{
                rf.seek(startPos);
                bytes = rf.read(ary,0,ary.length);
                System.out.println(">>>"+rf.getFilePointer()+" ff "+(startPos+bytes));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return new FileSendingFormat(ary,startPos,bytes);
    }
    
    
}
