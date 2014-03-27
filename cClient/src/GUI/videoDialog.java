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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class videoDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private cClient theClient;
	private int talkToID;
	private String toIPAddress;
	/**
	 * Create the dialog.
	 */
	public videoDialog(JFrame parent, String title, cClient cc, int ttID, String fromIPAddress) {
		super(parent, title, true);
		theClient = cc;
		talkToID = ttID;
		toIPAddress = fromIPAddress;
		
		setResizable(false);
		setBounds(100, 100, 173, 151);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel("接受視訊?");
			lblNewLabel.setIcon(new ImageIcon(videoDialog.class.getResource("/Icon/videoLabel.png")));
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("是");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
						theClient.replyVideoRequest(talkToID, true, toIPAddress);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("否");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
						theClient.replyVideoRequest(talkToID, false, "");
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public static void showDialog(JFrame parent, String title, cClient cc, int ttID, String fromIPAddress){
		videoDialog tmp = new videoDialog(parent, title, cc, ttID, fromIPAddress);
		tmp.setVisible(true);
	}
}
