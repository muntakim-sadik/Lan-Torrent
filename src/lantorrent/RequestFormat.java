/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.io.*;
import java.util.*;
/**
 *
 * @author sid
 */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class RequestFormat implements Serializable {

    public String fileName;
    public boolean updateDatabase;///true for update request, false for seederlist query
    public ArrayList<Range> hasList;
    int localPort;

    RequestFormat(String name, ArrayList<Range> r, boolean queryType) {
        super();
        fileName = name;
        hasList = (ArrayList<Range>)r.clone();
        updateDatabase = queryType;
    }
    
    void setPort(int x){
        localPort =x;
    }
    
}
