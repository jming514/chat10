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

    
           
    public loginDB(){
        //connect();
        createNewTable();
       // Frame();
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
                    + "username text NOT NULL UNIQUE,\n"
                    + "password text NOT NULL,\n"
                    + "fname text NOT NULL,\n"
                    + "lname text NOT NULL\n"
                    + ");";
            String sql2="CREATE TABLE IF NOT EXISTS chatlogs (\n"
                   + "id integer PRIMARY KEY,\n"
                    +"msg text NULL"
                    + ");";
                    
        //System.out.println("created");
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
            loginDB login = new loginDB();
       }
}
