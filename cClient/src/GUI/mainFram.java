package GUI;

import javax.swing.*;

import java.awt.BorderLayout;
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
	private JTextField textField_2;
	private JButton btnNewButton_1;
	private JTabbedPane tabbedPane;
	
	
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setEditable(false);
		scrollPane.setViewportView(textPane_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 198, 130, 164);
		panel.add(scrollPane_1);
		
		JLabel label_1 = new JLabel("當前聊天室在線");
		scrollPane_1.setColumnHeaderView(label_1);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setEditable(false);
		scrollPane_1.setViewportView(textPane_2);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(0, 0, 130, 24);
		panel.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean success;
				Vector<String> connection = conDialog.showDialog(mainFram.this, "連線");
				success = theClient.connectToServer((String)connection.get(0), Integer.parseInt((String)connection.get(1)), (String)connection.get(2));
				if (success) {
					dispose();
				}
				else {
					showAlertDialog("Username has been used.");
				}
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
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane_2.setViewportView(textPane);
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
		
		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/soundicon.png")));
		btnNewButton_4.setBounds(100, 9, 29, 23);
		panel_2.add(btnNewButton_4);
		
		JButton button = new JButton("");
		button.setIcon(new ImageIcon(mainFram.class.getResource("/Icon/mic.png")));
		button.setBounds(128, 9, 29, 23);
		panel_2.add(button);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(167, 11, 1, 21);
		panel_2.add(separator_1);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(178, 9, 144, 23);
		panel_2.add(comboBox_1);
		
		textField_2.addKeyListener(new checktypeListener());
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

