/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lantorrent;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.sql.SQLException;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Tracker {

    RequestFormat requests;
    ServerSocket server;
    Socket sock;
    final int PORT = 10000;

    public void runTracker() {
        try {
            server = new ServerSocket(PORT, 100);
            while (true) {
                sock = server.accept();
                System.out.println("tracker-runtracker");
                Thread t = new Thread(new ConnectionProcessor(sock));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Tracker t = new Tracker();
        t.runTracker();
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class ConnectionProcessor implements Runnable {

    Socket tmp;
    ObjectInputStream in;
    ObjectOutputStream out;

    ConnectionProcessor(Socket s) {
        tmp = s;
    }

    public void run() {
        try {
            System.out.println("tracker-connectionprocessor-run1");
            out = new ObjectOutputStream(tmp.getOutputStream());
            out.flush();
            in = new ObjectInputStream(tmp.getInputStream());
            Object o = in.readObject();
            System.out.println("tracker-connectionprocessor-run2");

            // while(o instanceof RequestFormat){
            System.out.println("tracker-connectionprocessor-run3");
            lantorrent.RequestFormat r = (lantorrent.RequestFormat) o;
            if (r.updateDatabase) {
                Update u = new Update();
                System.out.println("trck " + tmp.getPort() + " pre " + u.preprocess(r.hasList) + " pre-pre " + r.hasList.size());

                u.Insert(tmp.getInetAddress().getHostAddress(), r.localPort, r.fileName, u.preprocess(r.hasList));
//                    Update u = new Update(tmp.getInetAddress().getHostAddress(),tmp.getPort(),r.fileName,r.hasList);
            } else {
                System.out.println("tracker-connectionprocessor-run4");
                Query q = new Query(r.fileName);
                ArrayList<QueryData> list = q.get();
                out.writeObject(list);
                out.flush();
            }
            o = in.readObject();
            // }
            // if(o instanceof String){
            String s = (String) o;
            if (o.equals("Success")) {
                in.close();
                out.close();
                tmp.close();    
                //}
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }
}
