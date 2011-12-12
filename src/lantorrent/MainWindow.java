package lantorrent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class MainWindow extends JFrame {

	private JPanel contentPane;
        public static boolean stopServices = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton addButton = new JButton("");
		addButton.setIcon(new ImageIcon(MainWindow.class.getResource("/resource/ad.png")));
		toolBar.add(addButton);
		
		JButton removeButton = new JButton("");
		removeButton.setIcon(new ImageIcon(MainWindow.class.getResource("/resource/delete.png")));
		toolBar.add(removeButton);
		
		JButton playButton = new JButton("");
		playButton.setIcon(new ImageIcon(MainWindow.class.getResource("/resource/play.png")));
		toolBar.add(playButton);
		
		JButton pauseButton = new JButton("");
		pauseButton.setIcon(new ImageIcon(MainWindow.class.getResource("/resource/pause.png")));
		toolBar.add(pauseButton);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		String[] leftList={"Downloaded","Paused","Downloading"};
		JList lList = new JList(leftList);
		lList.setVisibleRowCount(10);
		leftPanel.add(lList);
	
		contentPane.add(leftPanel, BorderLayout.WEST);	
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		contentPane.add(rightPanel, BorderLayout.CENTER);
		
		
		JList fileList = new JList();
		
		rightPanel.add(fileList);
		
	}

}
