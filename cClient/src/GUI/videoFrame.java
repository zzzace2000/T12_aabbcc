package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import readWebcamUsingUDP.MainRecorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Insets;

public class videoFrame extends JFrame {

	private JPanel contentPane;
	public FacePanel netPane;
	public FacePanel camPane;
	private MainRecorder theRecorder;
	private JToggleButton turnOnVideo;

	// show video or not
	boolean showVideoMsg = true;

	/**
	 * Create the frame.
	 */
	public videoFrame(MainRecorder mr) {
		theRecorder = mr;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		turnOnVideo = new JToggleButton("off");
		turnOnVideo.setSelected(true);
		turnOnVideo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (turnOnVideo.isSelected()) {
					turnOn();
				} else {
					turnOff();
				}
			}

			private void turnOff() {
				turnOnVideo.setText("on");
				showVideoMsg = false;
			}

			private void turnOn() {
				turnOnVideo.setText("off");
				showVideoMsg = true;
			}
		});
		turnOnVideo.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/video.png")));
		panel.add(turnOnVideo);

		JToggleButton showDesktop = new JToggleButton("New toggle button");
		showDesktop.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/displayDesktop.png")));
		panel.add(showDesktop);

		JToggleButton enableSound = new JToggleButton("sound");
		enableSound.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/sound.png")));
		panel.add(enableSound);
		
		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);
		
		camPane = new FacePanel();
		//layeredPane.setLayer(camPane, 2);
		camPane.setBackground(Color.ORANGE);
		camPane.setBounds(220, 100, 100, 100);
		camPane.setOpaque(true);
		layeredPane.add(camPane);
		
		JPanel netPane = new FacePanel();
		//layeredPane.setLayer(netPane, 1);
		netPane.setBackground(Color.RED);
		netPane.setBounds(0, 0, 424, 213);
		netPane.setOpaque(true);
		layeredPane.add(netPane);
	}
	
	@Override
	public void dispose() {
		theRecorder.closeTheThread();
		super.dispose();
	}
	

	class FacePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private BufferedImage image;

		// Create a constructor method
		public FacePanel() {
			super();
		}

		/*
		 * Converts/writes a Mat into a BufferedImage.
		 * 
		 * @param matrix Mat of type CV_8UC3 or CV_8UC1
		 * 
		 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
		 */
		public boolean matToBufferedImage(Mat matrix) {
			System.out.println("getInside");

			MatOfByte mb = new MatOfByte();
			System.out.println("getInside");
			Highgui.imencode(".jpg", matrix, mb);
			try {
				this.image = ImageIO
						.read(new ByteArrayInputStream(mb.toArray()));
			} catch (IOException e) {
				e.printStackTrace();
				return false; // Error
			}
			return true; // Successful
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (this.image == null)
				return;
			g.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
		}

		public void assignImage(BufferedImage scaledImage) {
			image = scaledImage;
		}

	}

	public JPanel getNetPane() {
		return netPane;
	}

	public JPanel getCamPane() {
		return camPane;
	}

	public synchronized void showImage(BufferedImage scaledImage) {
		if (showVideoMsg) {
			camPane.assignImage(scaledImage);
			camPane.repaint();
		}
	}

	public synchronized void showReceivedImage(BufferedImage image) {
		netPane.assignImage(image);
		netPane.repaint();
	}

}
