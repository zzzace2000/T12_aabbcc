package conn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import GUI.ServerFrame;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class cServer {
	
	static int port = 5001;
	
	ServerFrame serverFrame;
	ServerSocket ss;
	Vector<conn_thread> conn_client = new Vector<conn_thread>();
	ArrayList<String> nameList = new ArrayList<String>();
	
	public cServer() {
		// initialize variable
		serverFrame = new ServerFrame(this);
		serverFrame.setVisible(true);
		
		try {
			ss = new ServerSocket(port);
			
			while(true) {
								
				synchronized (this) {
					serverFrame.log("Start listening");
					Socket theSocket = ss.accept();
					
					// System show
					serverFrame.log("Client connection");
				
					conn_client.add(new conn_thread(this, theSocket));
				}
				Thread td = new Thread(conn_client.lastElement());
				td.start();
			}
			
		} catch (IOException e) {
			serverFrame.log(e.toString());
			e.printStackTrace();
		}
		
		
	}


        ////helper functions////
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
				return true;
			}
		}
		nameList.add(name);
		return false;
	}
        
         //tell all clients that a new member has joined in
        public String getAllOnl(String self){
            String allO = new String("");
            for (conn_thread i : conn_client){
                if (i.getUserName() != self){
                    allO+=" ";
                    allO+=i.getUserName();                    
                }
            }
            return allO;
        }
        public  void synNewMem (String newO) {
            for (conn_thread i : conn_client) {
                if (i.getUserName() != newO) {
                    // "/o new_name"
                    i.sendPMsg("/o "+ newO);
                }
            }
            serverFrame.disFrd(newO);
        }
        
        /////functions for transmitting/////
        
        public void broadcast(String from, String tx){
            for (conn_thread i : conn_client)
                // "/t from_name to_say"
                i.sendPMsg("/t " + from + " " + tx);
        }
        
        public void whisper(String rec, String from, String tx){
            for (conn_thread i : conn_client) {
                
                if (i.getUserName() == rec) {
                    serverFrame.log("name = "+ i.getUserName());
                    i.sendPMsg("/t " + from + " " + tx);
                }
                else if (i.getUserName() == from) {
                     i.sendPMsg("/t " + from + " " + tx);
                     serverFrame.log("name = "+ i.getUserName());
                }
            }
        }


		public void broadCast(String text) {
			for (conn_thread i : conn_client) {
                i.sendPMsg("/t -b 0 server "+text);
            }
		}
	
	
	
	
	
}
