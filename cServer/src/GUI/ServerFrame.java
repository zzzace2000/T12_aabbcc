package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import conn.cServer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ScrollPaneConstants;
import javax.swing.BoxLayout;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private JPanel logPanel;
	private JPanel guestPanel;
	private cServer theServer;
	private JTextPane textPane;
	private static final String TEXT_SUBMIT = "text-submit";
	private static final String INSERT_BREAK = "insert-break";

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ServerFrame(cServer ts) {
		// pass into var. Server
		theServer = ts;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(ServerFrame.class.getResource("/Icon/1394654416_MESSAGES.png")));
		setTitle("NMLAB 網多戰隊-team12 Server");
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		guestPanel = new JPanel();
		guestPanel.setForeground(Color.LIGHT_GRAY);
		guestPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		guestPanel.setBackground(Color.WHITE);
		guestPanel.setBounds(10, 10, 108, 342);
		contentPane.add(guestPanel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(139, 10, 410, 245);
		contentPane.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.add(scrollPane);
		
		logPanel = new JPanel();
		logPanel.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		logPanel.setBackground(Color.WHITE);
		logPanel.setLayout(new BoxLayout(logPanel, BoxLayout.Y_AXIS));
    	
		scrollPane.setViewportView(logPanel);
		
		final JButton btnNewButton = new JButton("");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				broadCast();
			}
		});
		btnNewButton.setEnabled(false);
		btnNewButton.setSelectedIcon(null);
		btnNewButton.setToolTipText("傳送");
		btnNewButton.setIcon(new ImageIcon(ServerFrame.class.getResource("/Icon/tosend.png")));
		btnNewButton.setBounds(469, 265, 80, 87);
		contentPane.add(btnNewButton);
		
		textPane = new JTextPane();
		InputMap input = textPane.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(enter, TEXT_SUBMIT);
		input.put(shiftEnter, INSERT_BREAK);
		
		ActionMap action = textPane.getActionMap();
		action.put(TEXT_SUBMIT, new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				broadCast();
			}
		});
		
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(textPane.getText().length() != 0) {
					btnNewButton.setEnabled(true);
				}
				else
					btnNewButton.setEnabled(false);
			}
		});
		textPane.setBounds(139, 265, 320, 87);
		contentPane.add(textPane);
	}
	
	private void broadCast() {
		String broadCastMsg = textPane.getText();
		if (!broadCastMsg.isEmpty()) {
			theServer.broadcast(0, "server", broadCastMsg);
			textPane.setText("");
			log(broadCastMsg);
		}
	}
	
	
	// ======== Display ========== //
	// Display words in the main screen
    public synchronized void disTxt (String from, String msg) {
        //JTextPane tmp = new JTextPane();
        JLabel tmp = new JLabel();
        tmp.setText(from + ":" + msg);
        logPanel.add(tmp);
        logPanel.validate();
    }
    
    public synchronized void log(String text) {
    	JLabel tmp = new JLabel();
    	String labelText = String.format("<html><p style=\"width:%dpx; word-wrap:break-all;\">%s</p></html>", 300, text);
    	tmp.setText(labelText);
    	tmp.setFont(new Font("Calibri", Font.PLAIN, 16));
        
        logPanel.add(tmp);
        logPanel.validate();
    }
    
    
    // Display friends in the left panel
    public synchronized void disFrd (String name) {
    	JLabel tmp = new JLabel(name);
    	tmp.setIcon(new ImageIcon(ServerFrame.class.getResource("/Icon/onlineGreenlignt.png")));
    	guestPanel.add(tmp);
    	guestPanel.validate();
                    
    }
    
	public JPanel getLogPanel() {
		return logPanel;
	}
	public JPanel getGuestPanel() {
		return guestPanel;
	}
	public JTextPane getTextPane_1() {
		return textPane;
	}
}
