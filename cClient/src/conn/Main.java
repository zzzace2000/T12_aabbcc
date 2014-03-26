package conn;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class Main {
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

		cClient theClient = new cClient();
		
		//theClient.connectToServer();
		
		
	}
	
	
	
	
}
