package conn;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import GUI.conDialog;
import GUI.mainFram;
import GUI.newChatroom;
import GUI.privateMessage;
import GUI.videoDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import readWebcamUsingUDP.MainRecorder;

public class cClient implements Runnable {
	// members for connecting2Server
	Socket socket;
	String serverIP;
	int port;
	DataOutputStream output;
	DataInputStream input;
	// members for GUI
	mainFram frame;
	// members for operating thread
	Thread theThread = new Thread(cClient.this);
	// members about self-informaion
	String userName;
	int userID;
	// Vector<String> onList = new Vector<String>();
	HashMap<Integer, String> onList = new HashMap<Integer, String>();
	private String LOCAL_IP_ADDRESS;

	// String fileName;
	// File file;

	public cClient() {
		frame = new mainFram(cClient.this);

		// conDialog dialog = new conDialog(frame, "haha");
		// dialog.setVisible(true);

		frame.setVisible(true);
		
		LOCAL_IP_ADDRESS = getLocalAddress();
		/*
		 * EventQueue.invokeLater(new Runnable() { public void run() { try {
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } } });
		 */
	}

	public void connectToServer(String serverIP, int port, String name) {

		this.serverIP = serverIP;
		this.port = port;
		this.userName = name;
		System.out.println("Client connects Server with IP: " + serverIP + ", port: " + port);
		try {
			socket = new Socket(InetAddress.getByName(serverIP), port);
			System.out.println("Connection successed");

			output = new DataOutputStream(socket.getOutputStream());
			input = new DataInputStream(socket.getInputStream());

			Thread theThread = new Thread(cClient.this);
			theThread.start();
			// return true;

		} catch (IOException e) {
			System.out.println("Client connect to Server failed");
			System.out.println(e.toString());

			e.printStackTrace();
		}
		// return false;
	}

