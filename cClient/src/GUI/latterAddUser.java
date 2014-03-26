package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class latterAddUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	String values;
	/**
	 * Create the dialog.
	 */
	public latterAddUser(JFrame parent, String title) {
		super(parent, title, true);
		setTitle("加入使用者");
		setIconImage(Toolkit.getDefaultToolkit().getImage(latterAddUser.class.getResource("/Icon/latterAddUser.png")));
		setResizable(false);
		setBounds(100, 100, 148, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JComboBox comboBox = new JComboBox();
			comboBox.setBounds(10, 10, 112, 21);
			contentPanel.add(comboBox);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public String getValues(){
		return values;
	}
	public static String showDialog(JFrame parent, String title){
		latterAddUser dialog = new latterAddUser(parent, title);
		dialog.setVisible(true);
		return dialog.getValues();
	}

}
