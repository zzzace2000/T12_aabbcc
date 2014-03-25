package GUI;

import conn.cClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class privateMessage extends JFrame {

        private cClient theClient;
        private String talkTo;
	private JPanel contentPane;
	private JTextField textField;
	private JButton sendButton;
        private final static 
        String DEFAULT_FILE_PATH = "C:\\Users\\Kimberly Hsiao\\Documents\\NetBeansProjects\\fileClient\\build\\classes\\Icon";
        
	/**
	 * Create the frame.
	 */
	public privateMessage(cClient cc, String rec) {
                theClient = cc;
                talkTo = rec;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 481, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(50,90));
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(211, 5, 2, 0);
		separator.setOrientation(SwingConstants.VERTICAL);
		panel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(89, 7, 1, 21);
		panel.add(separator_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(37, 5, 42, 23);
		panel.add(comboBox);
		
		JButton button = new JButton("");
		button.setBounds(10, 5, 29, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("strickers");
		button_1.setBounds(100, 5, 84, 23);
		panel.add(button_1);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				if(textField.getText().length() != 0)
					sendButton.setEnabled(true);
			}
		});
		textField.setColumns(10);
		textField.setBounds(10, 32, 377, 49);
		panel.add(textField);
		
		sendButton = new JButton("");
		sendButton.setEnabled(false);
		sendButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/tosend.png")));
		sendButton.setBounds(397, 32, 49, 49);
		panel.add(sendButton);
                sendButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(textField.getText());
                        //chatBoard.replaceSelection("hello!!");
                        theClient.sendPMsg("/t -w "+ talkTo + " " + textField.getText());
                        
                    }
                });
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(194, 7, 1, 21);
		panel.add(separator_2);
		
		JButton soundButton = new JButton("");
		soundButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/soundicon.png")));
		soundButton.setBounds(202, 5, 29, 23);
		panel.add(soundButton);
		
		JButton voiceButton = new JButton("");
		voiceButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/mic.png")));
		voiceButton.setBounds(229, 5, 29, 23);
		panel.add(voiceButton);
		
		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/video.png")));
		button_2.setBounds(256, 5, 29, 23);
		panel.add(button_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(295, 7, 1, 21);
		panel.add(separator_3);
		
		JButton filebutton = new JButton("");
		filebutton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/file.png")));
		filebutton.setBounds(306, 5, 29, 23);
		panel.add(filebutton);
                filebutton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println("hi sendfile");
                        //String fileName = JOptionPane.showInputDialog(null,"sendingFile");                        
                        String fileName = "";
                        File file = new File("");
                       
                        JFileChooser fcObj = new JFileChooser(DEFAULT_FILE_PATH); 
                        int result = -1;
			fcObj.setDialogTitle("開啟舊檔");
                        result = fcObj.showOpenDialog(privateMessage.this);
                        if(result == JFileChooser.APPROVE_OPTION){
                            file = fcObj.getSelectedFile();
                            //取得檔案選擇器選取的檔案
                            //fileName = file.getName();                            

                    }
			else if(result == JFileChooser.CANCEL_OPTION){

			} 
                        theClient.sendFile (talkTo, file);
                    }
                });
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel messagePane = new JPanel();
		messagePane.setBackground(Color.WHITE);
		messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(messagePane);
	}
        
}
