package GUI;

import java.awt.BorderLayout;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Vector;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class conDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private Vector<String> values = new Vector<String>();
	
	private String IP_ADDRESS;
	private static int PORT = 5001;

	/**
	 * Create the dialog.
	 */
	public conDialog(JFrame parent, String title) {
		super(parent, title, true);
		setBounds(100, 100, 299, 227);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		try {
			IP_ADDRESS = getLocalIPAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
		
		textField = new JTextField();
		textField.setText(IP_ADDRESS);
		textField.setBounds(10, 35, 96, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 66, 46, 15);
		contentPanel.add(lblPort);
		
		textField_1 = new JTextField();
		textField_1.setText(String.valueOf(PORT));
		textField_1.setBounds(10, 91, 96, 21);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 122, 46, 15);
		contentPanel.add(lblName);
		
		textField_2 = new JTextField();
		textField_2.setBounds(10, 147, 96, 21);
		contentPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton = new JButton("Connect!!!");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				values.add(textField.getText());
				values.add(textField_1.getText());
				values.add(textField_2.getText());
				setVisible(false);
			}
		});
		btnNewButton.setIcon(new ImageIcon(conDialog.class.getResource("/Icon/network_connection-tmp.png")));
		btnNewButton.setBounds(116, 10, 153, 83);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(conDialog.class.getResource("/Icon/cancel.png")));
		btnNewButton_1.setBounds(116, 103, 153, 68);
		contentPanel.add(btnNewButton_1);
		
		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(10, 10, 46, 15);
		contentPanel.add(lblIp);
	}
	
	public Vector<String> getValues(){
		return values;
	}
	
	public static Vector<String> showDialog(JFrame parent, String title){
		conDialog tmp = new conDialog(parent, title);
		tmp.setVisible(true);
		return tmp.getValues();
	}
	
	private String getLocalIPAddress() throws UnknownHostException {
		String theAddresString = Inet4Address.getLocalHost().getHostAddress();
		System.out.println(theAddresString);
		return theAddresString;
		
	}
	
	
}
