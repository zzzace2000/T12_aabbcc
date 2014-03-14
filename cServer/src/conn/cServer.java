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
				
				Socket theSocket = new Socket();
				
				synchronized (this) {
					System.out.println("Start listening");
					theSocket = ss.accept();
				}
				DataInputStream input = new DataInputStream( theSocket.getInputStream() );
				DataOutputStream output = new DataOutputStream( theSocket.getOutputStream() );
				
				// System show
				System.out.println("Client connection");
				
				conn_thread theThread = new conn_thread(this, theSocket);
				/*  Find problems....I can't store the thread into a vector....why?   */
				//conn_client.add(theThread);
				Thread td = new Thread(theThread);
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
