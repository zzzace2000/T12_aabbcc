package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JSeparator;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class abouts extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public abouts() {
		setResizable(false);
		setTitle("Abouts");
		setIconImage(Toolkit.getDefaultToolkit().getImage(abouts.class.getResource("/Icon/about.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 234, 201);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNmlabTeam = new JLabel("NMLab - team12");
		lblNmlabTeam.setBounds(66, 10, 162, 15);
		contentPane.add(lblNmlabTeam);
		
		JLabel label = new JLabel("網路與多媒體實驗戰隊 - 代號12小隊");
		label.setBounds(10, 27, 236, 15);
		contentPane.add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 52, 193, 2);
		contentPane.add(separator);
		
		JLabel lblCopyleft = new JLabel("Copyleft");
		lblCopyleft.setBounds(82, 65, 46, 15);
		contentPane.add(lblCopyleft);
		
		JLabel lblVersionBeta = new JLabel("Version 1.0 beta");
		lblVersionBeta.setBounds(66, 91, 121, 15);
		contentPane.add(lblVersionBeta);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 116, 193, 2);
		contentPane.add(separator_1);
		
		JButton btnNewButton = new JButton("Done");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(64, 130, 87, 23);
		contentPane.add(btnNewButton);
	}
}
