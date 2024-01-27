/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 *
 * @author acv
 */
import java.util.Date;
import java.sql.Timestamp;

public class DBConnection {
    
    public Connection connection;
    private static final String user = "java";
    private static final String password = "java";
    private static final String database = "jdbc:derby://localhost:1527/CourseSchedulerDBGeorgeJianggfj5101";
    private static final String DBServer = "jdbc:derby://localhost:1527";
    private static final String DBName = "CourseSchedulerDBGeorgeJianggfj5101";
    private Timestamp timestamp;
    
    public static Connection getConnection()
    {
        Connection connection = ConnectionMgr.getConnection(DBServer, DBName, user, password);
        return connection;
    }
    
    public DBConnection (Connection c, Timestamp t) {
        connection = c;
        timestamp = t;
    }
    
    public void setTimestamp(Timestamp t) {
        timestamp = t;
    }
    
}
