package GUI;
import javax.swing.*;

class chatroomPane extends JScrollPane{
	private JTextPane innerPane ;
	public String name;
	chatroomPane(String tmpname){
		super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		name = tmpname;
		innerPane = new JTextPane();
		innerPane.setEditable(false);
		setViewportView(innerPane);
	}
	public void displaychats(){
		//some more specific display stickers, text, etc
	}
}
