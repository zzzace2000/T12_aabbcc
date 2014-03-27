package GUI;
import java.util.Vector;
import javax.swing.*;
import javax.swing.text.StyledDocument;

class chatroomPane extends JScrollPane{
	private JTextPane innerPane ;
	private int chatroomID;
	public String roomName;
        private Vector<String> members = new Vector<String>();
	chatroomPane(int ID, String tmpname, Vector<String> mm){
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		roomName = tmpname;
                chatroomID = ID;
                members = mm;
		innerPane = new JTextPane();
		innerPane.setEditable(false);
		setViewportView(innerPane);
	}
	public void displaychats(String from, String msg){
		//some more specific display stickers, text, etc
            StyledDocument doc = innerPane.getStyledDocument();
            try{
                doc.insertString(doc.getLength(),from,null);
                doc.insertString(doc.getLength(), " : ", null);
                doc.insertString(doc.getLength(), msg, null);
                doc.insertString(doc.getLength(), "\n", null);
            }catch(Exception e){}
            
	}
        
        public int getChrID() {
            return chatroomID;
        }
}
