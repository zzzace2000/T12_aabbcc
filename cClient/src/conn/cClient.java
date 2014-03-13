package conn;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import GUI.mainFram;

public class cClient implements Runnable{
		
	Socket socket;
	DataOutputStream output;
	DataInputStream input;
	mainFram frame;
	public cClient() {
		mainFram frame = new mainFram(cClient.this);
		frame.setVisible(true);
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}
	
	public void connectToServer(String serverIP, int port, String name) {
		
		try {
			socket = new Socket(InetAddress.getByName(serverIP), port);
		} catch (UnknownHostException e) {
			System.out.println("can't analyze host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("connection falied");
			e.printStackTrace();
		}
		
		try {
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Connection failed. Can't open input output stream.");
			e.printStackTrace();
		}
		
		sendMsg("success!");
		Thread theThread = new Thread(cClient.this);
		theThread.start();
		
	}

	@Override
	public void run() {
		while (true) {
			
			String msg = null;
			try {
				msg = input.readUTF();
			} catch (IOException e) {
				reconnect();
				e.printStackTrace();
			}
			judge(msg);
			
		}
	}
	
	private void sendMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void reconnect() {
		// TODO Auto-generated method stub
		
	}

	private void judge(String msg) {
		// TODO Auto-generated method stub
		if (msg.startsWith("Hi")) {
			frame.showMsg(msg);
		}
	}
	
}
