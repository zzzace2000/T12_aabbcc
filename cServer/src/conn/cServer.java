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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class cServer {

	static int port = 5001;

	ServerFrame serverFrame;
	ServerSocket ss;
	HashMap<Integer, conn_thread> conn_client = new HashMap<Integer, conn_thread>();
	int availableID;
	Vector<Vector<Integer>> chrmList = new Vector<Vector<Integer>>();

	public cServer() {
		// initialize variable
		serverFrame = new ServerFrame(this);
		serverFrame.setVisible(true);
		availableID = 0;
		try {
			ss = new ServerSocket(port);

			while (true) {

				synchronized (this) {
					serverFrame.log("Start listening");
					Socket theSocket = ss.accept();

					// System show
					serverFrame.log("Client connection");
					conn_thread cth = new conn_thread(this, availableID,
							theSocket);
					conn_client.put(availableID, cth);
					availableID++;
					Thread td = new Thread(cth);
					td.start();

				}

			}

		} catch (IOException e) {
			serverFrame.log(e.toString());
			e.printStackTrace();
		}

	}

	// //helper functions////
	/*
	 * public void deleteClient(conn_thread theThread) { String clientName =
	 * theThread.getUserName(); if (clientName != null) {
	 * conn_client.remove(theThread); nameList.remove(clientName); } }
	 */

	/*
	 * public boolean checkName(String name) { if (chrmList.isEmpty()){
	 * Vector<String> vv = new Vector<String>(); vv.add(name); chrmList.add(vv);
	 * return false; } else { for (String n: chrmList.get(0)) { if ( n == name)
	 * { return true; } } (chrmList.get(0)).add(name); return false; } }
	 */
	public boolean checkName(String name, int ID) {
		if (chrmList.isEmpty()) {
			Vector<Integer> vv = new Vector<Integer>();
			vv.add(ID);
			chrmList.add(vv);
			Vector ret = new Vector();
			return false;
		} else {
                    for (Integer key: conn_client.keySet()) {
                        if (((conn_client.get(key)).getUserName()) != null) {
                            if (((conn_client.get(key)).getUserName()).equals(name)) {
                                return true;
                            }
                        }
                }
			
			(chrmList.get(0)).add(ID);
			return false;
		}
	}

	public Vector checkRoom(String mems) {
		String[] splMem = mems.split(" ");
		Vector<Integer> cheMem = new Vector<Integer>();
		for (String i : splMem) {
			cheMem.add(Integer.parseInt(i));
		}
		boolean ret = false;
		int rID = 0;
		for (Vector<Integer> i : chrmList) {
			if ((i.size()) == cheMem.size()) {
				int count = 0;
				for (Integer j : i) {
					if (j != cheMem.get(count)) {
						break;
					} else
						count++;
					if (count == i.size()) {
						ret = true;
					}
				}
				if (ret == true) {
					break;
				}
			}
			rID++;
		}
		if (ret == false) {
			chrmList.add(cheMem);
		}
		Vector rr = new Vector();
		rr.add(ret);
		rr.add(rID);
		return rr;
	}

	// tell all clients that a new member has joined in
	public String getAllOnl(int self) {
		String allO = new String("");
            for (Integer key: conn_client.keySet()) {
                if ((conn_client.get(key)).getUserName() != null) {
                    if (self != key){
                        allO+=" ";
                        allO+=key;
                        allO+="_";
                        allO+=((conn_client.get(key)).getUserName());
			}
		}
            }
		return allO;
	}
	
	public void removeClient(int removeID) {
		for (Integer key : conn_client.keySet()) {
			if (key == removeID) {
				conn_client.remove(key);
			}
		}
	}

	public void synNewMem(int newOID, String newOName) {
		/*
		 * for (conn_thread i : conn_client) { if (i.getUserID() != newOID) { //
		 * "/o new_name" i.sendPMsg("/o "+ newOID+"_"+newOName); } }
		 */
		for (Integer key : conn_client.keySet()) {
			if (key != newOID) {
				(conn_client.get(key))
						.sendPMsg("/o " + newOID + "_" + newOName);
			}

		}
		serverFrame.disFrd(newOName);
	}
        
        public void synMemLeave(int leaveID) {
            for (Integer key : conn_client.keySet()) {
			if (key != leaveID) {
				(conn_client.get(key))
						.sendPMsg("/d " + leaveID);
			}
		}
            
        }

	public void synNewRoom(int roomID, String roomName, String mems) {
		/*
		 * for(Integer i :chrmList.get(roomID)) { for (Integer key:
		 * conn_client.keySet()) { if (key == i) {
		 * (conn_client.get(key)).sendPMsg("/nrm -a " + " " + mems); } } }
		 */

		for (Integer key : conn_client.keySet()) {
			for (Integer i : chrmList.get(roomID)) {
				if (i == key) {
					(conn_client.get(key)).sendPMsg("/nrm -a " + roomID + "_"
							+ roomName +" "+ mems);
				}
			}
		}
	}

        public void synNewWhisper(int toID, int fromID){
            for (Integer key:conn_client.keySet()) {
                if (toID == key) {
                    (conn_client.get(key)).sendPMsg("/npr "+fromID);
                }
            }
        }
	// ///functions for transmitting/////

	public void broadcast(int roomID, String from, String tx) {
		for (Integer key : conn_client.keySet()) {
			for (Integer i : chrmList.get(roomID)) {
				if (i == key) {
					(conn_client.get(key)).sendPMsg("/t -b " + roomID + " "
							+ from + " " + tx);
				}
			}
		}
	}
        public void broadText(int roomID, int fromID, String from, String tx){
            for (Integer i : chrmList.get(roomID)) {
                if (i!=fromID) {
                    for (Integer key: conn_client.keySet()) {
                        if (i==key) {
                            (conn_client.get(key)).sendPMsg("/t -b " + roomID + " " + from + " " + tx);
                            break;
                        }
                    }
                }
            }
        }
        
        public void broadIcon(int roomID, int fromID, String from, String iconID) {
             for (Integer i : chrmList.get(roomID)) {
                 if (i!=fromID) {
                    for (Integer key: conn_client.keySet()) {
                        if (i==key) {
                            (conn_client.get(key)).sendPMsg("/p -b " + roomID + " " + from + " " + iconID);
                            break;
                        }
                    }
                }
             }
        }
 public void whisText (int rec, int from, String tx) {
            for (Integer key: conn_client.keySet()) {
                if (rec == key) {
                    (conn_client.get(key)).sendPMsg("/t -w " + from + " " + tx);
                    break;
                }
            }
        }
        
        public void whisIcon (int rec, int from , String iconID) {
             for (Integer key: conn_client.keySet()) {
                if (rec == key) {
                    (conn_client.get(key)).sendPMsg("/p -w " + from + " " + iconID);
                    break;
                }
            }
        }
        
        public void whisAskFile(int rec, int from, String fileName) {
             for (Integer key: conn_client.keySet()) {
                if (rec == key) {
                    (conn_client.get(key)).sendPMsg("/f -ox " + from + " " + fileName);
                    break;
                }
            }
        }
        
        public void whisFileSuc (int rec, int from, String fileName) {
            for (Integer key: conn_client.keySet()) {
                if (from == key) {
                    (conn_client.get(key)).sendPMsg("/f -s " + rec + " " +fileName);
                    break;
                }
            }
        }
        
        public void whisShake (int rec, int from) {
            for (Integer key: conn_client.keySet()) {
                if (key == rec) {
                    (conn_client.get(key)).sendPMsg("/! "+from);
                    break;
                }
            }
        }
        
         public void serverSendFile (int rec, String fileName) {
                for (Integer key: conn_client.keySet()) {
                    if (key == rec) {
                        (conn_client.get(key)).sendFile(fileName);
                        break;
                    }
                }
        }



	public void sendVideoRequest(int fromID, int receiverID, String fromIPAddress) {
		for (Integer k: conn_client.keySet()) {
			if (receiverID == k) {
				(conn_client.get(k)).sendPMsg("/v -ox " + fromID + " " + fromIPAddress);
				serverFrame.log("send to userID "+k+":"+ "/v -ox " + fromID + " " + fromIPAddress);
			}
		}
	}

	public void replyVideoRequest(int fromID, int receiverID, boolean answer, String IPAddress) {
		if (answer) {
			// yes
			for (Integer k: conn_client.keySet()) {
				if (receiverID == k) {
					(conn_client.get(k)).sendPMsg("/v -a y " + fromID + " " + IPAddress);
					serverFrame.log("send to userID "+k+": /v -a y " + fromID + " " + IPAddress);
				}
			}
		}
		else {
			// no
			for (Integer k: conn_client.keySet()) {
				if (receiverID == k) {
					(conn_client.get(k)).sendPMsg("/v -a n " + fromID);
					serverFrame.log("send to userID "+k+": /v -a n " + fromID);
				}
			}
		}
	}
	
	
	

}
