import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.derby.client.ClientAutoloadedDriver;
import org.apache.derby.iapi.jdbc.AutoloadedDriver;




import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author potato
 */
public class RMIServer extends UnicastRemoteObject implements RMIInterface {
    private static final long serialVersionUID = 1;
    protected RMIServer() throws RemoteException {
        super(0, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
//        super(0);
    }
    
    @Override
    public void addSemester(String semester) throws RemoteException {
        SemesterQueries.addSemester(semester);
        
    }
    @Override
    public ArrayList<String> getSemesterList() throws RemoteException {
        return SemesterQueries.getSemesterList();
    }
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
//        try { 
////            Class.forName("org.apache.derby.jdbc.ClientDriver"); // this works in MainFrame constructor
////            Class.forName("org.apache.derby.client.ClientAutoloadedDriver");
//            Class.forName("org.apache.derby.iapi.jdbc.AutoloadedDriver");
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
//        }

        System.setProperty("javax.net.ssl.keyStore", "/Users/potato/keytest/server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "passwd");
        
        System.setProperty("javax.net.ssl.trustStore", "/Users/potato/keytest/server.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
        // protocol
//        System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1.2");
        // encryption
        System.setProperty("javax.net.debug", "ssl");

        
        try {
            Registry registry = LocateRegistry.createRegistry(port, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
//            Registry registry = LocateRegistry.createRegistry(port);

            RMIServer obj = new RMIServer();

            // Bind this object instance to the name "HelloServer"
            registry.bind("//localhost/RMIServer", obj);


            
//            Naming.rebind("//localhost/RMIServer", new RMIServer());
            System.err.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCourse(String semester, String courseCode, String courseDescription, int seats) throws RemoteException {
        CourseQueries.addCourse(semester, courseCode, courseDescription, seats);
    }

    @Override
    public ArrayList<CourseEntry> getAllCourses(String semester) throws RemoteException {
        return CourseQueries.getAllCourses(semester);
    }

    @Override
    public ArrayList<String> getAllCourseCodes(String semester) throws RemoteException {
        return CourseQueries.getAllCourseCodes(semester);
    }

    @Override
    public int getCourseSeats(String semester, String courseCode) throws RemoteException {
        return CourseQueries.getCourseSeats(semester, courseCode);
    }

    @Override
    public void dropCourse(String semester, String courseCode) throws RemoteException {
        CourseQueries.dropCourse(semester, courseCode);
    }
    
    @Override
    public void addScheduleEntry(ScheduleEntry schedule) throws RemoteException {
        ScheduleQueries.addScheduleEntry(schedule);
    }
    
    @Override
    public ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String StudentID) throws RemoteException {
        return ScheduleQueries.getScheduleByStudent(semester, StudentID);
    }
    
    @Override
    public int getScheduledStudentCount(String currentSemester, String courseCode) throws RemoteException {
        return ScheduleQueries.getScheduledStudentCount(currentSemester, courseCode);
    }
    
    @Override
    public ArrayList<ScheduleEntry> getScheduledStudentsByCourse(String semester, String courseCode) throws RemoteException {
        return ScheduleQueries.getScheduledStudentsByCourse(semester, courseCode);
    }
    
    @Override
    public ArrayList<ScheduleEntry> getWaitlistedStudentsByCourse(String semester, String courseCode) throws RemoteException {
         return ScheduleQueries.getWaitlistedStudentsByCourse(semester, courseCode);
    }
    
    @Override
    public void dropStudentScheduleByCourse(String semester, String studentID, String courseCode) throws RemoteException {
        ScheduleQueries.dropStudentScheduleByCourse(semester, studentID, courseCode);
    }
    
    @Override
    public void dropScheduleByCourse(String semester, String courseCode) throws RemoteException {
        ScheduleQueries.dropScheduleByCourse(semester, courseCode);
    }
    
    @Override
    public void updateScheduleEntry(String semester, ScheduleEntry entry) throws RemoteException {
        ScheduleQueries.updateScheduleEntry(semester, entry);
    }
   
    @Override
    public void addStudent(StudentEntry Student) throws RemoteException {
        StudentQueries.addStudent(Student);
    }
    
    @Override
    public ArrayList<StudentEntry> getAllStudents() throws RemoteException {
        return StudentQueries.getAllStudents();
    }
    
    @Override
    public StudentEntry getStudent(String studentID) throws RemoteException {
        return StudentQueries.getStudent(studentID);
    }
    
    @Override
    public void dropStudent(String studentID) throws RemoteException {
        StudentQueries.dropStudent(studentID);
    }
    
}
