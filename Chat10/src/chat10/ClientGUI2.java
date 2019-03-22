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
    private JButton login, logout, send;
    // shows all chat messages
    private JTextArea chatScreen;
    
    // Constructor
    ClientGUI2(String host, int port) {

        super("Client Chat");
        defaultHost = host;
        defaultPort = port;

        // Setting the frame size
        setSize(500, 500);
        // Set start position
        setLocationRelativeTo(null);

        // Top panel of client GUI where host, port, username, login button are located
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        JPanel infoArea = new JPanel(new GridLayout(1, 5, 1, 3));
        // Host and port
        tfHost = new JTextField(host);
        tfPort = new JTextField(Integer.toString(port));
        infoArea.add(new JLabel("Server: "));
        infoArea.add(tfHost);
        infoArea.add(new JLabel("Port: "));
        infoArea.add(tfPort);
        // Login button
        login = new JButton("Login");
        login.addActionListener(this);
        infoArea.add(login);
        topPanel.add(infoArea);
        // Username
        username = new JLabel("Enter your username below", SwingConstants.CENTER);
        topPanel.add(username);
        userField = new JTextField();
        topPanel.add(userField);
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
        send.setEnabled(false);
        botPanel.add(send);
        add(botPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ClientGUI2("localhost", 1995);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == login) {
            try {
                loginDB.main(new String[] {});
            } catch (Exception ex) {
                Logger.getLogger(ClientGUI2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
