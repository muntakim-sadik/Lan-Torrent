/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.io.*;
import java.util.*;


public class ConfigFile implements Serializable{
    ArrayList<FileStructure> ary;
    ConfigFile(ArrayList<FileStructure> x){
        ary = x;
    }
}
