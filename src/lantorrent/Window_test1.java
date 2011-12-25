package lantorrent;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.*;

public class Window_test1 extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JTable table_1;
    int selectedRow;
    int lselectedRow;
    ArrayList<FileStructure> fs;
    ArrayList<Thread> cl;
    final String configfileName = new String("config.ini");
    Thread serverThread;
    Server ss;
    
    final DefaultTableModel model = new DefaultTableModel(
                new Object[][]{}, new String[]{"File name", "Size",
                    "downloaded", "status"});
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    Window_test1 frame = new Window_test1();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Window_test1() {

        

        setBackground(SystemColor.controlHighlight);
        setTitle("Lantorrent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 614, 418);
        
        fs = new ArrayList(5);
        cl = new ArrayList(5);
        initAll();

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(SystemColor.controlHighlight);
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("File");
        mnNewMenu.setBackground(SystemColor.controlHighlight);
        menuBar.add(mnNewMenu);

        JMenuItem mntmNew = new JMenuItem("new");
        mntmNew.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JFileChooser jfc = new JFileChooser();
                        int x= jfc.showOpenDialog(Window_test1.this);
                        if(x==JFileChooser.APPROVE_OPTION){
                            File ff = jfc.getSelectedFile();
                            String ip="192.168.49.49";
                            int port =10000;
                            String nn ="t.t";
                            //String ip = JOptionPane.showInputDialog("Tracker's IP:");
                            //int port = Integer.parseInt(JOptionPane.showInputDialog("Tracker's port(Default 10000):"));
                            TorrentFileProcessor tf = new TorrentFileProcessor(ff,ip,port);
                            //String nn = JOptionPane.showInputDialog("Torrentfile's name:");
                            File f = new File(nn);
                            tf.makeTorrentFile(f);
                            JOptionPane.showMessageDialog(Window_test1.this, "File "+nn+" was saved");
                            trackerUpdate(f);
                            System.out.println("new finis "+ff.exists()+" "+ff.length());
                            model.addRow(new Object[]{ff.getName(),ff.length()/(1024*1024),"100%","Seeding"});
                        }
                    }
                });
        mnNewMenu.add(mntmNew);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mnNewMenu.add(mntmExit);
        mntmExit.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        closeAll();
                    }
                });

        JMenu mnNewMenu_1 = new JMenu("Help");
        mnNewMenu_1.setBackground(SystemColor.controlHighlight);
        menuBar.add(mnNewMenu_1);

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        JOptionPane.showMessageDialog(Window_test1.this, "Lan Torrent v11.12\nAuthor:Muntakim(0905003), Ahiqul Mostofa(0905024)\nLevel-2, term-1\nCSE, BUET");
                    }
                });
        mnNewMenu_1.add(mntmAbout);
        contentPane = new JPanel();
        contentPane.setForeground(Color.LIGHT_GRAY);
        contentPane.setBackground(SystemColor.controlHighlight);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
/////////////////////////////////////////////////////////////////////
        JButton addButton = new JButton("");
        addButton.setIcon(new ImageIcon(
                "./resource/ad.png"));
        addButton.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        JFileChooser jfc = new JFileChooser();
                        int ret = jfc.showOpenDialog(Window_test1.this);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File ff = jfc.getSelectedFile();
                            
                            fs.add(TorrentFileProcessor.process(ff));
                            
                            cl.add(new Thread(new Client(fs.get(fs.size()-1),ss,Window_test1.this)));
                            
                            model.addRow(new Object[]{fs.get(fs.size()-1).fileName,fs.get(fs.size()-1).fileSize/(1024*1024),"0%","Pending"});
                            
                            
                            
                        }
                    }
                });
        addButton.setBounds(12, 0, 42, 42);
        contentPane.add(addButton);
/////////////////////////////////////////////////////////////////////
        JButton deleteButton = new JButton("");

        deleteButton.setActionCommand("delete");

        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if ("delete".equals(e.getActionCommand())) {
                    if(selectedRow==-1){
                        JOptionPane.showMessageDialog(Window_test1.this,"Please select a file to remove");
                    }else{
                        String name= (String) model.getValueAt(selectedRow, 0);
                        int row =0;
                        for(int i=0;i<fs.size();i++){
                            if(fs.get(i).fileName.equals(name)){
                                row = i;
                                break;
                            }
                        }
                        cl.remove(row).interrupt();
                        fs.remove(row);
                        
                        model.removeRow(selectedRow);
                    }
                    
                }
            }
        });
        deleteButton.setIcon(new ImageIcon(
                "./resource/delete.png"));
        deleteButton.setBounds(54, 0, 42, 42);
        contentPane.add(deleteButton);
