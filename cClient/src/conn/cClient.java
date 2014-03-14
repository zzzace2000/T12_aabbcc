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
		while (true) {
			
			String msg = null;
			try {
				msg = input.readUTF();
				System.out.println(msg);
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
			System.out.println("sendMsg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private void reconnect() {
		connectToServer(serverIP, port, name);
	}

	private void judge(String msg) {
		// TODO Auto-generated method stub
		
		String Msg;
		try {
			Msg = input.readUTF();
		
		
		if (Msg.startsWith("/c")) {
			sendMsg("/u " + name);
		}
		else if (Msg.startsWith("/r")) {
			mainFram.showAlertDialog("Name has been occupied.");
		}
		else {
			reconnect();
		}
		} catch (IOException e) {
			System.out.println("Failed in judge msg");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}
	
}
