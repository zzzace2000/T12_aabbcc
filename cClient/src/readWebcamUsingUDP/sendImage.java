package readWebcamUsingUDP;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

public class sendImage implements Runnable{
	
	MainRecorder theMain;
	DatagramSocket theSocket;
	OutputStream oStream;
	String IP_TO_STREAM_TO;
	int PORT_TO_STREAM_TO = 19998;
	boolean sendIndicator = true;
	
	public sendImage(MainRecorder mn, String ToIPAddress) {

		try {
			
			theSocket = new DatagramSocket();
			theMain = mn;
			//IP_TO_STREAM_TO = getLocalAddress();
			IP_TO_STREAM_TO = ToIPAddress;
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		
		try {
			while(!Thread.currentThread().isInterrupted()) {
				if (!theMain.imageBuffer.isEmpty() && sendIndicator) {
					//System.out.println("Write something");
					BufferedImage theImage = theMain.imageBuffer.remove(0);
					//theMain.imageBuffer.clear();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(theImage, "jpg", baos);
					baos.close();

					//System.out.println("Write byte size = " + baos.size());

					//System.out.println("Write byte stream");
					sendThruUDP(baos.toByteArray());
					//theMain.imageIsNotSent = false;
				}
				
			}
		} catch (IOException e) {
			System.out.println("Wrong in image WriteIO");
			e.printStackTrace();
		} finally {
			theSocket.close();
		}
	}

	private void sendThruUDP(byte[] packet) throws UnknownHostException, IOException {
		//System.out.println("packet size = "+packet.length);
		theSocket.send( new DatagramPacket( packet , packet.length , InetAddress.getByName( IP_TO_STREAM_TO ) , PORT_TO_STREAM_TO ) ) ; 
	}
	
	private String getLocalAddress() {
		String theAddresString;
		try {
			theAddresString = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println(theAddresString);
		return theAddresString;
	}

	public void closeTheThread() {
	}
	
	public void suspendSendingImage() {
		sendIndicator = false;
	}
	public void resumeSendingImage() {
		sendIndicator = true;
	}
	
}
