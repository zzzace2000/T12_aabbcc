package conn;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import GUI.conDialog;
import GUI.mainFram;
import GUI.newChatroom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Vector;

public class cClient implements Runnable{
			//members for connecting2Server
        Socket socket;
	String serverIP;
        int port;
        DataOutputStream output;
	DataInputStream input;
	//members for GUI
        mainFram frame;
	//members for operating thread
        Thread theThread = new Thread(cClient.this);
	//members about self-informaion
	String name;
        Vector<String> onList = new Vector<String>();
        String fileName;
        File file;
        
	public cClient() {
		frame = new mainFram(cClient.this);

		//conDialog dialog = new conDialog(frame, "haha");
		//dialog.setVisible(true);
		
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
			//return true;
			
		} catch (IOException e) {
			System.out.println("Client connect to Server failed");
			System.out.println(e.toString());
			
			e.printStackTrace();
		}
		//return false;
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
			System.out.println("Client: get msg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	

	private synchronized void reconnect() {
		connectToServer(serverIP, port, name);
	}

	 private void judge(String msg) {
		// TODO Auto-generated method stub
                  
                    //if Connection made
                    if (msg.startsWith("/c")) {                       
			sendPMsg("/u " + name);
                    }
                    //if name is Repeated
                    else if (msg.startsWith("/r")) {
			mainFram.showAlertDialog("Name has been occupied.");
                    }
                    //if login Succeed
                    else if (msg.startsWith("/s")) {
                        if (msg.length()>2)
                            startComm(msg.substring(3));
                        else startComm("");
                    }
                    else if (msg.startsWith("/t")){
                        recPMsg(msg.substring(3));
                    }
                    else if (msg.startsWith("/o")) {
                    	System.out.println("in /o");
                        newOnline(msg.substring(3));
                    }
                    else {
			reconnect();
                    }
          
		
	}
         
         
        //////helper functions//////
        //update online friend list
        public void newOnline(String newName){
            //.println("new friend in :"+ newName);
            frame.disFrd(newName);
        }
        
        
        //////////functions for talking///////////
        //initializing data...
        private void startComm(String friendL){
            //initialize all friends online
        	System.out.print(friendL);
            if (friendL.length()>0){
               String[] splitFrd = friendL.split(" ");
               for (String i : splitFrd)
                    onList.add(i);
               for(String i : onList)
            	   frame.disFrd(i);
            }
        }
	
        //sending
        //protocol: /t  -Broadcast, -Whisper [who]
        //raw message

        
        //message with headers(or protocols)
        public void sendPMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			System.out.println("sendPMsg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
        
        public void sendFile () {
            try {
                output.writeUTF("/f");
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                printStream.println(file.getName());
            
                System.out.print("transmitting...");
            
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            
                int readin;
                while ((readin = inputStream.read()) != -1) {
                    printStream.write(readin);
                    Thread.yield();
                }
                printStream.flush();
                printStream.close();
                inputStream.close();
                
                System.out.println("transmitting completed!");
            }
            catch(Exception e) {
                System.out.println("sendFile failed");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        
        //receiving
        private void recPMsg(String tx) {
            System.out.println("in recPMsg :"+tx);
            int parse = tx.indexOf(" ");
            String from = tx.substring(0, parse);
            String msgOnB = tx.substring(parse+1);
            //System.out.println(msgOnB);
            //frame.chatBoard.replaceSelection(from + " " + msgOnB);
            frame.disTxt(from, msgOnB);
        }
        
        
	
}
