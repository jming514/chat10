/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat10;
import java.sql.*;
import javax.swing.*;   
import java.awt.event.*;
/**
 *
 * @author David
 */
public class loginDB {
    JFrame loginFrame=new JFrame("Login");
    JLabel loginLabel=new JLabel("Username:");
    JLabel loginLabel2=new JLabel("Password:");
    JTextField loginText=new JTextField(15);
    JTextField loginText2=new JPasswordField(10);
    JButton loginButton = new JButton ("Login");
    JButton registrationButton = new JButton ("Register");
    Connection conn;
    Statement st;
    ResultSet rs;
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
    public loginDB(){
        connect();
        createNewTable();
        Frame();
    }
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:login.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:login.db";
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
                + "id integer PRIMARY KEY,\n"
                + "	fname text NOT NULL,\n"
                + "	lname text NOT NULL,\n"
                + "	username text NOT NULL UNIQUE,\n"
                + "	password text NOT NULL\n"
                + ");";
        System.out.println("created");
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void Frame(){
        loginFrame.setSize(300,150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
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
            if (!loginText.getText().isEmpty() && !loginText2.getText().isEmpty() )
                try {
                   String user = loginText.getText().trim();
                   String pass = loginText2.getText().trim();              
                   Class.forName("org.sqlite.JDBC");
                   c = DriverManager.getConnection("jdbc:sqlite:login.db");
                   c.setAutoCommit(false);
                   System.out.println("Opened database successfully");

                   stmt = c.createStatement();
                   ResultSet rs = stmt.executeQuery( "SELECT username,password FROM user WHERE username='"+user+"'and password='"+pass+"';" );
                   int resultCount=0;
                    while ( rs.next() ) {
                        resultCount+=1;
                    }
                    if (resultCount==1){
                        JOptionPane.showMessageDialog (null, "Login Sucessful!");
                    }
                    else {
                        JOptionPane.showMessageDialog (null, "Incorrect username or password");
                    }
                    rs.close();
                    stmt.close();
                    c.close();
                }   
                catch ( Exception e1 ) {
                    System.err.println( e1.getClass().getName() + ": " + e1.getMessage() );
                    System.exit(0);
                }
            else{
                JOptionPane.showMessageDialog (null, "Invalid");
            }
        } });
        registrationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){   
                registrationFrame.setSize(300,300);
                registrationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                registrationFrame.setVisible(true);
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
                        Connection c = null;             
                        String sql = "INSERT INTO user VALUES('', ?, ?,?,?)";
                        if (!registrationText.getText().isEmpty() && !registrationText2.getText().isEmpty()&& !registrationText3.getText().isEmpty() && !registrationText4.getText().isEmpty()){
                            try {
                                Class.forName("org.sqlite.JDBC");
                                c = DriverManager.getConnection("jdbc:sqlite:login.db");
                                Statement stmt = c.createStatement();
                                String fname = registrationText.getText().trim();
                                String lname = registrationText2.getText().trim();
                                String user = registrationText3.getText().trim();
                                String pass = registrationText4.getText().trim();

                                System.out.println(fname+lname+user+pass);
                                stmt.executeUpdate( "INSERT INTO user (fname,lname,username,password) VALUES('"+fname+"','"+lname+"','"+user+"','"+pass+"')");

                                System.out.println(fname+lname+user+pass);
                                registrationFrame.dispose();
                                registrationText.setText("");
                                registrationText2.setText("");
                                registrationText3.setText("");
                                registrationText4.setText("");
                                JOptionPane.showMessageDialog (null, "User Created");
                                registerButton.removeActionListener(this);
                                c.close();
                            }
                            catch ( Exception e2 ) {
                                JOptionPane.showMessageDialog (null, "Username already exist");
                             }
                        }
                        else{
                            JOptionPane.showMessageDialog (null, "Invalid");
                        }
                         }
                    } );
                    
                }
        }      
        );
    }
    public static void main(String[] args) throws Exception {
            loginDB login = new loginDB();
       }
}
