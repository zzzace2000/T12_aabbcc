package conn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

        
        
       /////thread operation///// 
	@Override
	public void run() {
		try {
		// output success message
                    output.writeUTF("/c");
                    while (true) {
                        String msg = input.readUTF();
                        judge(msg);
                    } 
                }
		catch (IOException e) {
			System.out.println("Server: User name check phase failed");
			e.printStackTrace();
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
            String name= null;
            // username
            try {
            if (msg.startsWith("/u")) {
		name = msg.substring(3);
		System.out.println(name);
                if (!mainServer.checkName(name)) {
                   // Successfully create user
                    username = name;
                    mainServer.synNewMem(username);
                    output.writeUTF("/s" + mainServer.getAllOnl(name));
                    
                  }
                  else {
                       // tell user to change another user name
                       output.writeUTF("/r");
                        breakHand();
                   }
            }
            else if (msg.startsWith("/t")){
                System.out.println("in judge: "+ msg);
                transPMsg(msg.substring(3));
            }
            else if (msg.startsWith("/f")) {
                transFile();
            }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        
	}
        
        /////receiving/////
          private void transFile() {
            try{
                 String fileName = new BufferedReader(new InputStreamReader(sock.getInputStream())).readLine();
                 System.out.printf("receiving file: %s", fileName);
                                            
                 BufferedInputStream inputStream = new BufferedInputStream(sock.getInputStream());
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName));
                                          
                 int readin;
                 while((readin = inputStream.read()) != -1) {
                    outputStream.write(readin);
                    Thread.yield();
                 }
                                            
                 outputStream.flush();
                 outputStream.close();
                 inputStream.close();
                                            
                 System.out.println("recieving completed!");
                
            } catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
        }
        
         private void transPMsg(String msg) {
            //new version
                 if (msg.startsWith("-b")) {
                         //System.out.println("-b"+ msg);
                        System.out.println("in transPMsg: "+ msg.substring(3));
                        mainServer.broadcast(username,msg.substring(3));
                    }
                 else if (msg.startsWith("-w")) {
                     String[] splitMsg = msg.split(" ");
                     mainServer.whisper(splitMsg[1], username, splitMsg[2]);
                 }
            
            //original version
            /*try {
                    if (msg.startsWith("-b")){
                         //System.out.println("-b"+ msg);
                        System.out.println("in transPMsg: "+ msg.substring(3));
                        mainServer.broadcast(msg.substring(3));
                    }
                    output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
                    e.printStackTrace();
		}*/
	}
        
        /////sending/////
	public void sendPMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

        ////helper function//// 
	public String getUserName() {
		if (username != null) {
			return username;
                }
		return null;
	}

	
	
	
	
}
