package GUI;

import java.util.Vector;

import javax.swing.*;
import javax.swing.text.StyledDocument;

class chatroomPane extends JScrollPane {
	private JTextPane innerPane;
	private int chatroomID;
	public String roomName;
	public Vector<String> members = new Vector<String>();

	chatroomPane(int ID, String tmpname, Vector<String> mm) {
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		roomName = tmpname;
		chatroomID = ID;
		members = mm;
		innerPane = new JTextPane();
		innerPane.setEditable(false);
		setViewportView(innerPane);
	}
	public void displaychats(String type, String from, String msg){
		//some more specific display stickers, text, etc
            StyledDocument doc = innerPane.getStyledDocument();
            try{
                doc.insertString(doc.getLength(),from,null);
                doc.insertString(doc.getLength(), " : ", null);
                if (type.equals("t"))
                    doc.insertString(doc.getLength(), msg, null);
                else if (type.equals("p")) {
                    innerPane.setCaretPosition(doc.getLength());
                    innerPane.insertIcon(new ImageIcon(mainFram.class.getResource("/Icon/emotion" + Integer.parseInt(msg)+ ".png")));
                    innerPane.validate();
                }
                    
                doc.insertString(doc.getLength(), "\n", null);
                System.out.println("after " + doc.getLength());
            }catch(Exception e){}
            
	}

	public int getChrID() {
		return chatroomID;
	}
}
