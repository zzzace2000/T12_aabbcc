package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

public class videoFrame extends JFrame {

	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	public videoFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JToggleButton turnOnVideo = new JToggleButton("on");
		turnOnVideo.setIcon(new ImageIcon(videoFrame.class.getResource("/Icon/video.png")));
		panel.add(turnOnVideo);
		
		JToggleButton showDesktop = new JToggleButton("New toggle button");
		showDesktop.setIcon(new ImageIcon(videoFrame.class.getResource("/Icon/displayDesktop.png")));
		panel.add(showDesktop);
		
		JToggleButton enableSound = new JToggleButton("sound");
		enableSound.setIcon(new ImageIcon(videoFrame.class.getResource("/Icon/sound.png")));
		panel.add(enableSound);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.CENTER);
	}

}
