package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;

import java.awt.GridLayout;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.FlowLayout;

public class videoFrame extends JFrame {
	
	private JPanel contentPane;
	public FacePanel netPane;
	public FacePanel camPane;
	private MainRecorder theRecorder;
	private JToggleButton turnOnVideo;
	private JToggleButton showDesktop;
	private JToggleButton enableSound;

	// show video or not
	boolean showVideoMsg = true;

	// Record the size change
	static int origHeight = 450;
	static int origWidth = 675;
	
	// netpane height and with
	static int netPaneHeight = 320;
	static int netPaneWidth = 636;
	
	// campane height and width

	static int CAMPANE_X = 450;
	static int CAMPANE_Y = 171;
	static int CAMPANE_HEIGHT = 150;
	static int CAMPANE_WIDTH = 186;
	
	/**
	 * Create the frame.
	 */
	public videoFrame(MainRecorder mr) {
		theRecorder = mr;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, origWidth, origHeight);
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
				camPane.assignImage(null);
				camPane.setOpaque(false);
				theRecorder.child.suspendSendingImage();
			}

			private void turnOn() {
				turnOnVideo.setText("off");
				showVideoMsg = true;
				theRecorder.child.resumeSendingImage();
			}
		});
		turnOnVideo.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/video.png")));
		panel.add(turnOnVideo);

		showDesktop = new JToggleButton("Desktop");
		showDesktop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (showDesktop.isSelected()) {
					turnOnDesktop();
				} else {
					turnOffDesktop();
				}
			}

			private void turnOffDesktop() {
				theRecorder.ViewMode = 1;
				showDesktop.setText("Desktop");
			}

			private void turnOnDesktop() {
				theRecorder.ViewMode = 2;
				showDesktop.setText("WebCam");
			}

		});
		showDesktop.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/displayDesktop.png")));
		panel.add(showDesktop);

		enableSound = new JToggleButton("off");
		enableSound.setEnabled(true);
		enableSound.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (enableSound.isSelected()) {
					turnOnSound();
				} else {
					turnOffSound();
				}
			}

			private void turnOffSound() {
				theRecorder.soundRecorder.suspendSoundRecord();
				enableSound.setText("On");
			}

			private void turnOnSound() {
				theRecorder.soundRecorder.resumeSoundRecord();
				enableSound.setText("Off");
			}

		
		});
		enableSound.setIcon(new ImageIcon(videoFrame.class
				.getResource("/Icon/sound.png")));
		panel.add(enableSound);
		
		JLayeredPane layeredPane = new JLayeredPane();
		contentPane.add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);
		
		netPane = new FacePanel();
		layeredPane.setLayer(netPane, 1);
		int layeredPaneWidth = this.getWidth();
		int layeredPaneHeight = this.getHeight();
		System.out.println(layeredPaneHeight+" "+layeredPaneWidth);
		System.out.println("panel layout: "+panel.getWidth()+" "+panel.getHeight());
		
		netPane.setBounds(0, 0, netPaneWidth, netPaneHeight);
		netPane.setOpaque(true);
		layeredPane.add(netPane);
		
		camPane = new FacePanel();
		layeredPane.setLayer(camPane, 2);
		camPane.setOpaque(false);
		camPane.setBounds(CAMPANE_X, CAMPANE_Y, CAMPANE_WIDTH, CAMPANE_HEIGHT);
		camPane.setOpaque(true);
		layeredPane.add(camPane);
		
		contentPane.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				int height = videoFrame.this.getHeight();
				int width = videoFrame.this.getWidth();
				System.out.println("frame:"+width+" "+height);

				double heightRatio = (double)height/origHeight;
				double widthRatio = (double)width/origWidth;
				System.out.println("ratio:"+widthRatio+" "+heightRatio);
				
				int newNetPaneHeight = (int) Math.floor(netPaneHeight * heightRatio);
				int newNetPaneWidth = (int) Math.floor(netPaneWidth * widthRatio);
				System.out.println("netPane:"+newNetPaneWidth+" "+newNetPaneHeight);
				
				netPane.setBounds(0,0,newNetPaneWidth,newNetPaneHeight);
				
				int newCamPaneX = (int) Math.floor(CAMPANE_X * widthRatio);
				int newCamPaneY = (int) Math.floor(CAMPANE_Y * heightRatio);
				
				int newCamPaneHeight = (int) Math.floor(CAMPANE_HEIGHT * heightRatio);
				int newCamPaneWidth = (int) Math.floor(CAMPANE_WIDTH * widthRatio);
				
				camPane.setBounds(newCamPaneX, newCamPaneY, newCamPaneWidth, newCamPaneHeight);
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
		});
		
	}
	
	@Override
	public void dispose() {
		theRecorder.closeTheThread();
		super.dispose();
	}
	

	class FacePanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private BufferedImage image;
		public boolean trivial = true;

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

	public void showImage(BufferedImage scaledImage) {
		if (showVideoMsg) {
			camPane.assignImage(scaledImage);
			camPane.repaint();
		}
	}

	public void showReceivedImage(BufferedImage image) {
		netPane.assignImage(image);
		netPane.repaint();
		netPane.revalidate();
	}
}
