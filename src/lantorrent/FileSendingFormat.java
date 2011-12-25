/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.io.*;


public class FileSendingFormat implements Serializable{
    byte[] ary;
    long from;
    int length;
    FileSendingFormat(byte[] a, long f, int l){
        super();
        ary = a.clone();
        from = f;
        length = l;
    }
}
