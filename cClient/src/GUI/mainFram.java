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
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class mainFram extends JFrame {
	
	private cClient theClient;
	private JPanel contentPane;
	private JTextField textField_2;//enter txt
	private JButton btnNewButton_1;
	private JTabbedPane tabbedPane;
	private JTextPane chatBoardPane;
	private JLabel userNameLabel ;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JPanel panel_2;
	public JPanel allOnline;
	Vector<privateMessage> PM = new Vector<privateMessage>();
        Vector<newChatroom> ChR = new Vector<newChatroom> ();
        
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
		
		int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
	    int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		scrollPane = new JScrollPane(v,h);
		scrollPane.setBounds(0, 25, 130, 174);
		panel.add(scrollPane);
		
		JLabel label = new JLabel("所有在線");
		label.setBackground(Color.WHITE);
		scrollPane.setColumnHeaderView(label);
		
		allOnline = new JPanel();
		allOnline.setBackground(Color.WHITE);
		scrollPane.setViewportView(allOnline);
		allOnline.setLayout(new BoxLayout(allOnline, BoxLayout.Y_AXIS));
		
		scrollPane_1 = new JScrollPane(v,h);
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
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//refresh the user in the specific room;
			}
		});
		tabbedPane.setBounds(0, 0, 458, 269);
		panel_1.add(tabbedPane);
		
		chatroomPane lobby = new chatroomPane("0");
		tabbedPane.addTab("0", lobby);
		tablabel main = new tablabel("大廳", lobby);
		tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(lobby), main);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("", panel_4);
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
		
		panel_2 = new JPanel();
		panel_2.setBounds(0, 267, 456, 95);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		textField_2 = new JTextField();
		textField_2.setBounds(10, 36, 291, 49);
		panel_2.add(textField_2);
		textField_2.setColumns(10);
		
		btnNewButton_1 = new JButton("");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/tosend.png")));
		btnNewButton_1.setBounds(311, 36, 49, 49);
		panel_2.add(btnNewButton_1);
		
		JButton stickers = new JButton(new ImageIcon(mainFram.class.getResource("/Icon/emotionButtonIcon.png")));
		stickers.setBounds(10, 9, 84, 23);
		stickers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JPopupMenu tmpMenu = new JPopupMenu();
				for(int i = 1; i < 7; ++i){
					JMenuItem tmp = new JMenuItem(new ImageIcon(mainFram.class.getResource("/Icon/emotion" + i + "-key.png")));
					tmp.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							//send stickers by cClient
						}
					});
					tmpMenu.add(tmp);
				}
				tmpMenu.show(panel_2,10,9);
			}
			
		});
		panel_2.add(stickers);
		
		JButton shake = new JButton(new ImageIcon(mainFram.class.getResource("/Icon/vibrate.png")));
		shake.setBounds(105, 9, 23, 23);
		shake.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					int originalX = mainFram.this.getLocationOnScreen().x;
					int originalY = mainFram.this.getLocationOnScreen().y;
					for(int i = 0; i < 10; ++i){
						Thread.sleep(10);
						mainFram.this.setLocation(originalX, originalY+5);
						Thread.sleep(10);
						mainFram.this.setLocation(originalX, originalY-5);
						Thread.sleep(10);
						mainFram.this.setLocation(originalX+5, originalY);
						Thread.sleep(10);
						mainFram.this.setLocation(originalX, originalY);
					}
				}catch(Exception err){
					err.printStackTrace();
				}
			}
		});
		panel_2.add(shake);
		
		userNameLabel = new JLabel("");
		userNameLabel.setBounds(255, 9, 105, 17);
		panel_2.add(userNameLabel);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		panel_6.setBounds(370, 9, 76, 76);
		panel_2.add(panel_6);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/notSetFace.png")));
		panel_6.add(lblNewLabel);
		
		textField_2.addKeyListener(new checktypeListener());
		
		snedButton sendListener = new snedButton();
		textField_2.addActionListener(sendListener);
		btnNewButton_1.addActionListener(sendListener);
		
		//first time open the application and try to link server
		Vector<String> connection = conDialog.showDialog(mainFram.this, "連線");
		theClient.connectToServer((String)connection.get(0), Integer.parseInt((String)connection.get(1)), (String)connection.get(2));
		userNameLabel.setText((String)connection.get(2)); 
	}
        
         //////display///////
        //display chat conten
        public void disTxt (String from, String msg) {
            //JTextPane tmp = new JTextPane();
            JLabel tmp = new JLabel();
            tmp.setText(from + ":" + msg);
            chatBoardPane.add(tmp);
            chatBoardPane.validate();
            System.out.println("here");
            /*
             */
        }
        //display friends 
        public void disFrd (String name) {
        	onlineLabel tmp = new onlineLabel(name);
			allOnline.add(tmp);
			allOnline.validate();
			scrollPane.validate();
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
		//private privateMessage PM;
                String talkTo;
		onlineLabel(String name){
                    talkTo = name;
			setIcon(new ImageIcon(mainFram.class.getResource("/Icon/onlineGreenlignt.png")));
			setText(name);
			setToolTipText("double click to sned prvate message");
			addMouseListener(this);
		}
		public void mouseClicked(MouseEvent arg0) {
			if(arg0.getClickCount() == 2) {
                            PM.add(new privateMessage(theClient, talkTo));
                            (PM.lastElement()).setVisible(true);
                            //PM = new privateMessage(theClient,talkTo);
                            //PM.setVisible(true);
		}
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
	}
	class tablabel extends JPanel implements ActionListener{
		JButton exit;
		JLabel tabname;
		JScrollPane owner;
		JPopupMenu chatroomPopMenu;
		
		tablabel(String name, JScrollPane tmp){
			chatroomPopMenu = new JPopupMenu();
			JMenuItem pmExit = new JMenuItem("離開此聊天室"), pmAdd = new JMenuItem("加入使用者");
			pmExit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					tabbedPane.remove(tabbedPane.indexOfComponent(owner));
				}
			});
			pmAdd.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String addFriend = latterAddUser.showDialog(mainFram.this, "加入使用者");
					//等待後臺
				}
			});
			chatroomPopMenu.add(pmAdd);
			chatroomPopMenu.add(pmExit);
			addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e){
					if(e.getButton() == MouseEvent.BUTTON3)
						chatroomPopMenu.show(tablabel.this, e.getX(), e.getY());
					else if(e.getButton() == MouseEvent.BUTTON1)
						tabbedPane.setSelectedComponent(owner);
				}
			});
			owner = tmp;
			tabname = new JLabel(name);
			exit = new JButton(new ImageIcon(mainFram.class.getResource("/Icon/tabcancel.png")));
			exit.setPreferredSize(new Dimension(8,8));
			exit.addActionListener(this);
			if(name == "大廳"){ 
				exit.setEnabled(false);
				pmExit.setEnabled(false);
				pmAdd.setEnabled(false);
			}
			add(tabname);
			add(exit);
			setToolTipText("right click to see more");
		}
		public void actionPerformed(ActionEvent e){
			tabbedPane.remove(tabbedPane.indexOfComponent(owner));
		}
	}
	class addtab implements MouseListener{
		public void mouseClicked(MouseEvent e) {
			Vector connection = newChatroom.showDialog(theClient.getAllOnline(), mainFram.this, "Create new room");
			chatroomPane newChatroomPane = new chatroomPane((String)connection.get(0));
			tablabel tmp = new tablabel((String)connection.get(0), newChatroomPane);
			tabbedPane.add(newChatroomPane, tabbedPane.getTabCount()-1);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(newChatroomPane), tmp);
		}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}

	}
	class addtabButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Vector connection = newChatroom.showDialog(theClient.getAllOnline(), mainFram.this, "Create new room");
			chatroomPane newChatroomPane = new chatroomPane((String)connection.get(0));
			tablabel tmp = new tablabel((String)connection.get(0), newChatroomPane);
			tabbedPane.add(newChatroomPane, tabbedPane.getTabCount()-1);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(newChatroomPane), tmp);
		}
	}
	class snedButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(textField_2.getText().length() == 0)
				return;
			theClient.sendPMsg("/t -b " +textField_2.getText());
			textField_2.setText("");
			btnNewButton_1.setEnabled(false);
		}
	}
}

