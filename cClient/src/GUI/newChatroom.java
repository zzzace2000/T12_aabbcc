package GUI;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Collections;

public class newChatroom extends JDialog {

	private mainFram frame;
        private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTable table;
        private JPanel frdPane;
	private Vector values = new Vector(); //first element is chatroom name, second element is friend list
        private Vector<String> rmFrd = new Vector<String>();
	private JLabel lblNewLabel;
	private JTable friendTable;
	private DefaultTableModel addFriend;
        

	/**
	 * Create the dialog.
	 */
	public newChatroom(Vector<String> allOnline, JFrame parent, String title) {
		super(parent, title, true);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("create new chat room");
		setIconImage(Toolkit.getDefaultToolkit().getImage(newChatroom.class.getResource("/Icon/Chattmp.png")));
		setBounds(100, 100, 416, 295);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblChatroomName = new JLabel("Chatroom name");
		lblChatroomName.setBounds(10, 10, 95, 15);
		contentPanel.add(lblChatroomName);
                
		
                
		textField = new JTextField();
		textField.setBounds(10, 31, 216, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
	
		JComboBox friendBox = new JComboBox(allOnline);
		friendBox.setBounds(10, 62, 117, 21);
		contentPanel.add(friendBox);
                friendBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                       JComboBox selFrd = (JComboBox)e.getSource();
                       String frd = (String)selFrd.getSelectedItem();
                       boolean addFlag = true;
                       for(int i = 0; i < rmFrd.size(); ++i){
                    	   if(rmFrd.get(i).equals(frd)){
                    		   addFlag = false;
                    		   break;
                    	   }
                       }
                       if(addFlag){
                    	   rmFrd.add(frd);
                    	   addFriend.addRow(new Object[]{frd});
                       }
                    }
                });
		
		JButton btnNewButton_1 = new JButton("delete");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(friendTable.getSelectedRow() < 0)
					return;
				Object selectedObject = (Object)friendTable.getValueAt(friendTable.getSelectedRow(),0);
				String tmp = (String)selectedObject;
				addFriend.removeRow(friendTable.getSelectedRow());
				rmFrd.remove(tmp);
			}
		});
		btnNewButton_1.setBounds(137, 94, 89, 23);
		contentPanel.add(btnNewButton_1);
		
		JScrollPane friendScrollPane = new JScrollPane();
		friendScrollPane.setBounds(10, 93, 117, 121);
		contentPanel.add(friendScrollPane);
		
        addFriend = new DefaultTableModel();
		addFriend.addColumn("加入");
		friendTable = new JTable(addFriend);
		friendScrollPane.setViewportView(friendTable);
		
		JButton btnNewButton_2 = new JButton("Create");
		btnNewButton_2.addActionListener(new addListener());
		btnNewButton_2.setIcon(new ImageIcon(newChatroom.class.getResource("/Icon/addChatroom.png")));
		btnNewButton_2.setBounds(236, 31, 154, 87);
		contentPanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Cancel");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_3.setIcon(new ImageIcon(newChatroom.class.getResource("/Icon/cancelchat.png")));
		btnNewButton_3.setBounds(236, 128, 154, 87);
		contentPanel.add(btnNewButton_3);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setBounds(10, 224, 380, 23);
		contentPanel.add(lblNewLabel);
	}
	//////helper funtions/////
	public Vector getValues(){
		return values;
	}
	
	public static Vector showDialog(Vector<String> allOnline, JFrame parent, String title){
		newChatroom dialog = new newChatroom(allOnline, parent, title);
		dialog.setVisible(true);
		return dialog.getValues();
	}
        
      
	
	class addListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(!check()){
				lblNewLabel.setText("Chatroom name is incorrect");
				lblNewLabel.setIcon(new ImageIcon(newChatroom.class.getResource("/Icon/warning.png")));
				return;
			}
			values.add(textField.getText());
                        values.add(rmFrd);
			setVisible(false);
		}
	}
	
	private boolean check(){
		if(textField.getText().length() == 0) return false;
		for(int i = 0; i < textField.getText().length(); ++i){
			if(textField.getText() != " ")
				return true;
		}
		return false;
	}
}
