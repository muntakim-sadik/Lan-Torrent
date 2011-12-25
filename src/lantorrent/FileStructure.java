/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.io.Serializable;
import java.util.ArrayList;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class FileStructure implements Serializable{
    String fileName;
    long fileSize; ///in bytes
    int totalSegments;
    int segmentSize;
    String trackerIP;
    int trackerPort;
    ArrayList<Range> segmentList;
    FileStructure(String s, long l, int i1, int i2,ArrayList<Range> r,String s2, int p){
        super();
        fileName = s;
        fileSize = l;
        totalSegments = i1;
        segmentSize = i2;
        segmentList = r;
        trackerIP = s2;
        trackerPort = p;
    }
}