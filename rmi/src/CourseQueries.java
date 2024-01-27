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

public class CourseQueries {
    private static Connection connection;
    private static PreparedStatement getAllCourses;
    private static PreparedStatement addCourse;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getCourseSeats;
    private static PreparedStatement dropCourse;
    private static ResultSet resultSet;
    
    public static void addCourse(String semester, String courseCode, String courseDescription, int seats) {
        
        connection = DBConnection.getConnection();
        try {
            addCourse = connection.prepareStatement("insert into app.course (SEMESTER, COURSECODE, DESCRIPTION, SEATS) values (?,?,?,?)");
            addCourse.setString(1,semester);
            addCourse.setString(2,courseCode);
            addCourse.setString(3,courseDescription);
            addCourse.setInt(4, seats);
            addCourse.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public static ArrayList<CourseEntry> getAllCourses(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<CourseEntry> courses = new ArrayList<CourseEntry>();
        try {
            getAllCourses = connection.prepareStatement("SELECT * FROM app.COURSE where SEMESTER = ? order by coursecode");
            getAllCourses.setString(1,semester);
            resultSet = getAllCourses.executeQuery();
            
            while (resultSet.next()) {
                courses.add(new CourseEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4)));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return courses;
    }
    
    public static ArrayList<String> getAllCourseCodes(String semester) {
        connection = DBConnection.getConnection();
        ArrayList<String> codes = new ArrayList<String>();
        try {
            getAllCourseCodes = connection.prepareStatement("SELECT coursecode FROM app.COURSE where SEMESTER = ? order by coursecode");
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while (resultSet.next()) {
                codes.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codes;
    }
    
    public static int getCourseSeats(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        int seats = 0;
        try {
            getCourseSeats = connection.prepareStatement("SELECT seats FROM app.COURSE where (semester = ? AND coursecode = ?)");
            getCourseSeats.setString(1,semester);
            getCourseSeats.setString(2,courseCode);
            resultSet = getCourseSeats.executeQuery();    
            while(resultSet.next()) {
                seats = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }    
    
    public static void dropCourse(String semester, String courseCode) {
        connection = DBConnection.getConnection();
        try {
            dropCourse = connection.prepareStatement("delete from app.course where semester = ? AND coursecode = ?");
            dropCourse.setString(1, semester);
            dropCourse.setString(2, courseCode);
            dropCourse.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}