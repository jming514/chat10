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
    JFrame f=new JFrame("Login");
    JLabel l=new JLabel("Username:");
    JLabel l2=new JLabel("Password:");
    JTextField t=new JTextField(15);
    JTextField t2=new JPasswordField(10);
    JButton loginButton = new JButton ("Login");
    Connection conn;
    Statement st;
    ResultSet rs;
    public loginDB(){
        //connect();
        //createNewTable();
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
                + "	id integer PRIMARY KEY,\n"
                + "	fname text NOT NULL,\n"
                + "	lname text NOT NULL,\n"
                + "	username text NOT NULL,\n"
                + "	password text NOT NULL\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     */
    public  void selectAll(){        
        try{
            String sql = "SELECT username,password FROM user";
            rs = st.executeQuery(sql);
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("username") +  "\t" + 
                                   rs.getString("password"));
            }
        } 
        catch (SQLException e) {
        }
    }
    public void Frame(){
        f.setSize(300,120);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        JPanel p = new JPanel();
        p.add(l);
        p.add(t);
        p.add(l2);
        p.add(t2);
        p.add(loginButton);
        f.add(p);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){   
            Connection c = null;
            Statement stmt = null;
            try {
               String user = t.getText().trim();
               String pass = t2.getText().trim();
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
        } }
                
        );
    }
    public static void main(String[] args) throws Exception {
            loginDB test = new loginDB();
            //test.selectAll();
       }
}
