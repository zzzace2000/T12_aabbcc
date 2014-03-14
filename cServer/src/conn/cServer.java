package conn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class cServer {
	
	static int port = 5001;

	ServerSocket ss;
	Vector<conn_thread> conn_client;
	
	public cServer() {
		
		try {
			ss = new ServerSocket(port);
			
			while(true) {
				
				Socket theSocket;
				synchronized (this) {
					theSocket = ss.accept();
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
	
	
	
	
}
