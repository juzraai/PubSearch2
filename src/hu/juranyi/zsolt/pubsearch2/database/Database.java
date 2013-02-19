/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.juranyi.zsolt.pubsearch2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Zsolt
 */
public class Database {

    private static Connection connection;

    public static boolean connect(String host, String user, String pass) {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host, user, pass);
            System.out.println("Database server connection ready.");
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database server " + host + " with user " + user + ".\n" + ex.getMessage());
        } finally {
            return connection != null;
        }
    }

    public static void useDatabase(String database) {
        try {
            Statement s = getConnection().createStatement();            
            s.execute(String.format("CREATE DATABASE IF NOT EXISTS %s;", database));
            s.execute(String.format("USE %s;", database));
            System.out.println("Database is ready.");
        } catch (Exception ex) {
            System.out.println("Cannot create and/or use database.\n" + ex.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
