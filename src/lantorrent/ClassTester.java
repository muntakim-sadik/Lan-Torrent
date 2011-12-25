/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.util.HashMap;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lantorrent.*;
import javax.swing.*;
/**
 *
 * @author paagol
 */
public class ClassTester extends JFrame{
    public static void main(String args[]){
        
//        //////////for testing torrent files
//        TorrentFileProcessor tf = new TorrentFileProcessor("file.mp4","127.0.0.1",10000);
//        tf.makeTorrentFile(new File("xx.trnt"));
//        //try {
//            FileStructure fs = tf.process(new File("xx.trnt"));
//            System.out.print(fs.fileName+" "+fs.fileSize+" "+fs.segmentSize+" "+fs.segmentSize+" "+fs.trackerIP);
//            for(int i=0;i<fs.segmentList.size();i++)
//                System.out.println(" "+fs.segmentList.get(i).from+" "+fs.segmentList.get(i).to);
//    //        ////////for running the server
//    //        HashMap <String, ArrayList<Range>> x = new HashMap();;
    //        ArrayList<Range> r = new ArrayList();
    //        r.add(new Range(0,22038400,true));
    //        r.add(new Range(22038400,44076800,true));
    //        r.add(new Range(44076800,66115196,true));
    //        x.put("file.mp4", r);
    //        
    //        (new Thread(new Server(x))).start();
    //       
    //        ///////////for running the client
    //        ArrayList<Range> toGet = new ArrayList();
    //        toGet.add(new Range(0,102400));
    //        toGet.add(new Range(102400,204800));
    //        toGet.add(new Range(204800,307200));
    //        FileStructure fs = new FileStructure("file.pdf",307200,3,102400,toGet,"127.0.0.1",10000);
    //        Client cc = new Client(fs);
    //        ArrayList<QueryData> qq = new ArrayList();
    //        qq.add(new QueryData("127.0.0.1",10000,r));
    //        cc.startProcessing();
    //        cc.startProcessing();
//        } catch (IOException ex) {
//            Logger.getLogger(ClassTester.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ClassTester.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        

    }
}
