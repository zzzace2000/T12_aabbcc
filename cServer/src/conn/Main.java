package conn;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	public static void main(String[] argv) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		cServer theServer = new cServer();
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );

	}
}