/////////////////////////////////////////////////////////////////////
        JButton pauseButton = new JButton("");
        pauseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        pauseButton.setIcon(new ImageIcon(
                "./resource/pause.png"));
        pauseButton.setBounds(96, 0, 42, 42);
        contentPane.add(pauseButton);
/////////////////////////////////////////////////////////////////////
        JButton playButton = new JButton("");
        playButton.setIcon(new ImageIcon(
                "./resource/play.png"));
        playButton.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        if(selectedRow==-1){
                            JOptionPane.showMessageDialog(Window_test1.this, "Please select a file to start");
                        }else{
                            String name= (String) model.getValueAt(selectedRow, 0);
                            int row =0;
                            for(int i=0;i<fs.size();i++){
                                if(fs.get(i).fileName.equals(name)){
                                    row = i;
                                    break;
                                }
                            }
                            cl.get(row).start();
                        }
                    }
                });
        playButton.setBounds(137, 0, 42, 42);
        contentPane.add(playButton);
/////////////////////////////////////////////////////////////////////
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setToolTipText("");
        scrollPane.setBounds(185, 0, 415, 360);
        contentPane.add(scrollPane);

        table = new JTable();

        table.setShowHorizontalLines(false);

        table.setModel(model);

       // model.addRow(new Object[]{"saeed", "99", "12", "ad"});
       // model.addRow(new Object[]{"saeed", "99", "12", "ad"});
        scrollPane.setViewportView(table);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(12, 46, 143, 82);
        //contentPane.add(scrollPane_1);

        table_1 = new JTable();
        table_1.setModel(new DefaultTableModel(new Object[][]{
                    {"All", null}, {"Downloading", null},
                    {"Downloaded", null},}, new String[]{"status", ""}) {

            Class[] columnTypes = new Class[]{String.class, Object.class};

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
            boolean[] columnEditables = new boolean[]{false, true};

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        table_1.getColumnModel().getColumn(1).setPreferredWidth(25);
        table_1.getColumnModel().getColumn(1).setMaxWidth(25);
        scrollPane_1.setViewportView(table_1);

        JSeparator separator = new JSeparator();
        separator.setBounds(121, 46, 0, 191);
        contentPane.add(separator);

        // /////////////////////////
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                   
                    selectedRow=-1;
                } else {
                    selectedRow = lsm.getMinSelectionIndex();
                    
                    // System.out.println("Row " + selectedRow
                    // + " is now selected.");
                    // model.removeRow(selectedRow);
                }
            }
        });

        // ////////////////////
        // /////////////////////////
        ListSelectionModel lrowSM = table_1.getSelectionModel();
        lrowSM.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                    return;
                }

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    System.out.println("No rows are selected.");
                } else {
                    lselectedRow = lsm.getMinSelectionIndex();
                    // System.out.println("lRow " + lselectedRow
                    // + " is now selected.");
                    // model.removeRow(selectedRow);
                }
            }
        });

        // ////////////////////

        // ///////////////////////////

    }
    
    void initAll(){
        try {
            File ff = new File(configfileName);
            if(ff.exists()){
                ObjectInputStream oin= new ObjectInputStream(new FileInputStream(configfileName));
                ConfigFile cf;
                cf = (ConfigFile)oin.readObject();
                fs = cf.ary;
                System.out.println("INITIALIZED ");
                System.out.println(fs.size());
            }
            ss = new Server(fs);
            serverThread = new Thread(ss);
            serverThread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    void closeAll(){
        ObjectOutputStream oout = null;
        try {
            ConfigFile cf = new ConfigFile(fs);
            oout = new ObjectOutputStream(new FileOutputStream(configfileName));
            oout.writeObject(cf);
           for(int i=0;i<cl.size();i++){
               cl.get(i).interrupt();               
           }
           serverThread.interrupt();
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oout.close();
            } catch (IOException ex) {
                ex.printStackTrace();
//                Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         System.exit(0);
    }
    
    void trackerUpdate(File ff){
        try {
            FileStructure fs = TorrentFileProcessor.process(ff);
            RequestFormat rf= new RequestFormat(fs.fileName,fs.segmentList,true);
            rf.setPort(9999);
            Socket sock = new Socket(fs.trackerIP,fs.trackerPort);
            ObjectOutputStream oout = new ObjectOutputStream(sock.getOutputStream());
            System.out.println("paise "+rf.hasList.size()+"  fs "+fs.segmentList.size());
            oout.flush();
            oout.writeObject(rf);
            oout.flush();
            oout.writeObject("Success");
            oout.flush();
            oout.close();
            sock.close();
            ss.refreshList(fs);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
//            Logger.getLogger(Window_test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void UpdateTable(String fn, Double per){
        for(int i=0;i<model.getRowCount();i++){
            if(model.getValueAt(i, 0).equals(fn)){
                model.setValueAt(101-per.intValue(),i, 2);
                model.fireTableDataChanged();
            }
        }
    }
    
}
