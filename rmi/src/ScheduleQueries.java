/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ScheduleQueries {
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement getScheduledStudentsByCourse;
    private static PreparedStatement getWaitlistedStudentsByCourse;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement dropScheduleByCourse;
    private static PreparedStatement updateScheduleEntry;
    
    private static ResultSet resultSet;
//    private static final String DBServer = "jdbc:derby://localhost:1527";
//    private static final String DBName = "CourseSchedulerDBGeorgeJianggfj5101";
 
    public static void addScheduleEntry(ScheduleEntry schedule) {
        connection = DBConnection.getConnection();
        try {
            addScheduleEntry = connection.prepareStatement("INSERT into app.schedule (semester, coursecode, studentid, status, timestamp) values (?,?,?,?,?)");
            addScheduleEntry.setString(1, schedule.getSemester());
            addScheduleEntry.setString(2, schedule.getCourseCode());
            addScheduleEntry.setString(3, schedule.getStudentID());
            addScheduleEntry.setString(4, schedule.getStatus());
            addScheduleEntry.setTimestamp(5, schedule.getTimestamp());
            addScheduleEntry.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String StudentID) {
        connection = DBConnection.getConnection();

        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        try {
            getScheduleByStudent = connection.prepareStatement("select semester, studentid, coursecode, status, timestamp from app.schedule where semester = ? and studentid = ? order by coursecode");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, StudentID);
            resultSet = getScheduleByStudent.executeQuery();
            while(resultSet.next()) {
                schedule.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
                
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return schedule;
    }
    
    public static int getScheduledStudentCount(String currentSemester, String courseCode) {
        connection = DBConnection.getConnection();

        int count = 0;
        try {
            getScheduledStudentCount = connection.prepareStatement("select count(studentID) from app.schedule where semester = ? and courseCode = ?");
            getScheduledStudentCount.setString(1, currentSemester);
            getScheduledStudentCount.setString(2, courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            while(resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public static ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();

        ArrayList<ScheduleEntry> students = new ArrayList<ScheduleEntry>();
        try {
           getScheduledStudentsByCourse = connection.prepareStatement("select * from app.schedule where semester = ? AND coursecode = ? AND status = ?");
           getScheduledStudentsByCourse.setString(1, semester);
           getScheduledStudentsByCourse.setString(2, courseCode);
           getScheduledStudentsByCourse.setString(3, "S");
           resultSet = getScheduledStudentsByCourse.executeQuery();
           
           while(resultSet.next()) {
               students.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
               
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();

        ArrayList<ScheduleEntry> students = new ArrayList<ScheduleEntry>();
        try {
            getWaitlistedStudentsByCourse = connection.prepareStatement("select * from app.schedule where semester = ? AND coursecode = ? AND status = ? order by Timestamp");
            getWaitlistedStudentsByCourse.setString(1, semester);
            getWaitlistedStudentsByCourse.setString(2, courseCode);
            getWaitlistedStudentsByCourse.setString(3, "W");
            resultSet = getWaitlistedStudentsByCourse.executeQuery();
            
            while(resultSet.next()) {
                students.add(new ScheduleEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getTimestamp(5)));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public static void dropStudentScheduleByCourse(String semester, String studentID, String courseCode) {
        connection = DBConnection.getConnection();

        try {
            dropStudentScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? AND studentID = ? And courseCode = ?");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, studentID);
            dropStudentScheduleByCourse.setString(3, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            
        }
    }
    
    public static void dropScheduleByCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();

        try {
            dropScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = ? AND courseCode = ?");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateScheduleEntry(String semester, ScheduleEntry entry) {
        connection = DBConnection.getConnection();

        dropStudentScheduleByCourse(semester, entry.getStudentID(), entry.getCourseCode());
        
        addScheduleEntry(new ScheduleEntry(entry.getSemester(), entry.getStudentID(), entry.getCourseCode(), "S", entry.getTimestamp()));
//        try {
//            
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
    
}
