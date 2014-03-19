package GUI;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.FlowLayout;
import java.awt.CardLayout;

import javax.swing.GroupLayout.Alignment;

import conn.cClient;

import java.awt.Component;
import java.awt.event.MouseAdapter;

public class mainFram extends JFrame {
	
	private cClient theClient;
	private JPanel contentPane;
	private JTextField textField_2;//enter txt
	private JButton btnNewButton_1;
	private JTabbedPane tabbedPane;
	private JLabel userNameLabel ;
	public JPanel allOnline;
    public JTextPane chatBoard = new JTextPane(); //lobby chat
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFram frame = new mainFram();
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
	public mainFram(cClient tc) {
		theClient = tc;
		setIconImage(Toolkit.getDefaultToolkit().getImage(mainFram.class.getResource("/Icon/1394654416_MESSAGES.png")));
		setTitle("NMLAB 網多戰隊-team12 聊天室");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 130, 362);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(130, 362));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 25, 130, 174);
		panel.add(scrollPane);
		
		JLabel label = new JLabel("所有在線");
		label.setBackground(Color.WHITE);
		scrollPane.setColumnHeaderView(label);
		
		allOnline = new JPanel();
		allOnline.setBackground(Color.WHITE);
		scrollPane.setViewportView(allOnline);
		allOnline.setLayout(new BoxLayout(allOnline, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 198, 130, 164);
		panel.add(scrollPane_1);
		
		JLabel label_1 = new JLabel("當前聊天室在線");
		scrollPane_1.setColumnHeaderView(label_1);
		
		JPanel thisChatroomOnline = new JPanel();
		thisChatroomOnline.setBackground(Color.WHITE);
		scrollPane_1.setViewportView(thisChatroomOnline);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(0, 0, 130, 24);
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean success;
				Vector<String> connection = conDialog.showDialog(mainFram.this, "連線");
				theClient.connectToServer((String)connection.get(0), Integer.parseInt((String)connection.get(1)), (String)connection.get(2));
				userNameLabel.setText((String)connection.get(2)); 
			}


		});
		btnNewButton.setToolTipText("連線");
		btnNewButton.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/connection.png")));
		panel_5.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abouts tmp = new abouts();
				tmp.setVisible(true);
			}
		});
		btnNewButton_3.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/about.png")));
		panel_5.add(btnNewButton_3);
		btnNewButton_3.setToolTipText("關於");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(128, 0, 456, 362);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setPreferredSize(new Dimension(456, 362));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 458, 269);
		panel_1.add(tabbedPane);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		tabbedPane.addTab("大廳", null, panel_3, null);
		tablabel main = new tablabel("大廳", panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);
		
		
		chatBoard.setEditable(false);
		scrollPane_2.setViewportView(chatBoard);
		tabbedPane.setTabComponentAt(0, main);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("", null, panel_4, null);
		JPanel addpan = new JPanel();
		addpan.addMouseListener(new addtab());
		JLabel addLab = new JLabel("+");
		addpan.add(addLab);
		tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel_4), addpan);
		panel_4.setLayout(new BorderLayout(0, 0));
		addpan.setToolTipText("開新聊天室");
		
		JButton btnCreatANew = new JButton("Create a new Chat room");
		btnCreatANew.addActionListener(new addtabButton());
		btnCreatANew.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/Chattmp.png")));
		btnCreatANew.setPreferredSize(new Dimension(80,80));
		panel_4.add(btnCreatANew);
		
		Component horizontalStrut = Box.createHorizontalStrut(109);
		panel_4.add(horizontalStrut, BorderLayout.WEST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(112);
		panel_4.add(horizontalStrut_1, BorderLayout.EAST);
		
		Component verticalStrut = Box.createVerticalStrut(80);
		panel_4.add(verticalStrut, BorderLayout.NORTH);
		
		Component verticalStrut_1 = Box.createVerticalStrut(71);
		panel_4.add(verticalStrut_1, BorderLayout.SOUTH);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 267, 456, 95);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		textField_2 = new JTextField();
		textField_2.setBounds(10, 36, 377, 49);
		panel_2.add(textField_2);
		textField_2.setColumns(10);
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/tosend.png")));
		btnNewButton_1.setBounds(397, 36, 49, 49);
		panel_2.add(btnNewButton_1);
                btnNewButton_1.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println(textField_2.getText());
                        //chatBoard.replaceSelection("hello!!");
                        theClient.sendPMsg("/t " +textField_2.getText());
                        
                    }
                });
                
                
                
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(37, 9, 42, 23);
		panel_2.add(comboBox);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setBounds(10, 9, 29, 23);
		panel_2.add(btnNewButton_2);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(89, 11, 1, 21);
		panel_2.add(separator);
		
		JButton stickers = new JButton("strickers");
		stickers.setBounds(100, 9, 84, 23);
		panel_2.add(stickers);
		
		userNameLabel = new JLabel("");
		userNameLabel.setBounds(341, 9, 105, 17);
		panel_2.add(userNameLabel);
		
		textField_2.addKeyListener(new checktypeListener());
	}
        
         //////display///////
        //display chat conten
        public void disTxt (String from, String msg) {
            //chatBoard.replaceSelection("hello I'm happy");
            chatBoard.replaceSelection(from + " " + msg + "\n");
        }
        //display friends 
        public void disFrd (String name) {
			allOnline.add(new onlineLabel(name));
        }
	public static void showAlertDialog(String alertMsg) {
		// Write a pump up dialog that shows some alerts
		System.out.println("Alert: "+alertMsg);
	}
	
	
	class checktypeListener extends KeyAdapter{
		public void keyReleased(KeyEvent e){
			if(textField_2.getText().length() != 0)
				btnNewButton_1.setEnabled(true);
			else
				btnNewButton_1.setEnabled(false);
		}
	}
	class onlineLabel extends JLabel implements MouseListener{
		private privateMessage PM;
		onlineLabel(String name){
			setIcon(new ImageIcon(mainFram.class.getResource("/Icon/onlineGreenlignt.png")));
			setText(name);
			setToolTipText("double click to sned prvate message");
			addMouseListener(this);
		}
		public void mouseClicked(MouseEvent arg0) {
			if(arg0.getClickCount() == 2)
					PM = new privateMessage();
					PM.setVisible(true);
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class tablabel extends JPanel implements ActionListener{
		JButton exit;
		JLabel tabname;
		JPanel owner;
		
		tablabel(String name, JPanel tmp){
			owner = tmp;
			tabname = new JLabel(name);
			exit = new JButton(new ImageIcon(mainFram.class.getResource("/Icon/tabcancel.png")));
			exit.setPreferredSize(new Dimension(8,8));
			exit.addActionListener(this);
			if(name == "大廳") exit.setEnabled(false);
			add(tabname);
			add(exit);
		}
		public void actionPerformed(ActionEvent e){
			tabbedPane.remove(tabbedPane.indexOfComponent(owner));
		}
	}
	class addtab implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			Vector connection = newChatroom.showDialog(mainFram.this, "Create new room");
			JPanel tmpPan = new JPanel();
			tablabel tmp = new tablabel((String)connection.get(0), tmpPan);
			tabbedPane.add(tmpPan, tabbedPane.getTabCount()-1);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(tmpPan), tmp);
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}

	}
	class addtabButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Vector connection = newChatroom.showDialog(mainFram.this, "Create new room");
			JPanel tmpPan = new JPanel();
			tablabel tmp = new tablabel((String)connection.get(0), tmpPan);
			tabbedPane.add(tmpPan, tabbedPane.getTabCount()-1);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(tmpPan), tmp);
		}
	}
}

