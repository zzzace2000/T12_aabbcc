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
	
	String serverIP;
	int port;
	String name;
	
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
	
	public boolean connectToServer(String serverIP, int port, String name) {
		
		this.serverIP = serverIP;
		this.port = port;
		this.name = name;
		System.out.println("Client connects Server with IP: "+serverIP+", port: "+port);
		try {
			socket = new Socket(InetAddress.getByName(serverIP), port);
			System.out.println("Connection successed");
			
			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());
			
			Thread theThread = new Thread(cClient.this);
			theThread.start();
			return true;
			
		} catch (IOException e) {
			System.out.println("Client connect to Server failed");
			System.out.println(e.toString());
			
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void run() {
		try {

			while (true) {
				
				String msg = input.readUTF();
				System.out.println(msg);
				judge(msg);
			}
		} catch (IOException e) {
			//reconnect();
			System.out.println("get msg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	private void sendMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			System.out.println("sendMsg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private synchronized void reconnect() {
		connectToServer(serverIP, port, name);
	}

	private void judge(String msg) {
		
		// returns username
		if (msg.startsWith("/c")) {
			sendMsg("/u " + name);
		}
		else if (msg.startsWith("/r")) {
			mainFram.showAlertDialog("Name has been used. Please use another name");
		}
		else if (msg.startsWith("/s")) {
			System.out.println("Successfully add Name list");
		}
		
		
	}
	
}
