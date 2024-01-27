/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */

import java.io.Serializable;

public class CourseEntry implements Serializable {
    private String semester;
    private String courseCode;
    private String description;
    private int seats;
    
    public CourseEntry(String semester, String courseCode, String description, int seats) {
        this.semester = semester;
        this.courseCode = courseCode;
        this.description = description;
        this.seats = seats;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public String getDescription() {
        return description;
        
    }
    
    public int getSeats() {
        return seats;
    }
    

}
