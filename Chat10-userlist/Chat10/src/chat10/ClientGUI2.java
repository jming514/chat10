/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat10;

/**
 *
 * @author Lester
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientGUI2 extends JFrame implements ActionListener {

    /**
     * Username from loginDB must be passed to ClientGUI2
     */

    private static final long serialVersionUID = 1L;
    // defaults
    private String defaultHost;
    private int defaultPort;
    // ask for username
    private JLabel username;
    // where all messages are typed
    private JTextField messageField, userField;
    // host and port entered here
    private JTextField tfHost, tfPort;
    // buttons
    private JButton loginLogout, logout, send;
    // shows all chat messages
    private JTextArea chatScreen;
    
    private DataInputStream dInput;		
    private DataOutputStream dOutput;	
    JFrame loginFrame=new JFrame("Login");
    JLabel loginLabel=new JLabel("Username:");
    JLabel loginLabel2=new JLabel("Password:");
    
    JTextField loginText=new JTextField(15);
    JTextField loginText2=new JPasswordField(10);
    JButton loginButton = new JButton ("Login");
    JButton registrationButton = new JButton ("Register");
    Connection conn;
    JFrame registrationFrame=new JFrame("Registration");
    JLabel registrationLabel=new JLabel("First Name:");
    JLabel registrationLabel2=new JLabel("Last Name:");
    JLabel registrationLabel3=new JLabel("Username:");
    JLabel registrationLabel4=new JLabel("Password:");
    JTextField registrationText=new JTextField(15);
    JTextField registrationText2=new JTextField(15);
    JTextField registrationText3=new JTextField(15);
    JTextField registrationText4=new JPasswordField(10);
    JButton registerButton = new JButton ("Register");
    
    private boolean connected;
    private Client client;
    private loginDB login;
    // Constructor
    ClientGUI2(String host, int port) {

        super("Client Chat");
        defaultHost = host;
        defaultPort = port;

        // Setting the frame size
        setSize(500, 500);
        // Set start position
        setLocationRelativeTo(null);

        // Top panel of client GUI where host, port, username, loginLogout button are located
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JPanel infoArea = new JPanel(new GridLayout(1, 5, 1, 3));
        // Host and port
        tfHost = new JTextField(host);
        tfPort = new JTextField(Integer.toString(port));
        infoArea.add(new JLabel("Server: "));
        infoArea.add(tfHost);
        infoArea.add(new JLabel("Port: "));
        infoArea.add(tfPort);
        // Login button
        loginLogout = new JButton("Login");
        loginLogout.setText("Login");
        loginLogout.addActionListener(this);
        infoArea.add(loginLogout);
        topPanel.add(infoArea);
        // Username 
        // TO BE REMOVED || MOVE TO BELOW MESSAGEFIELD
        username = new JLabel(loginText.getText().trim().toLowerCase(), SwingConstants.CENTER);
        topPanel.add(username);
        add(topPanel, BorderLayout.NORTH);

        // Panel with chat history
        JPanel midPanel = new JPanel(new GridLayout(1, 1));
        chatScreen = new JTextArea("Welcome to the Chat\n", 80, 50);
        midPanel.add(new JScrollPane(chatScreen));
        chatScreen.setEditable(false);
        add(midPanel, BorderLayout.CENTER);

        // Bottom panel with text field and send button
        JPanel botPanel = new JPanel(new GridLayout(2, 0));
        messageField = new JTextField(10);
        messageField.setEnabled(false);
        botPanel.add(messageField);
        send = new JButton("Send");
        send.addActionListener(this);
        send.setEnabled(false);
        botPanel.add(send);
        add(botPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    // append text in JTextArea
    void append(String str) {
		chatScreen.append(str);
		chatScreen.setCaretPosition(chatScreen.getText().length() - 1);
    }
    void alert(String str) {
        
        if (str.length()<16){
            JOptionPane.showMessageDialog (null, str);
            if (str.equals("User created")==true){
                registrationFrame.dispose();}
              
        }
        
        else if (str.substring(0,16).equals("Successful Login")==true){
            JOptionPane.showMessageDialog (null, str.substring(0,16));
            String uname = str.substring(16,str.length());
            loginFrame.dispose();      
            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
            client = new Client(defaultHost, defaultPort, uname, this);
            loginLogout.setText ("Logout");
                
            if(!client.start()) 
                return;         
        }
        else {
            JOptionPane.showMessageDialog (null, str);
            if (str.equals("Connected as anonymous")==true){
                loginFrame.dispose();
                    }
        }

     
    }
    public static void main(String[] args) {
        new ClientGUI2("localhost", 1500);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == send) {
            client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, messageField.getText()));
            messageField.setText("");
            return;
        }

        if (o == loginLogout) {
           // client = new Client(defaultHost, defaultPort, defaultHost,this); 
//             client.sendMessage(new ChatMessage(ChatMessage.LOGIN, ""));

                        // to loop until LOGOUT
            if (loginLogout.getText() == "Login") {
                try {   
                    String uname = "Anonymous";
                    client = new Client(defaultHost, defaultPort, uname, this);               
                    if(!client.start()) 
                        return;
                    
                    login = new loginDB();
                    loginFrame.setSize(300,150);
                    loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    loginFrame.setVisible(true);
                    loginFrame.setLocationRelativeTo(null);
                    JPanel p = new JPanel();
                    p.add(loginLabel);
                    p.add(loginText);
                    p.add(loginLabel2);
                    p.add(loginText2);
                    p.add(loginButton);
                    p.add(registrationButton);
                    loginFrame.add(p);

                    loginButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e){
                            Connection c = null;
                            Statement stmt = null;
                            String message = loginText.getText().trim().toLowerCase() + ":" +loginText2.getText().trim().toLowerCase();
                            client.sendMessage(new ChatMessage(ChatMessage.LOGIN, message));
                            loginText2.setText("");

                            loginButton.removeActionListener(this);
                        }
                    });

                    registrationButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e){   
                            registrationFrame.setSize(300,300);
                            registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            registrationFrame.setVisible(true);
                            registrationFrame.setLocationRelativeTo(null);
                            JPanel p = new JPanel();
                            p.add(registrationLabel);
                            p.add(registrationText);
                            p.add(registrationLabel2);
                            p.add(registrationText2);
                            p.add(registrationLabel3);
                            p.add(registrationText3);
                            p.add(registrationLabel4);
                            p.add(registrationText4);
                            p.add(registerButton);
                            registrationFrame.add(p);
                            registerButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e){  
                                    String fname = registrationText.getText().trim().toLowerCase();
                                    String lname = registrationText2.getText().trim().toLowerCase();
                                    String user = registrationText3.getText().trim().toLowerCase();
                                    String pass = registrationText4.getText().trim().toLowerCase();
                                    String message = fname +":"+lname+":"+user+":"+pass;
                                    client.sendMessage(new ChatMessage(ChatMessage.REGISTER, message)); 

                                    registrationFrame.dispose();
                                    registrationText.setText("");
                                    registrationText2.setText("");
                                    registrationText3.setText("");
                                    registrationText4.setText("");

                                    registerButton.removeActionListener(this);
                                }
                            });
                        }
                    });
                } catch (Exception ex) {
                    Logger.getLogger(ClientGUI2.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageField.addActionListener(this);
            }
        }
        if (loginLogout.getText() == "Logout") {
            loginLogout.setText("Login");
            messageField.setText("");
            messageField.setEnabled(false);
            send.setEnabled(false);;
            client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
                
        } else {
                // Username passed from LoginDB
                messageField.setText("");
                messageField.setEnabled(true);
                // messageField.setFocusable(true);
                connected = true;
                tfHost.setEditable(false);
                tfPort.setEditable(false);
                send.setEnabled(true);
                loginLogout.setText("Logout");
                //client.sendMessage(new ChatMessage(ChatMessage.LOGIN, ""));
            }
    }

    // called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		// login.setEnabled(true);
		// logout.setEnabled(false);
		// whoIsIn.setEnabled(false);
		// label.setText("Enter your username below");
		// tf.setText("Anonymous");
		// // reset port number and host name as a construction time
		// tfPort.setText("" + defaultPort);
		// tfServer.setText(defaultHost);
		// // let the user change them
		// tfServer.setEditable(false);
		// tfPort.setEditable(false);
		// // don't react to a <CR> after the username
		// tf.removeActionListener(this);
		connected = false;
	}

}
