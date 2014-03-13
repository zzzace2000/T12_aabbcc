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
		while (true) {
			
			try {
				msg = input.readUTF();
				
				judge(msg);
				
			} catch (IOException e) {
				
				if (e instanceof SocketException) {
					// delete client list
				}
				
				System.out.println(e.toString());
				e.printStackTrace();
			}
			
		}
	}
	
	public void judge(String msg) {
		
	}
	
	
	
	
}
