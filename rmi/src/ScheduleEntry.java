/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */

import java.sql.Timestamp;
import java.io.Serializable;

public class ScheduleEntry implements Serializable {
    private String semester;
    private String courseCode;
    private String studentID;
    private String status;
    private Timestamp timestamp;
    
    public ScheduleEntry(String semester, String studentID, String courseCode, String status, Timestamp timestamp) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.studentID = studentID;
        this.status = status;
        this.timestamp = timestamp;    
    }
    
    public String getSemester() {
        return semester;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public String getStudentID() {
        return studentID;
        
    }
    public String getStatus() {
        return status;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
}
