package conn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class cServer {
	
	static int port = 5001;

	ServerSocket ss;
	Vector<conn_thread> conn_client;
	ArrayList<String> nameList = new ArrayList<String>();
	
	public cServer() {
		
		try {
			ss = new ServerSocket(port);
			
			while(true) {
				
				Socket theSocket;
				synchronized (this) {
					System.out.println("Start listening");
					theSocket = ss.accept();
				}
				System.out.println("Accept some Clients");
				DataInputStream input = new DataInputStream( theSocket.getInputStream() );
				DataOutputStream output = new DataOutputStream( theSocket.getOutputStream() );
				
				// System show
				System.out.println("Client connection");
				
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
					
					if (!name_exists(name)) {
						nameList.add(name);
						break;
					}
					// tell user to change another user name
					output.writeUTF("/r");
				}
				conn_client.add(new conn_thread(this, theSocket));
				Thread td = new Thread(conn_client.lastElement());
				td.start();
			}
			
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		
	}

	private boolean name_exists(String name) {
		for (String n: nameList)
		{
			if ( n == name) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
