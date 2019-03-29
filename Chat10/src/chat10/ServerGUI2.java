/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat10;

import javax.swing.*;

import javafx.scene.layout.Border;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Lester
 */
public class ServerGUI2 extends JFrame implements ActionListener, WindowListener {
    // IP found using ipconfig
    private static final long serialVersionUID = 1L;
    private JTextField portField;
    private JButton startStop;
    private JTextArea chatLog, eventLog;
    private Server server;

    ServerGUI2(int port) {
        
        super("Chat Server");
        server = null;

        // Set up the server 
        JPanel topPanel = new JPanel(new GridLayout(0, 3));
        topPanel.add(new JLabel("Port: "));
        portField = new JTextField("" + port);
        topPanel.add(portField);
        startStop = new JButton("Start");
        startStop.addActionListener(this);
        topPanel.add(startStop);
        add(topPanel, BorderLayout.NORTH);
        // The chat and event history of the server
        JPanel midPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        chatLog = new JTextArea("Chats:\n", 10, 10);
        eventLog = new JTextArea("Events:\n", 10, 10);
        chatLog.setEditable(false);
        eventLog.setEditable(false);
        midPanel.add(new JScrollPane(eventLog));
        midPanel.add(new JScrollPane(chatLog));
        add(midPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // if running we have to stop
        if(server != null) {
            server.stop();
            server = null;
            portField.setEditable(true);
            startStop.setText("Start");
            return;
        }
        // OK start the server	
        int port;
        try {
            port = Integer.parseInt(portField.getText().trim());
        }
        catch(Exception er) {
            appendEvent("Invalid port number");
            return;
        }
        // create a new Server
//        server = new Server(port, this);
    }

    void appendEvent(String str) {
        eventLog.append(str);
        eventLog.setCaretPosition(chatLog.getText().length() - 1);
    }
    
    public void windowClosing(WindowEvent e) {}

    public static void main(String[] args) {
        new ServerGUI2(1234);
    }

    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    class ServerRunning extends Thread {
        public void run() {
            server.start();
	}
    }
}
