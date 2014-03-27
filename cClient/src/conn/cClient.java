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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
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
	String userName;
        int userID;
        //Vector<String> onList = new Vector<String>();
        HashMap<Integer, String> onList= new HashMap<Integer, String>();
        //String fileName;
        //File file;
        
        String DEFAULT_FILE_PATH = "C:\\Users\\Kimberly Hsiao\\Documents\\GitHub\\T12_aabbcc\\cClient\\src\\clientFile\\";
        
	public cClient() {
		frame = new mainFram(cClient.this);

		//conDialog dialog = new conDialog(frame, "haha");
		//dialog.setVisible(true);
		
		//frame.setVisible(true);
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
		this.userName = name;
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
		connectToServer(serverIP, port, userName);
	}

	 private void judge(String msg) {
		// TODO Auto-generated method stub
                  
                    //if Connection made
                    if (msg.startsWith("/c")) {                       
			sendPMsg("/u " + userName);
                    }
                    //if name is Repeated
                    else if (msg.startsWith("/r")) {
			mainFram.showAlertDialog("Name has been occupied.");
                    }
                    //if login Succeed
                    else if (msg.startsWith("/s")) {
                        frame.setVisible(true);
                        int space1 = (msg.substring(3)).indexOf(" ");
                        //if there is other people already online
                        if (space1 != -1) {
                            userID = Integer.parseInt(msg.substring(3,space1+3));
                            System.out.println("ID = "+userID);
                            startComm(msg.substring(space1+4));
                        }
                        else startComm("");
                    }
                    else if (msg.startsWith("/t")){
                        recPMsg(msg.substring(3));
                    }
                    else if (msg.startsWith("/p")) {
                        recPIcon(msg.substring(3));
                    }
                    else if (msg.startsWith("/o")) {
                    	System.out.println("in /o");
                        newOnline(msg.substring(3));
                    }
                    else if (msg.startsWith("/f")) {
                        String remain = msg.substring(3);
                        if (remain.startsWith("-s")) {
                           int space1 = remain.indexOf(" ");
                            String remain2 = remain.substring(space1+1);
                            int space2 = remain2.indexOf(" ");
                            int ID = Integer.parseInt(remain2.substring(0, space2));
                            String fileName = remain2.substring(space2+1);
                            frame.askPriFile(ID, fileName);
                        }
                        // ask client whether to receive file or not
                        else if (remain.startsWith("-ox")){
                            int space1 = remain.indexOf(" ");
                            String remain2 = remain.substring(space1+1);
                            int space2 = remain2.indexOf(" ");
                            int ID = Integer.parseInt(remain2.substring(0, space2));
                            String fileName = remain2.substring(space2+1);
                            frame.askPriFile(ID, fileName);
                        }
                        //recieve file
                        else if (remain.startsWith("-r")) {
                            recFile(remain.substring(3));
                        }
                    }
                    else if (msg.startsWith("/nrm")) {
                        String remain = msg.substring(5);
                        if (remain.startsWith("-o")){
                            
                        }
                       /* else if (remain.startsWith("-n")){
                            int space1 = remain.indexOf(" ");
                            String remain2 = remain.substring(space1+1);
                            int space2 = remain2.indexOf(" ");
                            int rID = Integer.parseInt(remain2.substring(0,space2));
                            String members = remain2.substring(space2+1);
                            frame.newChR(rID, members);
                        }*/
                        else if (remain.startsWith("-a")) {
                            int space1 = remain.indexOf(" ");
                            String remain2 = remain.substring(space1+1);
                            int space2 = remain2.indexOf(" ");
                            String room = remain2.substring(0,space2);
                            String[] splRoom = room.split("_");
                            int rID = Integer.parseInt(splRoom[0]);
                            String rName = splRoom[1];
                            String members = remain2.substring(space2+1);
                            frame.newChR(rID,rName, members);
                        }
                            
                    }
                    else if (msg.startsWith("/npr")) {
                        int talkTo = Integer.parseInt(msg.substring(5));
                        frame.newPrivM(talkTo);
                    }
                    else if (msg.startsWith("/!")) {
                        frame.shakePriv(Integer.parseInt(msg.substring(3)));
                    }
                    else {
			reconnect();
                    }
          
		
	}
         
         
        //////helper functions//////
        //update online friend list
        public void newOnline(String newFrd){
            //onList.add(newName);
            String[] frd = newFrd.split("_");
            onList.put(Integer.parseInt(frd[0]),frd[1]);
            System.out.println("new friend in :"+ frd[1]);
            frame.disFrd(frd[1]);
        }
        
	public String getUserName() {
		if (userName != null) {
			return userName;
		}
		return null;
	}
        
        public int getUserID() {
            return userID;
        }
        
        public Vector<String> getAllOnline(){
            Vector<String> vv = new Vector<String>();
            for (Integer key: onList.keySet()) {
                 vv.add(onList.get(key));
            }
            return vv;
        }
        
        public int getPrivateID(String name) {
            int ret = -1;
            for (Integer key: onList.keySet()) {
                if(name.equals(onList.get(key))) {
                    ret = key;
                    break;
                }       
            }
            return ret;
        }
        
        public String getPrivateName(int ID) {
            String ret = "";
            for (Integer key: onList.keySet()) {
                if (key == ID) {
                    ret = (onList.get(key));
                }
            }
            return ret;
        }
        
        
        //////////functions for talking///////////
        //initializing data...
        private void startComm(String friendL) {
            //initialize all friends online

        	System.out.print(friendL);
            if (friendL.length()>0){
               String[] splitFrd = friendL.split(" ");
               for (String i : splitFrd) {
                   String[] frd = i.split("_");
                   onList.put(Integer.parseInt(frd[0]), frd[1]);
                    //onList.add(i);
               }
               //for(String i : onList)
            	   //frame.disFrd(i);
               for (Integer key: onList.keySet()) {
                   frame.disFrd(onList.get(key));
               }
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
        
        public void sendFile (int rec, File file) {
            try {
                //File file = new File(fileName);
                //would this message be separated with the file itself??
                String fileName = file.getName();
                output.writeUTF("/f -s "+ rec + " "+file.length()+ " " +fileName);
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                //printStream.println("/f -s "+ rec + " "+fileName);
            
                System.out.print("transmitting...");
            
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            
                int readin;
                while ((readin = inputStream.read()) != -1) {
                   // System.out.print("hi");
                    //output.write(readin);
                    printStream.write(readin);
                    Thread.yield();
                }
                //output.flush();
                
                printStream.flush();
                //printStream.close();
                //i have to change this into something else inorder not to close the socket but can pass end of file...
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
        //has to be refined to broadcast and whisper case
        private void recPMsg(String tx) {
            /*System.out.println("in recPMsg :"+tx);
            String[] splitTx = tx.split(" ");
            
            //seperate to braodcast and whisper case
            if (splitTx[0].equals("-b")) {
                //default in lobby
                frame.roomChat(Integer.parseInt(splitTx[1]), splitTx[2], splitTx[3]);
            }
            else if (splitTx[0].equals("-w")) {
                frame.privateChat(Integer.parseInt(splitTx[1]),splitTx[2]); 
            }*/
            
             int space1 = tx.indexOf(" ");
              String mode = tx.substring(0,space1);
              String remain1 = tx.substring(space1+1);
              int space2 = remain1.indexOf(" ");
              String ID = remain1.substring(0,space2);
              if (mode.equals("-b")) {
                  String remain2 = remain1.substring(space2+1);
                  int space3 = remain2.indexOf(" ");
                  String from = remain2.substring(0,space3);
                  String msg = remain2.substring(space3+1);
                  frame.roomChat("t", Integer.parseInt(ID), from, msg);
              }
              else if (mode.equals("-w")) {
                  String msg = remain1.substring(space2+1);
                  frame.privateChat("t",Integer.parseInt(ID),msg);
              }
              
            
           /* int parse = tx.indexOf(" ");
            String from = tx.substring(0, parse);
            String msgOnB = tx.substring(parse+1);
            //System.out.println(msgOnB);
            //frame.chatBoard.replaceSelection(from + " " + msgOnB);
            frame.disTxt(from, msgOnB);*/
        }
        
        public void recPIcon(String tx) {
            int space1 = tx.indexOf(" ");
              String mode = tx.substring(0,space1);
              String remain1 = tx.substring(space1+1);
              int space2 = remain1.indexOf(" ");
              String ID = remain1.substring(0,space2);
              if (mode.equals("-b")) {
                  String remain2 = remain1.substring(space2+1);
                  int space3 = remain2.indexOf(" ");
                  String from = remain2.substring(0,space3);
                  String msg = remain2.substring(space3+1);
                  frame.roomChat("p", Integer.parseInt(ID), from, msg);
              }
              else if (mode.equals("-w")) {
                  String msg = remain1.substring(space2+1);
                  frame.privateChat("p", Integer.parseInt(ID),msg);
              }
            
        }
        
        public void recFile(String tx) {
            try {
                int fileLength = Integer.parseInt(tx.substring(0,tx.indexOf(" ")));
                String fileName = tx.substring(tx.indexOf(" ")+1);
            
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
                //BufferedInputStream inputStream = new BufferedInputStream(input);
                 BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(DEFAULT_FILE_PATH +fileName));
                                          
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
	
}
