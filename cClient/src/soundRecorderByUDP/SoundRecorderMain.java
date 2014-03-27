package soundRecorderByUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import soundReceiveByUDP.SoundReceiverMain;

public class SoundRecorderMain implements Runnable {

	private static final String IP_TO_STREAM_TO = "localhost";
	private static final int PORT_TO_STREAM_TO = 8888;
	DatagramSocket theSocket;
	SoundReceiverMain r;

	TargetDataLine targetDataLine;
	DataLine.Info dataLineInfo;

	boolean soundRecordIndicator = true;
	boolean threadRunningIndicator = true;

	/** Creates a new instance of SoundRecorderMain */
	public SoundRecorderMain() {

		// Open Sound Receiver
		r = new SoundReceiverMain();
		r.start();

		try {

			theSocket = new DatagramSocket();

		} catch (SocketException e) {
			e.printStackTrace();
		}

		Mixer.Info minfo[] = AudioSystem.getMixerInfo();
		for (int i = 0; i < minfo.length; i++) {
			System.out.println(minfo[i]);
		}

		if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
			try {

				dataLineInfo = new DataLine.Info(TargetDataLine.class,
						getAudioFormat());
				targetDataLine = (TargetDataLine) AudioSystem
						.getLine(dataLineInfo);
				targetDataLine.open(getAudioFormat());
				targetDataLine.start();

			} catch (Exception e) {
				System.out.println(" not correct ");
				System.exit(0);
			}
		}

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	/*
	 * public static void main(String[] args) { new SoundRecorderMain(); }
	 */

	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 1,2
		boolean signed = true;
		// true,false
		boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	public void sendThruUDP(byte soundpacket[]) {
		try {
			theSocket.send(new DatagramPacket(soundpacket, soundpacket.length,
					InetAddress.getByName(IP_TO_STREAM_TO), PORT_TO_STREAM_TO));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" Unable to send soundpacket using UDP ");
		}

	}

	@Override
	public void run() {
		byte tempBuffer[] = new byte[8000];

		while (threadRunningIndicator) {
			if (soundRecordIndicator) {
				targetDataLine.read(tempBuffer, 0, tempBuffer.length);
				sendThruUDP(tempBuffer);
				System.out.println("Keep Reading");
			}
		}
		r.closeTheThread();
		System.out.println("close sound recorder");
		targetDataLine.drain();
		targetDataLine.close();
		theSocket.close();

		// close the receiver
		System.out.println("close the sound receiver");
	}

	public void suspendSoundRecord() {
		soundRecordIndicator = false;
	}

	public void resumeSoundRecord() {
		soundRecordIndicator = true;
	}
	
	public void closeTheThread() {
		threadRunningIndicator = false;
	}
}