package conn;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		cClient theClient = new cClient();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		// theClient.connectToServer();

	}

}