	public void run() {
		try {
			while (true) {

				String msg = input.readUTF();
				System.out.println(msg);
				judge(msg);
			}
		} catch (IOException e) {
			// reconnect();
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

		// if Connection made
		if (msg.startsWith("/c")) {
			sendPMsg("/u " + userName);
		}
		// if name is Repeated
		else if (msg.startsWith("/r")) {
			mainFram.showAlertDialog("Name has been occupied.");
		}
		// if login Succeed
		else if (msg.startsWith("/s")) {
			int space1 = (msg.substring(3)).indexOf(" ");
			// if there is other people already online
			if (space1 != -1) {
				userID = Integer.parseInt(msg.substring(3, space1 + 3));
				System.out.println("ID = " + userID);
				startComm(msg.substring(space1 + 4));
			}
			else
				startComm("");
		}
		else if (msg.startsWith("/t")) {
			recPMsg(msg.substring(3));
		}
		else if (msg.startsWith("/o")) {
			System.out.println("in /o");
			newOnline(msg.substring(3));
		}
		else if (msg.startsWith("/f")) {
			String remain = msg.substring(3);
			// success??
			if (remain.startsWith("-s")) {

			}
			// ask client whether to receive file or not
			else if (remain.startsWith("-ox")) {

			}
			// send file to client
			else if (remain.startsWith("-r")) {

			}
		}
		else if (msg.startsWith("/nrm")) {
			String remain = msg.substring(5);
			if (remain.startsWith("-o")) {

			}
			/*
			 * else if (remain.startsWith("-n")){ int space1 =
			 * remain.indexOf(" "); String remain2 = remain.substring(space1+1);
			 * int space2 = remain2.indexOf(" "); int rID =
			 * Integer.parseInt(remain2.substring(0,space2)); String members =
			 * remain2.substring(space2+1); frame.newChR(rID, members); }
			 */
			else if (remain.startsWith("-a")) {
				int space1 = remain.indexOf(" ");
				String remain2 = remain.substring(space1 + 1);
				int space2 = remain2.indexOf(" ");
				String room = remain2.substring(0, space2);
				String[] splRoom = room.split("_");
				int rID = Integer.parseInt(splRoom[0]);
				String rName = splRoom[1];
				String members = remain2.substring(space2 + 1);
				frame.newChR(rID, rName, members);
			}

		}
		else if (msg.startsWith("/v")) {
			String[] tmp = msg.split(" ");
			// if receiving a request asks you
			if (tmp.length > 0) {
				if (tmp[1].equals("-ox")) {
					int fromID = Integer.parseInt(tmp[2]);
					String fromIPAddress = tmp[3];
					String friendName = getPrivateName(fromID);
					videoDialog.showDialog(frame, friendName, cClient.this, fromID, fromIPAddress);
				}
				else if (tmp[1].equals("-a")) {
					int fromID = Integer.parseInt(tmp[3]);
					
					if (tmp[2].equals("y")) { // yes
						String ToIPAddress = tmp[4];
						startVideoTalk(ToIPAddress);
					}
					else {
						// show failed message
						frame.privateChat(fromID, "Video request Rejected!");
					}
					
				}
			}
		}
		else {
			reconnect();
		}

	}

	// ////helper functions//////
	// update online friend list
	public void newOnline(String newFrd) {
		// onList.add(newName);
		String[] frd = newFrd.split("_");
		onList.put(Integer.parseInt(frd[0]), frd[1]);
		System.out.println("new friend in :" + frd[1]);
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

	public Vector<String> getAllOnline() {
		Vector<String> vv = new Vector<String>();
		for (Integer key : onList.keySet()) {
			vv.add(onList.get(key));
		}
		return vv;
	}

	public int getPrivateID(String name) {
		int ret = -1;
		for (Integer key : onList.keySet()) {
			if (name.equals(onList.get(key))) {
				ret = key;
				break;
			}
		}
		return ret;
	}

	public String getPrivateName(int ID) {
		String ret = "";
		for (Integer key : onList.keySet()) {
			if (key == ID) {
				ret = (onList.get(key));
			}
		}
		return ret;
	}

	// ////////functions for talking///////////
	// initializing data...
	private void startComm(String friendL) {
		// initialize all friends online
		System.out.print(friendL);
		if (friendL.length() > 0) {
			String[] splitFrd = friendL.split(" ");
			for (String i : splitFrd) {
				String[] frd = i.split("_");
				onList.put(Integer.parseInt(frd[0]), frd[1]);
				// onList.add(i);
			}
			// for(String i : onList)
			// frame.disFrd(i);
			for (Integer key : onList.keySet()) {
				frame.disFrd(onList.get(key));
			}
		}
	}

	// sending
	// protocol: /t -Broadcast, -Whisper [who]
	// raw message

	// message with headers(or protocols)
	public void sendPMsg(String msg) {
		try {
			output.writeUTF(msg);
		} catch (IOException e) {
			System.out.println("sendPMsg failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public void sendFile(int rec, File file) {
		try {
			// File file = new File(fileName);
			// would this message be separated with the file itself??
			String fileName = file.getName();
			output.writeUTF("/f -s " + rec + " " + file.length() + " " + fileName);
			PrintStream printStream = new PrintStream(socket.getOutputStream());
			// printStream.println("/f -s "+ rec + " "+fileName);

			System.out.print("transmitting...");

			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			int readin;
			while ((readin = inputStream.read()) != -1) {
				// System.out.print("hi");
				// output.write(readin);
				printStream.write(readin);
				Thread.yield();
			}
			// output.flush();

			printStream.flush();
			// printStream.close();
			// i have to change this into something else inorder not to close
			// the socket but can pass end of file...
			inputStream.close();

			System.out.println("transmitting completed!");
		} catch (Exception e) {
			System.out.println("sendFile failed");
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	// receiving
	// has to be refined to broadcast and whisper case
	private void recPMsg(String tx) {
		/*
		 * System.out.println("in recPMsg :"+tx); String[] splitTx =
		 * tx.split(" ");
		 * 
		 * //seperate to braodcast and whisper case if (splitTx[0].equals("-b"))
		 * { //default in lobby frame.roomChat(Integer.parseInt(splitTx[1]),
		 * splitTx[2], splitTx[3]); } else if (splitTx[0].equals("-w")) {
		 * frame.privateChat(Integer.parseInt(splitTx[1]),splitTx[2]); }
		 */

		int space1 = tx.indexOf(" ");
		String mode = tx.substring(0, space1);
		String remain1 = tx.substring(space1 + 1);
		int space2 = remain1.indexOf(" ");
		String ID = remain1.substring(0, space2);
		if (mode.equals("-b")) {
			String remain2 = remain1.substring(space2 + 1);
			int space3 = remain2.indexOf(" ");
			String from = remain2.substring(0, space3);
			String msg = remain2.substring(space3 + 1);
			frame.roomChat(Integer.parseInt(ID), from, msg);
		}
		else if (mode.equals("-w")) {
			String msg = remain1.substring(space2 + 1);
			frame.privateChat(Integer.parseInt(ID), msg);
		}

		/*
		 * int parse = tx.indexOf(" "); String from = tx.substring(0, parse);
		 * String msgOnB = tx.substring(parse+1); //System.out.println(msgOnB);
		 * //frame.chatBoard.replaceSelection(from + " " + msgOnB);
		 * frame.disTxt(from, msgOnB);
		 */
	}

	public void sendVideoRequest(int talkTo) {
		sendPMsg("/v -s " + talkTo + " " + LOCAL_IP_ADDRESS);
	}

	public void replyVideoRequest(int talkToID, boolean accept, String ToIPAddress) {
		if (accept) {
			sendPMsg("/v -r y "+talkToID);
			startVideoTalk(ToIPAddress);
		}
		else {
			sendPMsg("/v -r n "+talkToID);
		}
	}
	
	public void startVideoTalk(String ToIPAddress) {
		new MainRecorder(ToIPAddress);
	}
	
	private String getLocalAddress() {
		String theAddresString;
		try {
			theAddresString = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		System.out.println(theAddresString);
		return theAddresString;
	}

}
