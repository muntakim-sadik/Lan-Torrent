/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.util.HashMap;
import java.util.*;
import java.io.*;
import lantorrent.*;
import javax.swing.*;
/**
 *
 * @author paagol
 */
public class ClassTestClient {
    public static void main(String args[]){
        HashMap <String, ArrayList<Range>> x = new HashMap();;
        ArrayList<Range> r = new ArrayList();
        r.add(new Range(0,22038400,true));
        r.add(new Range(22038400,44076800,true));
        r.add(new Range(44076800,66115196,true));
        x.put("file.mp4", r);
        
//        (new Thread(new Server(x))).start();
       
        ///////////for running the client
//        ArrayList<Range> toGet = new ArrayList();
//        toGet.add(new Range(0,22038400));
//        toGet.add(new Range(22038400,44076800));
//        toGet.add(new Range(44076800,66115196));
//        FileStructure fs = new FileStructure("file.mp4",66115196,3,22038400,toGet,"127.0.0.1",10000);
//        Client cc = new Client(fs);
//        ArrayList<QueryData> qq = new ArrayList();
//        qq.add(new QueryData("127.0.0.1",10000,r));
//        cc.q = (ArrayList)qq.clone();
//        cc.startProcessing();
    }
}
