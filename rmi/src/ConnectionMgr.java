
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */
import java.util.*;
import java.sql.Timestamp;

public class ConnectionMgr {
    private static Hashtable <String, DBConnection> table = new Hashtable<String, DBConnection>();

//    private static final String user = "java";
//    private static final String password = "java";
//    private static final String database = "jdbc:derby://localhost:1527/CourseSchedulerDBGeorgeJianggfj5101";
    private static final String DBServer = "jdbc:derby://localhost:1527";
    private static final String DBName = "CourseSchedulerDBGeorgeJianggfj5101";
    public static Connection getConnection(String DBServer, String DBName, String user, String password) {
        String DBStr = DBServer + "/" + DBName;
        DBConnection dbconnection = table.get(DBStr);
        
        if (dbconnection == null)
        {
            try
            {
                Connection connection = DriverManager.getConnection(DBStr, user, password);
                Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                DBConnection dc = new DBConnection(connection, timestamp);
                table.put(DBStr, dc);
                dbconnection = dc;
            } catch (SQLException e)
            {
                e.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);

            }
        } else {
                Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                dbconnection.setTimestamp(timestamp);
        }
        return dbconnection.connection;
    }
    
    public static void dumpConnection() {
        // iterate through hashtable
        
    }
}
