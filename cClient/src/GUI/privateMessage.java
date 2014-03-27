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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
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
        private int talkTo;
	private JPanel contentPane;
	private JTextField textField;
	private JButton sendButton;
	private JPanel panel ;
	private videoFrame videochat;
        private final static 
        String DEFAULT_FILE_PATH = "C:\\Users\\Kimberly Hsiao\\Documents\\NetBeansProjects\\fileClient\\build\\classes\\Icon";
        
	/**
	 * Create the frame.
	 */
	public privateMessage(cClient cc, int rec) {
                theClient = cc;
                talkTo = rec;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 481, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panel = new JPanel();
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
		separator_2.setBounds(104, 7, 1, 21);
		panel.add(separator_2);
		
		JButton button_2 = new JButton("");
		button_2.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/video.png")));
		button_2.setBounds(169, 5, 29, 23);
		panel.add(button_2);
		
		JButton filebutton = new JButton("");
		filebutton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/file.png")));
		filebutton.setBounds(118, 5, 29, 23);
		panel.add(filebutton);
		
		JButton stickerButton = new JButton("");
		stickerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPopupMenu tmpMenu = new JPopupMenu();
				for(int i = 1; i < 7; ++i){
					JMenuItem tmp = new JMenuItem(new ImageIcon(mainFram.class.getResource("/Icon/emotion" + i + "-key.png")));
					tmp.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							//send stickers by cClient
						}
					});
					tmpMenu.add(tmp);
				}
				tmpMenu.show(panel,10,9);
			}
		});
		stickerButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/emotionButtonIcon.png")));
		stickerButton.setBounds(10, 5, 87, 23);
		panel.add(stickerButton);
		
		JSeparator separator1 = new JSeparator();
		separator1.setOrientation(SwingConstants.VERTICAL);
		separator1.setBounds(157, 6, 1, 21);
		panel.add(separator1);
		
		JButton videoButton = new JButton("");
		videoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//open videoFrame;
			}
		});
		videoButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/video.png")));
		videoButton.setBounds(168, 5, 29, 23);
		panel.add(videoButton);
		
		JSeparator separator2 = new JSeparator();
		separator2.setOrientation(SwingConstants.VERTICAL);
		separator2.setBounds(208, 6, 1, 21);
		panel.add(separator2);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int originalX = privateMessage.this.getLocationOnScreen().x;
					int originalY = privateMessage.this.getLocationOnScreen().y;
					for(int i = 0; i < 10; ++i){
						Thread.sleep(10);
						privateMessage.this.setLocation(originalX, originalY+5);
						Thread.sleep(10);
						privateMessage.this.setLocation(originalX, originalY-5);
						Thread.sleep(10);
						privateMessage.this.setLocation(originalX+5, originalY);
						Thread.sleep(10);
						privateMessage.this.setLocation(originalX, originalY);
					}
				}catch(Exception err){
					err.printStackTrace();
				}
			}
		});
		btnNewButton.setIcon(new ImageIcon(privateMessage.class.getResource("/Icon/vibrate.png")));
		btnNewButton.setBounds(219, 6, 29, 23);
		panel.add(btnNewButton);
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
                            //?��?檔�??��??�選?��?檔�?
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
