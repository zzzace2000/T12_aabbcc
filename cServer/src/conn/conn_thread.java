package conn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
import GUI.ServerFrame;

public class conn_thread implements Runnable {

	private ServerFrame mainFrame;
	private Socket sock;
	private cServer mainServer;
	private DataInputStream input;
	private DataOutputStream output;
	private String msg, username;
	private int userID;
       String DEFAULT_FILE_PATH = "./";
	public conn_thread(cServer mainServer, int ID, Socket sock) {
                userID = ID;
                username = null;
		this.mainServer = mainServer;
		this.sock = sock;
		this.mainFrame = mainServer.serverFrame;

		try {
			input = new DataInputStream(sock.getInputStream());
			output = new DataOutputStream(sock.getOutputStream());

		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	// ///thread operation/////
	@Override
	public void run() {
		try {
			// output success message
			output.writeUTF("/c");
			while (true) {
				String msg = input.readUTF();
				judge(msg);
			}
		} catch (IOException e) {
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
		String name = null;
		mainFrame.log("in judge: " + msg);

		// username
		try {
			if (msg.startsWith("/u")) {
				name = msg.substring(3);
				System.out.println(name);

				if (!mainServer.checkName(name, userID)) {
					// Successfully create user
					username = name;
					mainServer.synNewMem(userID, username);
					output.writeUTF("/s " + userID + mainServer.getAllOnl(userID));
				}
				else {
					// tell user to change another user name
					output.writeUTF("/r");
					breakHand();
				}
			}
			else if (msg.startsWith("/t")) {
System.out.println("in judge: "+ msg);
				transPMsg(msg.substring(3));
			}
            else if (msg.startsWith("/p")){
                transPIcon(msg.substring(3));
            }
			else if (msg.startsWith("/f")) {
				if ((msg.substring(3)).startsWith("-s"))
					transFile(msg.substring(6));
                	    else if ((msg.substring(3)).startsWith("-r")) {
                    		String fileName = msg.substring(6);
                   		// File file = new File(DEFAULT_FILE_PATH+fileName);
                    		mainServer.serverSendFile(userID, fileName);
                	    }
			}
			else if (msg.startsWith("/nrm")) {
				int space = (msg.substring(5)).indexOf(" ");
				String roomName = msg.substring(5, space + 5);

				Vector vs = mainServer.checkRoom(msg.substring(space + 6));
				if (((boolean) vs.get(0)) == true) {
					sendPMsg("/nrm -o " + ((int) vs.get(1)));
				}
				else {
					// sendPMsg("/nrm -n "+ ((int)vs.get(1)) + " " +
					// msg.substring(5));
					mainServer.synNewRoom(((int) vs.get(1)), roomName, msg.substring(5));
				}
			}
            else if (msg.startsWith("/npr")) {
                int toID = Integer.parseInt(msg.substring(5));
                mainServer.synNewWhisper(toID, userID);
            }
            else if (msg.startsWith("/!")) {
                int toID = Integer.parseInt(msg.substring(3));
                mainServer.whisShake(toID, userID);
            }
			else if (msg.startsWith("/v")) {
				String[] tmp = msg.split(" ");
				if (tmp.length > 0) {
					// video request
					if (tmp[1].equals("-s")) {
						int receiverID = Integer.parseInt(tmp[2]);
						String invitePersonIP = tmp[3];
						sendVideoRequest(receiverID, invitePersonIP);
					}
					// reply
					else if (tmp[1].equals("-r")) {
						if (tmp[2].equals("y")) {
							int talkToID = Integer.parseInt(tmp[3]);
							String FromIPAddress = tmp[4];
							mainServer.replyVideoRequest(userID, talkToID, true, FromIPAddress);
						}
						else if (tmp[2].equals("n")) {
							int talkToID = Integer.parseInt(tmp[3]);
							mainServer.replyVideoRequest(userID, talkToID, false, "");
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private void sendVideoRequest(int receiverID, String invitePersonIP) {
		mainServer.sendVideoRequest(userID, receiverID, invitePersonIP);
	}

	// ///receiving/////
	private void transFile(String msg) {
		try {
			// String fileName = new BufferedReader(new
			// InputStreamReader(sock.getInputStream())).readLine();
			// System.out.printf("receiving file: %s", fileName);
			System.out.println("in transFile " + msg);
			 int space1 = msg.indexOf(" ");
                int rec = Integer.parseInt(msg.substring(0, space1));//space1 not included
                String remain = msg.substring(space1+1);
                int space2 = remain.indexOf(" ");
                int fileLength = Integer.parseInt(remain.substring(0, space2));
                String remain2 = remain.substring(space2+1);
                int space3 = remain2.indexOf(" ");
                String fileName = remain2.substring(space3+1);
			BufferedInputStream inputStream = new BufferedInputStream(sock.getInputStream());
			// BufferedInputStream inputStream = new BufferedInputStream(input);
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(
							DEFAULT_FILE_PATH +fileName));

			int readin;
			/*
			 * while((readin = inputStream.read()) != -1) {
			 * outputStream.write(readin); Thread.yield(); }
			 */
			int count = 0;
			while (count < fileLength) {
				readin = inputStream.read();
				outputStream.write(readin);
				count++;
				Thread.yield();
			}

			outputStream.flush();
			outputStream.close();
			// inputStream.close();

			// sock.close();

			System.out.println("recieving completed!");
	             mainServer.whisAskFile(rec, userID, fileName);
                     mainServer.whisFileSuc(rec, userID, fileName);

		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private void transPMsg(String msg) {
		// new version
		int space1 = msg.indexOf(" ");
		String mode = msg.substring(0, space1);
		String remain1 = msg.substring(space1 + 1);
		int space2 = remain1.indexOf(" ");
		String ID = remain1.substring(0, space2);
		String tx = remain1.substring(space2 + 1);
		if (mode.equals("-b")) {
			System.out.println("in transPMsg: " + msg.substring(3));
			mainServer.broadText(Integer.parseInt(ID), userID, username,tx);
		}
		else if (mode.equals("-w")) {
			mainServer.whisText(Integer.parseInt(ID), userID, tx);
		}

		// original version
		/*
		 * try { if (msg.startsWith("-b")){ //System.out.println("-b"+ msg);
		 * System.out.println("in transPMsg: "+ msg.substring(3));
		 * mainServer.broadcast(msg.substring(3)); } output.writeUTF(msg); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}
         public void transPIcon(String msg) {
              int space1 = msg.indexOf(" ");
              String mode = msg.substring(0,space1);
              String remain1 = msg.substring(space1+1);
              int space2 = remain1.indexOf(" ");
              String ID = remain1.substring(0,space2);
              String iconID = remain1.substring(space2+1);
              if (mode.equals("-b")) {
                   //System.out.println("in transPMsg: "+ msg.substring(3));
                   mainServer.broadIcon(Integer.parseInt(ID), userID, username,iconID);
              }
              else if (mode.equals("-w")) {
                  mainServer.whisIcon(Integer.parseInt(ID), userID, iconID);
              }
             
         }

	// ///sending/////
	public void sendPMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
        
        public void sendFile (String fileName) {
             try {
                //File file = new File(fileName);
                //would this message be separated with the file itself??
                File file = new File(DEFAULT_FILE_PATH + fileName);
                output.writeUTF("/f -r "+file.length()+ " " +fileName);
                PrintStream printStream = new PrintStream(sock.getOutputStream());
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

        ////helper function//// 
	public String getUserName() {
		if (username != null) {
			return username;
		}
		return null;
	}

	public int getUserID() {
		return userID;
	}

}
