package GUI;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class newChatroom extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTable table;
	private Vector values = new Vector();

	/**
	 * Create the dialog.
	 */
	public newChatroom(){}
	public newChatroom(JFrame parent, String title) {
		super(parent, title, true);
		setAlwaysOnTop(true);
		setTitle("create new chat room");
		setIconImage(Toolkit.getDefaultToolkit().getImage(newChatroom.class.getResource("/Icon/Chattmp.png")));
		setBounds(100, 100, 416, 267);
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
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(10, 62, 117, 21);
		contentPanel.add(comboBox);
		
		JButton btnNewButton = new JButton("add");
		btnNewButton.setBounds(137, 61, 89, 23);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("delete");
		btnNewButton_1.setBounds(137, 94, 89, 23);
		contentPanel.add(btnNewButton_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 93, 117, 121);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnNewButton_2 = new JButton("Create");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton_2.setIcon(new ImageIcon(newChatroom.class.getResource("/Icon/addChatroom.png")));
		btnNewButton_2.setBounds(236, 31, 154, 87);
		contentPanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Cancel");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton_3.setIcon(new ImageIcon(newChatroom.class.getResource("/Icon/cancelchat.png")));
		btnNewButton_3.setBounds(236, 128, 154, 87);
		contentPanel.add(btnNewButton_3);
	}
	
	public Object[] getValues(){
		return values.toArray();
	}
	
	public Object[] showDialog(JFrame parent, String title){
		newChatroom dialog = new newChatroom(parent, title);
		dialog.setVisible(true);
		return dialog.getValues();
	}
}
