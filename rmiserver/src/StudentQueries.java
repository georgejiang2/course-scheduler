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

public class StudentQueries {
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    public static void addStudent(StudentEntry Student) {
        connection = DBConnection.getConnection();
        try {
            addStudent = connection.prepareStatement("INSERT into app.student (studentid, firstname, lastname) values (?,?,?)");
            addStudent.setString(1,Student.getStudentID());
            addStudent.setString(2,Student.getFirstName());
            addStudent.setString(3,Student.getLastName());
            addStudent.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public static ArrayList<StudentEntry> getAllStudents() {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        
        try {
            getAllStudents = connection.prepareStatement("SELECT * from app.student order by studentid");
            resultSet = getAllStudents.executeQuery();
            
            while (resultSet.next()) {
                students.add(new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
    
    public static StudentEntry getStudent(String studentID) {
        connection = DBConnection.getConnection();
        StudentEntry se = new StudentEntry("", "", "");
        
        try {
            getStudent = connection.prepareStatement("SELECT * from app.student where studentid = ?");
            getStudent.setString(1, studentID);
            resultSet = getStudent.executeQuery();
            while (resultSet.next()) {
                se = new StudentEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return se;
    }
    
    public static void dropStudent(String studentID) {
        connection = DBConnection.getConnection();
        
        try {
            dropStudent = connection.prepareStatement("delete from app.student where studentid = ?");
            dropStudent.setString(1, studentID);
            dropStudent.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    
}
