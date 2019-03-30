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
import java.util.logging.Level;
import java.util.logging.Logger;
import chat10.loginDB.*;

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
    private boolean connected;
    private Client client;
    
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
        loginLogout.addActionListener(this);
        infoArea.add(loginLogout);
        topPanel.add(infoArea);
        // Username 
        // TO BE REMOVED || MOVE TO BELOW MESSAGEFIELD
        username = new JLabel(">YOUR USERNAME<", SwingConstants.CENTER);
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
    
    public static void main(String[] args) {
        new ClientGUI2("192.168.2.15", 1500);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == send) {
            client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, messageField.getText()));
            messageField.setText("");
            return;
        }

        if (o == loginLogout) {
            // try {
            //     loginDB.main(new String[] {});
            // } catch (Exception ex) {
            //     Logger.getLogger(ClientGUI2.class.getName()).log(Level.SEVERE, null, ex);
            // }

            if (loginLogout.getText() == "Logout") {
                loginLogout.setText("Login");
                messageField.setText("");
                messageField.setEnabled(false);
                send.setEnabled(false);;
                client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
            } else {
                // Username passed from LoginDB
                String uname = "PLACEHOLDER";
                
                client = new Client(defaultHost, defaultPort, uname, this);

                if(!client.start()) 
                    return;
                    
                messageField.setText("");
                messageField.setEnabled(true);
                // messageField.setFocusable(true);
                connected = true;
                tfHost.setEditable(false);
                tfPort.setEditable(false);
                send.setEnabled(true);
                loginLogout.setText("Logout");
                getRootPane().setDefaultButton(send);  // Enter key = send button

            }
            
            messageField.addActionListener(this);
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
