package readWebcamUsingUDP;

/*  
 * Captures the camera stream with OpenCV  
 * Search for the faces  
 * Display a circle around the faces using Java  
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import soundRecorderByUDP.SoundRecorderMain;
import webCamReceiverUDP.receiver;
import GUI.videoFrame;

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
		MatOfByte mb = new MatOfByte();
		Highgui.imencode(".jpg", matrix, mb);
		try {
			this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
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

class FaceDetector {
	private CascadeClassifier face_cascade;

	// Create a constructor method
	public FaceDetector() {
		// face_cascade=new
		// CascadeClassifier("./cascades/lbpcascade_frontalface_alt.xml");
		// ..didn't have not much luck with the lbp

		face_cascade = new CascadeClassifier(".//res//lbpcascade_frontalface.xml");
		if (face_cascade.empty()) {
			System.out.println("--(!)Error loading A\n");
			return;
		}
		else {
			System.out.println("Face classifier loooaaaaaded up");
		}
	}

	public Mat detect(Mat inputframe) {
		Mat mRgba = new Mat();
		Mat mGrey = new Mat();
		MatOfRect faces = new MatOfRect();
		inputframe.copyTo(mRgba);
		inputframe.copyTo(mGrey);
		Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(mGrey, mGrey);
		face_cascade.detectMultiScale(mGrey, faces);
		System.out.println(String.format("Detected %s faces", faces.toArray().length));
		for (Rect rect : faces.toArray()) {
			Point center = new Point(rect.x + rect.width * 0.5, rect.y + rect.height * 0.5);
			Core.rectangle(mRgba, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top
																			// right
					new Scalar(255, 0, 0)); // RGB colour
			// draw a blue eclipse around face
			// Size s = new Size( rect.width*0.5, rect.height*0.5), 0, 0, 360,
			// new Scalar( 0, 0, 255 );
			// Core.ellipse( mRgba, center,s , 4, 8, 0 );
		}
		return mRgba;
	}
}

public class MainRecorder implements Runnable {

	LinkedList<BufferedImage> imageBuffer = new LinkedList<BufferedImage>();
	boolean imageIsNotSent = false;
	public int ViewMode = 1; // 1 from webcam. 2 from desktop

	receiver theReceiver;
	videoFrame video;
	public sendImage child;
	VideoCapture webCam;
	Mat webcam_image;
	Graphics2D graphics2D;
	boolean closeThreadIndicator = false;
	Robot robot;
	Rectangle screenRect;

	String toIPAddress;

	// sound
	public SoundRecorderMain soundRecorder;

	Thread theReceiverThread;
	Thread theThread;
	Thread theSoundRecorderThread;

	public static void main(String arg[]) throws InterruptedException {

		new MainRecorder("localhost");
	}

	public MainRecorder(String Tipa) {
		// Load the native library.

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// or ... System.loadLibrary("opencv_java244");

		toIPAddress = Tipa;

		// open sound
		soundRecorder = new SoundRecorderMain(toIPAddress);
		theSoundRecorderThread = new Thread(soundRecorder);
		theSoundRecorderThread.start();

		// make the JFrame
		video = new videoFrame(this);
		video.setVisible(true);

		FaceDetector faceDetector = new FaceDetector();

		// Open and Read from the video stream
		webcam_image = new Mat();
		webCam = new VideoCapture(0);

		// Initialize received thread
		theReceiver = new receiver(video);
		theReceiverThread = new Thread(theReceiver);
		theReceiverThread.start();

		try {
			robot = new Robot();
			screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // end main

	public void closeTheThread() {
		System.out.println("Interrupt");
		theReceiverThread.interrupt();
		theThread.interrupt();
		System.out.println("sound receiver interrupt");
		soundRecorder.closeTheThread();
		closeThreadIndicator = true;
	}

	@Override
	public void run() {
		try {
		if (webCam.isOpened()) {
			Thread.sleep(1000);

			child = new sendImage(this, toIPAddress);
			theThread = new Thread(child);
			theThread.start();

			while (!closeThreadIndicator) {
				// ==== Webcam mode ====//
				if (ViewMode == 1) {
					webCam.read(webcam_image);
					//System.out.println("Catch an image!");

					if (!webcam_image.empty()) {

						MatOfByte mb = new MatOfByte();
						Highgui.imencode(".jpg", webcam_image, mb);

						BufferedImage image = ImageIO
								.read(new ByteArrayInputStream(mb.toArray()));

						imageBuffer.add(image);
						
						video.showImage(image);
						
					} else {
						System.out.println(" --(!) No captured frame from webcam !");
						webCam.release(); // release the webcam
						return;
					}
				} else if (ViewMode == 2) {
					BufferedImage capture = robot.createScreenCapture(screenRect);
					int width = 600;
					int height = (int) Math.floor(width
							* capture.getHeight() / capture.getWidth());
					// Create new (blank) image of required (scaled) size

					BufferedImage scaledImage = new BufferedImage(width,
							height, BufferedImage.TYPE_INT_ARGB);

					// Paint scaled version of image to new image

					graphics2D = scaledImage.createGraphics();
					graphics2D.setRenderingHint(
							RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					graphics2D
							.drawImage(capture, 0, 0, width, height, null);

					imageBuffer.add(scaledImage);
					video.showImage(scaledImage);

				}

			} // while

		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
