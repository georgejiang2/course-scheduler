import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author potato
 */
public interface RMIInterface extends Remote {
    public static final int port = 10990;
    
    public void addSemester(String semester) throws RemoteException;
    public ArrayList<String> getSemesterList() throws RemoteException;
    
    public void addCourse(String semester, String courseCode, String courseDescription, int seats) throws RemoteException;
    public ArrayList<CourseEntry> getAllCourses(String semester) throws RemoteException;
    public ArrayList<String> getAllCourseCodes(String semester) throws RemoteException;
    public int getCourseSeats(String semester, String courseCode) throws RemoteException; 
    public void dropCourse(String semester, String courseCode) throws RemoteException;
    
    public void addScheduleEntry(ScheduleEntry schedule) throws RemoteException;
    public ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String StudentID) throws RemoteException;
    public int getScheduledStudentCount(String currentSemester, String courseCode) throws RemoteException;
    public ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode) throws RemoteException;
    public ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode) throws RemoteException;
    public void dropStudentScheduleByCourse(String semester, String studentID, String courseCode) throws RemoteException;
    public void dropScheduleByCourse(String semester, String courseCode) throws RemoteException;
    public void updateScheduleEntry(String semester, ScheduleEntry entry) throws RemoteException;
    
    public void addStudent(StudentEntry Student) throws RemoteException;
    public ArrayList<StudentEntry> getAllStudents() throws RemoteException;
    public StudentEntry getStudent(String studentID) throws RemoteException;
    public void dropStudent(String studentID) throws RemoteException;
    
    
}
