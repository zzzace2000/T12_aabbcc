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
	Vector<conn_thread> conn_client = new Vector<conn_thread>();
	ArrayList<String> nameList = new ArrayList<String>();
	
	public cServer() {
		// initialize variable
		
		try {
			ss = new ServerSocket(port);
			
			while(true) {
								
				synchronized (this) {
					System.out.println("Start listening");
					Socket theSocket = ss.accept();
					
					// System show
					System.out.println("Client connection");
				
					conn_client.add(new conn_thread(this, theSocket));
				}
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

	public void deleteClient(conn_thread theThread) {
		String clientName = theThread.getUserName();
		if (clientName != null) {
			conn_client.remove(theThread);
			nameList.remove(clientName);
		}
	}

	public boolean checkName(String name) {
		for (String n: nameList)
		{
			if ( n == name) {
				return false;
			}
		}
		nameList.add(name);
		return true;
	}
	
	
	
	
	
}
