package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import conn.cClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class logoutFrame extends JDialog {
	private cClient theClient;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public logoutFrame(JFrame parent, String title, cClient tmp) {
		super(parent, title, true);
		theClient = tmp;
		setResizable(false);
		setBounds(100, 100, 166, 141);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel("確定登出?");
			lblNewLabel.setIcon(new ImageIcon(logoutFrame.class.getResource("/Icon/logout.png")));
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("是");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//implement logout, remember to call variable theClient
						logoutFrame.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("否");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						logoutFrame.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public static void showDialog(JFrame parent, String title, cClient tmpclient){
		logoutFrame tmp = new logoutFrame(parent, title, tmpclient);
		tmp.setVisible(true);
	}

}
