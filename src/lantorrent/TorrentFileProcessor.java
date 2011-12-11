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


public class TorrentFileProcessor {
    FileStructure struct;
    File f;
    String defaultTrackerIP="localhost";
    int defaultTrackerPort = 10000;

    TorrentFileProcessor(String s){
        f = new File(s);
    }

    /// here argument f is the File instance to save the torrent file
    void makeTorrentFile(File f) throws IOException{
        ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(f));
        ArrayList<Range> x = new ArrayList();
        int segSize;
        if(f.length()<=5*1024*1024) //if less than 5MB transfer as whole
            segSize =5*1024*1024;
        else if(f.length()<=1024*1024*1024) //if less than 1GB trasfer as segments of 512kb each
                segSize =10*1024*1024;
        else segSize = 20*1024*1024; // else transfer as segments of 4Mb each;

        int tot = (int)Math.ceil((double)f.length()/segSize);
        for(long i=0;i<tot-1;i++)
            x.add(new Range(i,(i+1)*segSize-1));
        if(!(f.length()%tot==0))
            x.add(new Range(x.get(x.size()).to+1,f.length()));

        struct = new FileStructure(f.getName(),f.length(),tot,segSize,x,defaultTrackerIP,defaultTrackerPort);

        oout.writeObject(struct);
        oout.close();
    }

    /* return the list of segments of the file initialized with "has" signal marked as false*/
    FileStructure process() throws IOException,ClassNotFoundException {
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(f));
        struct = (FileStructure)oin.readObject();

        return struct;
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class FileStructure implements Serializable{
    String fileName;
    long fileSize; ///in bytes
    int totalSegments;
    int segmentSize;
    String trackerIP;
    int trackerPort;
    ArrayList<Range> segmentList;
    FileStructure(String s, long l, int i1, int i2,ArrayList<Range> r,String s2, int p){
        fileName = s;
        fileSize = l;
        totalSegments = i1;
        segmentSize = i2;
        segmentList = r;
        trackerIP = s2;
        trackerPort = p;
    }
}