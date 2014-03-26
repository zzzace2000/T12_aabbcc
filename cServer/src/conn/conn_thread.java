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
import java.util.Vector;
import GUI.ServerFrame;

public class conn_thread implements Runnable{
	
	private ServerFrame mainFrame;
	private Socket sock;
	private cServer mainServer;
	private DataInputStream input;
	private DataOutputStream output;
	private String msg, username;
        private int userID;
	
	public conn_thread(cServer mainServer, int ID, Socket sock) {
                userID = ID;
		this.mainServer = mainServer;
		this.sock = sock;
		this.mainFrame = mainServer.serverFrame;
		
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
            mainFrame.log("in judge: "+ msg);

            // username
            try {
            if (msg.startsWith("/u")) {
		name = msg.substring(3);
		System.out.println(name);
               
                if ( !mainServer.checkName(name, userID) ) {
                   // Successfully create user
                    username = name;
                    mainServer.synNewMem(userID,username);
                    output.writeUTF("/s " + userID + mainServer.getAllOnl(userID));                    
                  }
                  else {
                       // tell user to change another user name
                       output.writeUTF("/r");
                        breakHand();
                   }
            }
            else if (msg.startsWith("/t")){
                transPMsg(msg.substring(3));
            }
            else if (msg.startsWith("/f")) {
                if ((msg.substring(3)).startsWith("-s"))
                    transFile(msg.substring(6));
            }
            else if (msg.startsWith("/nrm")) {
                int space = (msg.substring(5)).indexOf(" ");
                String roomName = msg.substring(5, space+5);
                
                Vector vs = mainServer.checkRoom(msg.substring(space+6));
                if (((boolean)vs.get(0)) == true) {
                    sendPMsg("/nrm -o "+ ((int)vs.get(1)));
                }
                else {
                    //sendPMsg("/nrm -n "+ ((int)vs.get(1)) + " " + msg.substring(5));
                    mainServer.synNewRoom(((int)vs.get(1)), roomName, msg.substring(5));
                }
            }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        
	}
        
        /////receiving/////
          private void transFile(String msg) {
            try{
                 //String fileName = new BufferedReader(new InputStreamReader(sock.getInputStream())).readLine();
                 //System.out.printf("receiving file: %s", fileName);
                System.out.println("in transFile " +msg);
                /*String [] splitMsg = msg.split(" ");
                String rec = splitMsg[0];
                String fileName = splitMsg[1];*/
                int space1 = msg.indexOf(" ");
                String rec = msg.substring(0, space1);//space1 not included
                String remain = msg.substring(space1+1);
                int space2 = remain.indexOf(" ");
                int fileLength = Integer.parseInt(remain.substring(0, space2));
                                            
                                            
                 BufferedInputStream inputStream = new BufferedInputStream(sock.getInputStream());
                //BufferedInputStream inputStream = new BufferedInputStream(input);
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Kimberly Hsiao\\Documents\\NetBeansProjects\\fileClient\\build\\classes\\Icon\\ironman_2.jpg"));
                                          
                 int readin;
                /* while((readin = inputStream.read()) != -1) {
                    outputStream.write(readin);
                    Thread.yield();
                 }*/
                 int count = 0;
                 while (count<fileLength) {
                     readin = inputStream.read();
                     outputStream.write(readin);
                     count++;
                     Thread.yield();
                 }
                                            
                 outputStream.flush();
                 outputStream.close();
                 //inputStream.close();
                                            
                 //sock.close();
                                            
                 System.out.println("recieving completed!");
                
            } catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
        }
        
         private void transPMsg(String msg) {
            //new version
	int space1 = msg.indexOf(" ");
              String mode = msg.substring(0,space1);
              String remain1 = msg.substring(space1+1);
              int space2 = remain1.indexOf(" ");
              String ID = remain1.substring(0,space2);
              String tx = remain1.substring(space2+1);
              if (mode.equals("-b")) {
                   System.out.println("in transPMsg: "+ msg.substring(3));
                   mainServer.broadcast(Integer.parseInt(ID), username,tx);
              }
              else if (mode.equals("-w")) {
                  mainServer.whisper(Integer.parseInt(ID), userID, tx);
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
        public int getUserID () {
            return userID;
        }

	
	
	
	
}
