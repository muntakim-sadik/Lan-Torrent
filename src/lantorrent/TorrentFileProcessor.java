/**********************************************************************************
 * This class makes a torrent file for a given file class
 * or extracts data form a torrent file and returns a
 * FileStructure class which defines the fileName,
 * size, segment details
 * @completed
 */

package lantorrent;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TorrentFileProcessor {
    FileStructure struct;
    File f;
    String defaultTrackerIP="127.0.0.1";
    int defaultTrackerPort = 10000;

    TorrentFileProcessor(File ff,String ip, int port){
        f = ff;
        defaultTrackerIP = ip;
        defaultTrackerPort = port;
    }

    /// here argument p is the File instance to save the torrent file
    void makeTorrentFile(File p ) {
        ObjectOutputStream oout = null;
        try {
            oout = new ObjectOutputStream(new FileOutputStream(p));
            ArrayList<Range> x = new ArrayList();
            int segSize;
            if(f.length()<=5*1024*1024) //if less than 5MB transfer as whole
                segSize =(int)f.length();
            else if(f.length()<=1024*1024*1024) //if less than 1GB trasfer as segments of 10mb each
                    segSize =10*1024*1024;
            else segSize = 20*1024*1024; // else transfer as segments of 20Mb each;
            int tot = (int)Math.ceil((double)f.length()/segSize);
            if(tot==0) tot=1;
            long prev =0;
            for(long i=0;i<tot-1;i++){
                x.add(new Range(prev,(i+1)*segSize-1));
                prev = (i+1)*segSize;
            }
            if(!(f.length()%tot==0))
                x.add(new Range(x.get(x.size()-1).to+1,f.length()));
            struct = new FileStructure(f.getName(),f.length(),tot,segSize,x,defaultTrackerIP,defaultTrackerPort);
            oout.writeObject(struct);
            System.out.println("trnt prcr "+struct.totalSegments+" "+f.exists()+" "+segSize);
            oout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(TorrentFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oout.close();
            } catch (IOException ex) {
                ex.printStackTrace();
//                Logger.getLogger(TorrentFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /* return the list of segments of the file initialized with "has" signal marked as false*/
     static FileStructure process(File p) {
         FileStructure struc=null;
        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(p));
            struc = (FileStructure)oin.readObject();
            System.out.println("trnt prcr prcs "+struc.segmentList.size());
            oin.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(TorrentFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
//            Logger.getLogger(TorrentFileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return struc;
    }
}


