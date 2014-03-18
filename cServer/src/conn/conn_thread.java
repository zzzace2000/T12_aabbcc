package conn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class conn_thread implements Runnable{

	private Socket sock;
	private cServer mainServer;
	private DataInputStream input;
	private DataOutputStream output;
	private String msg, username;
	
	public conn_thread(cServer mainServer, Socket sock) {
		this.mainServer = mainServer;
		this.sock = sock;
		
		try {
			input = new DataInputStream( sock.getInputStream() );
			output = new DataOutputStream( sock.getOutputStream() );
			
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try {

		// output success message
		output.writeUTF("/c");
		
		while (true) {
			String msg = input.readUTF();
			String name= null;

			System.out.println(msg);
			
			// username
			if (msg.startsWith("/u")) {
				name = msg.substring(3);
				System.out.println(name);
			}
	
			if (!mainServer.checkName(name)) {
				// Successfully create user
				username = name;
				output.writeUTF("/s" + mainServer.getAllOnl(name));
				break;
			}
			else {
				// tell user to change another user name
				output.writeUTF("/r");
				breakHand();
			}	
			} 
		}
		catch (IOException e) {
			System.out.println("Server: User name check phase failed");
			e.printStackTrace();
		}
		
		while (true) {
			try {
				msg = input.readUTF();
				judge(msg);
				
			} catch (IOException e) {
				
				if (e instanceof SocketException) {
					mainServer.deleteClient(conn_thread.this);
				}
				
				System.out.println(e.toString());
				e.printStackTrace();
			}
			
		}
	}
	
	private void breakHand() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread.currentThread().interrupt();
	}

	public void judge(String msg) {
		
		if (msg.startsWith("/c")) {
			
		}
		if (msg.startsWith("success")) {
			sendPMsg("Hi");
		}
	}

	public void sendPMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUserName() {
		if (username != null) {
			return username;
		}

		return null;
	}

	
	
	
	
}
