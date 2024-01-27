
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel; 
import java.sql.*;

// rmi
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.registry.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author acv
 */

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    private String currentSemester;
    
    private String author;
    private String project;
    private RMIInterface client;

    // keytool -genkey -alias mykey -keyalg RSA -keystore KeyStore.jks
    // openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed
    // keytool -import -alias CARoot -file ca-cert -keystore KeyStore.jks
    // keytool -import -alias mykey -file cert-signed -keystore KeyStore.jks

    public MainFrame() throws NotBoundException, MalformedURLException, RemoteException, UnknownHostException {
        
        // Set the system properties for SSL
        System.setProperty("javax.net.ssl.keyStore", "/Users/potato/keytest/client.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "passwd");
        System.setProperty("javax.net.ssl.trustStore", "/Users/potato/keytest/client.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "passwd");
//        System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1.2");
//        System.setProperty("javax.rmi.ssl.client.enabledCipherSuites", "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384");
        System.setProperty("javax.net.debug", "ssl");

//        try {
//            Class.forName("org.apache.derby.jdbc.ClientDriver");
//            System.out.println("111111");
//        } catch (ClassNotFoundException e) {
//            System.out.println("222222");
//        }

        Registry registry = LocateRegistry.getRegistry(
            InetAddress.getLocalHost().getHostName(), RMIInterface.port,
            new SslRMIClientSocketFactory());

//        Registry registry = LocateRegistry.getRegistry(
//            InetAddress.getLocalHost().getHostName(), RMIInterface.port);

        System.err.println("registry");

        // "obj" is the identifier that we'll use to refer
        // to the remote object that implements the "Hello"
        // interface
        client = (RMIInterface) registry.lookup("//localhost/RMIServer");
        System.err.println("lookup successful");
//client = (RMIInterface) Naming.lookup("//localhost/RMIServer");
        
        initComponents(); 
        checkData();
        rebuildSemesterComboBoxes();
        rebuildComboBoxes();
    }
    
    
    public void rebuildSemesterComboBoxes() throws RemoteException {
        // ArrayList<String> semesters = SemesterQueries.getSemesterList();
        ArrayList<String> semesters = null;
        try {
            semesters = client.getSemesterList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        currentSemesterComboBox.setModel(new javax.swing.DefaultComboBoxModel(semesters.toArray()));
        if (semesters.size() > 0) {
            currentSemesterLabel.setText(semesters.get(0));
            currentSemester = semesters.get(0);
        } else {
            currentSemesterLabel.setText("None, add a semester.");
            currentSemester = "None";
        }
        ArrayList<StudentEntry> se = client.getAllStudents();
        ArrayList<String> students = new ArrayList<String>();
        for (StudentEntry s : se) {
            students.add(s.getLastName()+", "+s.getFirstName() + " " + s.getStudentID());
        }
        if (students.size() > 0) {
            currentStudentTextField.setText(students.get(0));
        } else {
            currentStudentTextField.setText("None, add a student.");
        }
    }
    public void rebuildComboBoxes() throws RemoteException {
        // student combo box
        if (currentStudentTextField.equals("None, add a student.")) {
            ArrayList<StudentEntry> se = client.getAllStudents();
            currentStudentTextField.setText(se.getLastName() + ", "+ se.getFirstName() + " " + se.getStudentID());
        }
        ArrayList<StudentEntry> se = client.getAllStudents();
        ArrayList<String> students = new ArrayList<String>();
        for (StudentEntry s : se) {
            students.add(s.getLastName()+", "+s.getFirstName() + " " + s.getStudentID());
        }
        currentStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
        dropStudentSelectStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel(students.toArray()));
        
        //course combo box
        ArrayList<String> courses = new ArrayList<String>();
        try {
            if (client.getSemesterList().size() > 0) {
                courses = client.getAllCourseCodes(currentSemesterComboBox.getSelectedItem().toString());
                
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();
        ArrayList<String> studentSchedule = new ArrayList<String>();
        try {
            if (client.getSemesterList().size() > 0 && students.size() > 0) {
                schedule = client.getScheduleByStudent(currentSemesterLabel.getText(), currentStudentTextField.getText().split(" ", 3)[2]);
                for (ScheduleEntry s : schedule) {
                    studentSchedule.add(s.getCourseCode());
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        

        dropCourseSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel(studentSchedule.toArray()));
        listSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel(courses.toArray()));
        scheduleCoursesSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel(courses.toArray()));
        dropCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel(courses.toArray()));
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        addSemesterTextfield = new javax.swing.JTextField();
        addSemesterSubmitButton = new javax.swing.JButton();
        addSemesterStatusLabel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        addCourseCourseCodeLabel = new javax.swing.JLabel();
        addCourseCourseCodeTextField = new javax.swing.JTextField();
        addCourseCourseDescriptionLabel = new javax.swing.JLabel();
        addCourseCourseDescriptionTextField = new javax.swing.JTextField();
        addCourseSeatsLabel = new javax.swing.JLabel();
        addCourseSeatsSpinner = new javax.swing.JSpinner();
        addCourseButton = new javax.swing.JButton();
        addCourseLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addCourseStatusLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        addStudentStudentIDLabel = new javax.swing.JLabel();
        addStudentFirstNameLabel = new javax.swing.JLabel();
        addStudentLastNameLabel = new javax.swing.JLabel();
        addStudentStudentIDTextField = new javax.swing.JTextField();
        addStudentFirstNameTextField = new javax.swing.JTextField();
        addStudentLastNameTextField = new javax.swing.JTextField();
        addStudentButton = new javax.swing.JButton();
        addStudentStatusLabel = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        listSelectCourseComboBox = new javax.swing.JComboBox<>();
        listButton = new javax.swing.JButton();
        listScheduledStudentsLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        scheduledStudentsTable = new javax.swing.JTable();
        listWaitlistedStudents = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        waitlistedStudentsTable = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        dropStudentSelectStudent = new javax.swing.JLabel();
        dropStudentSelectStudentComboBox = new javax.swing.JComboBox<>();
        dropStudentButton = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        dropStudentTextArea = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        dropCourseLabel = new javax.swing.JLabel();
        dropCourseComboBox = new javax.swing.JComboBox<>();
        dropCourseButton = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        dropTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        displayCoursesButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayCoursesTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        scheduleCoursesSelectCourseLabel = new javax.swing.JLabel();
        scheduleCoursesSelectCourseComboBox = new javax.swing.JComboBox<>();
        scheduleCoursesButton = new javax.swing.JButton();
        scheduleCoursesStatusLabel = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayScheduleTable = new javax.swing.JTable();
        displayScheduleButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        dropCourseSelectCourseLabel = new javax.swing.JLabel();
        dropCourseSelectCourseComboBox = new javax.swing.JComboBox<>();
        dropCourseDropCourseButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        dropCourseTextArea = new javax.swing.JTextArea();
        currentStudentLabel = new javax.swing.JLabel();
        currentStudentTextField = new javax.swing.JLabel();
        currentStudentComboBox = new javax.swing.JComboBox<>();
        changeStudentButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        currentSemesterLabel = new javax.swing.JLabel();
        currentSemesterComboBox = new javax.swing.JComboBox<>();
        changeSemesterButton = new javax.swing.JButton();
        aboutButton = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 244, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Course Scheduler");

        jLabel3.setText("Semester Name:");

        addSemesterTextfield.setColumns(20);
        addSemesterTextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSemesterTextfieldActionPerformed(evt);
            }
        });

        addSemesterSubmitButton.setText("Submit");
        addSemesterSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSemesterSubmitButtonActionPerformed(evt);
            }
        });

        addSemesterStatusLabel.setText("                                                   ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addSemesterTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(addSemesterSubmitButton))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addSemesterStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(475, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(addSemesterTextfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addSemesterSubmitButton)
                .addGap(18, 18, 18)
                .addComponent(addSemesterStatusLabel)
                .addContainerGap(280, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Add Semester", jPanel3);

        addCourseCourseCodeLabel.setText("Course Code:");

        addCourseCourseCodeTextField.setColumns(20);

        addCourseCourseDescriptionLabel.setText("Course Description:");

        addCourseCourseDescriptionTextField.setColumns(50);

        addCourseSeatsLabel.setText("Seats:");

        ((JSpinner.DefaultEditor) addCourseSeatsSpinner.getEditor()).getTextField().setColumns(4);

        addCourseButton.setText("Submit");
        addCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseButtonActionPerformed(evt);
            }
        });

        addCourseStatusLabel.setText("                                                   ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addCourseCourseCodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addCourseCourseCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addCourseCourseDescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addCourseCourseDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addCourseSeatsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addCourseButton)
                            .addComponent(addCourseSeatsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(addCourseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(addCourseStatusLabel))))
                .addContainerGap(626, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseCourseCodeLabel)
                    .addComponent(addCourseCourseCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseCourseDescriptionLabel)
                    .addComponent(addCourseCourseDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseSeatsLabel)
                    .addComponent(addCourseSeatsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addCourseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addCourseStatusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addCourseLabel)
                .addContainerGap(205, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Add Course", jPanel4);

        addStudentStudentIDLabel.setText("Student ID:");

        addStudentFirstNameLabel.setText("First Name:");

        addStudentLastNameLabel.setText("Last Name:");

        addStudentStudentIDTextField.setColumns(20);
        addStudentStudentIDTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentStudentIDTextFieldActionPerformed(evt);
            }
        });

        addStudentFirstNameTextField.setColumns(20);

        addStudentLastNameTextField.setColumns(20);

        addStudentButton.setText("Submit");
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        addStudentStatusLabel.setText("                                                                             ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addStudentStudentIDLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addStudentStudentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addStudentFirstNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addStudentFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addStudentLastNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addStudentButton)
                            .addComponent(addStudentLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(addStudentStatusLabel))
                .addContainerGap(528, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentStudentIDLabel)
                    .addComponent(addStudentStudentIDTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentFirstNameLabel)
                    .addComponent(addStudentFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addStudentLastNameLabel)
                    .addComponent(addStudentLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addStudentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addStudentStatusLabel)
                .addContainerGap(229, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Add Student", jPanel5);

        jLabel5.setText("Select Course:");

        listSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        listButton.setText("Display");
        listButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listButtonActionPerformed(evt);
            }
        });

        listScheduledStudentsLabel.setText("Scheduled Students:");

        scheduledStudentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Last Name", "First Name", "StudentID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(scheduledStudentsTable);

        listWaitlistedStudents.setText("Waitlisted Students:");

        waitlistedStudentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Last Name", "First Name", "StudentID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(waitlistedStudentsTable);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49)
                                .addComponent(listButton))
                            .addComponent(listScheduledStudentsLabel)
                            .addComponent(listWaitlistedStudents))
                        .addContainerGap(550, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(listSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(listButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listScheduledStudentsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listWaitlistedStudents)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Display Course List of Students", jPanel11);

        dropStudentSelectStudent.setText("Select Student:");

        dropStudentSelectStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        dropStudentButton.setText("Drop Student");
        dropStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropStudentButtonActionPerformed(evt);
            }
        });

        dropStudentTextArea.setColumns(20);
        dropStudentTextArea.setRows(5);
        jScrollPane6.setViewportView(dropStudentTextArea);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(dropStudentSelectStudent)
                        .addGap(18, 18, 18)
                        .addComponent(dropStudentSelectStudentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(dropStudentButton)))
                .addContainerGap(237, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropStudentSelectStudent)
                    .addComponent(dropStudentSelectStudentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropStudentButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Drop Student", jPanel12);

        dropCourseLabel.setText("Select Course to be Dropped:");

        dropCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        dropCourseButton.setText("Drop Course");
        dropCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseButtonActionPerformed(evt);
            }
        });

        dropTextArea.setColumns(20);
        dropTextArea.setRows(5);
        jScrollPane7.setViewportView(dropTextArea);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(dropCourseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dropCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(dropCourseButton)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropCourseLabel)
                    .addComponent(dropCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropCourseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Drop Course", jPanel13);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Admin", jPanel1);

        displayCoursesButton.setText("Display");
        displayCoursesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayCoursesButtonActionPerformed(evt);
            }
        });

        displayCoursesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Course Code", "Description", "Seats"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(displayCoursesTable);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 284, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(displayCoursesButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(displayCoursesButton)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Display Courses", jPanel7);

        scheduleCoursesSelectCourseLabel.setText("Select Course:");

        scheduleCoursesSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        scheduleCoursesButton.setText("Submit");
        scheduleCoursesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scheduleCoursesButtonActionPerformed(evt);
            }
        });

        scheduleCoursesStatusLabel.setText("                                                           ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scheduleCoursesStatusLabel)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(scheduleCoursesSelectCourseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scheduleCoursesButton)
                            .addComponent(scheduleCoursesSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(600, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scheduleCoursesSelectCourseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scheduleCoursesSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scheduleCoursesButton)
                .addGap(29, 29, 29)
                .addComponent(scheduleCoursesStatusLabel)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Schedule Courses", jPanel8);

        displayScheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Course Code", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(displayScheduleTable);

        displayScheduleButton.setText("Display");
        displayScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayScheduleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 480, Short.MAX_VALUE))
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(137, 137, 137)
                .addComponent(displayScheduleButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayScheduleButton)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Display Schedule", jPanel9);

        dropCourseSelectCourseLabel.setText("Select Course:");

        dropCourseSelectCourseComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        dropCourseDropCourseButton.setText("Drop Course");
        dropCourseDropCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropCourseDropCourseButtonActionPerformed(evt);
            }
        });

        dropCourseTextArea.setColumns(20);
        dropCourseTextArea.setRows(5);
        jScrollPane3.setViewportView(dropCourseTextArea);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(dropCourseSelectCourseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dropCourseSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(dropCourseDropCourseButton))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dropCourseSelectCourseLabel)
                    .addComponent(dropCourseSelectCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dropCourseDropCourseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(129, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Drop Course", jPanel10);

        currentStudentLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        currentStudentLabel.setText("Current Student: ");

        currentStudentTextField.setFont(new java.awt.Font("Comic Sans MS", 0, 16)); // NOI18N
        currentStudentTextField.setText("           ");

        currentStudentComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        currentStudentComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentStudentComboBoxActionPerformed(evt);
            }
        });

        changeStudentButton.setText("Change Student");
        changeStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeStudentButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentStudentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentStudentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(currentStudentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(changeStudentButton)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentStudentLabel)
                    .addComponent(currentStudentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeStudentButton)
                    .addComponent(currentStudentTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3))
        );

        jTabbedPane1.addTab("Student", jPanel2);

        jLabel2.setFont(new java.awt.Font("Comic Sans MS", 1, 16)); // NOI18N
        jLabel2.setText("Current Semester: ");

        currentSemesterLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 16)); // NOI18N
        currentSemesterLabel.setText("           ");

        currentSemesterComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        currentSemesterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentSemesterComboBoxActionPerformed(evt);
            }
        });

        changeSemesterButton.setText("Change Semester");
        changeSemesterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeSemesterButtonActionPerformed(evt);
            }
        });

        aboutButton.setText("About");
        aboutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentSemesterLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(currentSemesterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changeSemesterButton)
                        .addGap(31, 31, 31)
                        .addComponent(aboutButton)))
                .addContainerGap())
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(currentSemesterLabel)
                    .addComponent(currentSemesterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeSemesterButton)
                    .addComponent(aboutButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addSemesterSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSemesterSubmitButtonActionPerformed
        String semester = addSemesterTextfield.getText();
        try {
            // SemesterQueries.addSemester(semester);
            client.addSemester(semester);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        addSemesterStatusLabel.setText("Semester " + semester + " has been added.");
        
        try {
            rebuildSemesterComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_addSemesterSubmitButtonActionPerformed

    private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutButtonActionPerformed
        // TODO add your handling code here:
        // display about information.
        JOptionPane.showMessageDialog(null, "Author: " + author + " Project: " + project);
    }//GEN-LAST:event_aboutButtonActionPerformed

    private void currentSemesterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentSemesterComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentSemesterComboBoxActionPerformed

    private void addSemesterTextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSemesterTextfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addSemesterTextfieldActionPerformed

    private void changeSemesterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeSemesterButtonActionPerformed
        // TODO add your handling code here:
        currentSemesterLabel.setText(currentSemesterComboBox.getSelectedItem().toString());
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        
    }//GEN-LAST:event_changeSemesterButtonActionPerformed

    private void addCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseButtonActionPerformed
        // TODO add your handling code here:
        String courseCode = addCourseCourseCodeTextField.getText();
        String courseDescription = addCourseCourseDescriptionTextField.getText();
        int seats = (int) addCourseSeatsSpinner.getValue();
        try {
            client.addCourse(currentSemesterLabel.getText(), courseCode, courseDescription, seats);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        addCourseStatusLabel.setText(courseCode + " has been added.");
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_addCourseButtonActionPerformed

    private void addStudentStudentIDTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentStudentIDTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addStudentStudentIDTextFieldActionPerformed

    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        // TODO add your handling code here:
        String studentID = addStudentStudentIDTextField.getText();
        String firstName = addStudentFirstNameTextField.getText();
        String lastName = addStudentLastNameTextField.getText();
        try {
            client.addStudent(new StudentEntry(studentID, firstName, lastName));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        addStudentStatusLabel.setText(lastName+", "+firstName+" has been added.");
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_addStudentButtonActionPerformed

    private void scheduleCoursesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scheduleCoursesButtonActionPerformed
        // TODO add your handling code here:
        String semester = currentSemesterComboBox.getSelectedItem().toString();
        String courseCode = scheduleCoursesSelectCourseComboBox.getSelectedItem().toString();
        String studentName = currentStudentTextField.getText();
        String[] names = studentName.split(" ", 3);
        String studentID = names[2];
        studentName = names[0] + " " + names[1];

        String status = "";
        try {
            if (client.getScheduledStudentCount(semester,courseCode) >= client.getCourseSeats(semester, courseCode)) {
                status = "W";
            } else {
                status = "S";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
        try {
            client.addScheduleEntry(new ScheduleEntry(semester, studentID, courseCode, status, currentTimestamp));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (status == "W") {    
            scheduleCoursesStatusLabel.setText(studentName + " has been waitlisted for the class.");    
        } else {
            scheduleCoursesStatusLabel.setText(studentName + " has been scheduled for the class.");
        }
        
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_scheduleCoursesButtonActionPerformed

    private void displayCoursesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayCoursesButtonActionPerformed
        // TODO add your handling code here:
        ArrayList<CourseEntry> courses = null;
        try {
            courses = client.getAllCourses(currentSemesterComboBox.getSelectedItem().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        DefaultTableModel displayCoursesTabelModel = (DefaultTableModel) displayCoursesTable.getModel();
        Object[] rowData = new Object[3];
        displayCoursesTabelModel.setNumRows(0);
        for (CourseEntry ce : courses) {
            rowData[0] = ce.getCourseCode();
            rowData[1] = ce.getDescription();
            rowData[2] = ce.getSeats();
            displayCoursesTabelModel.addRow(rowData);
        }
    }//GEN-LAST:event_displayCoursesButtonActionPerformed

    private void displayScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayScheduleButtonActionPerformed
        String studentID = currentStudentTextField.getText().split(" ", 3)[2];
        String semester = currentSemesterLabel.getText();
        ArrayList<ScheduleEntry> schedule = null;
        try {
            schedule = client.getScheduleByStudent(semester, studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        DefaultTableModel displayCoursesTabelModel = (DefaultTableModel) displayScheduleTable.getModel();
        Object[] rowData = new Object[2];
        displayCoursesTabelModel.setNumRows(0);
        for (ScheduleEntry se : schedule) {
            rowData[0] = se.getCourseCode();
            if (se.getStatus().equals("W")) {
                rowData[1] = "Waitlisted";
            } else {
                rowData[1] = "Scheduled";
            }
            displayCoursesTabelModel.addRow(rowData);
            
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_displayScheduleButtonActionPerformed

    private void dropCourseDropCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseDropCourseButtonActionPerformed
        // TODO add your handling code here:
        dropCourseTextArea.setText("");
        String semester = currentSemesterLabel.getText();
        String studentID = currentStudentTextField.getText().split(" ", 3)[2];
        String courseCode = dropCourseSelectCourseComboBox.getSelectedItem().toString();
        ArrayList<ScheduleEntry> w = null;
        try {
            w = client.getWaitlistedStudentsByCourse(semester, courseCode);
            client.dropStudentScheduleByCourse(semester, studentID, courseCode);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        ArrayList<ScheduleEntry> waitlisted = null;
        try {
            waitlisted = client.getWaitlistedStudentsByCourse(semester, courseCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (w.size() != waitlisted.size()) {
            // person was on waitlist
            dropCourseTextArea.append(currentStudentTextField.getText() + " has been dropped from the waitlist for " + courseCode + "\n");
        } else {
            // schedule new person
            dropCourseTextArea.append(currentStudentTextField.getText() + " has been dropped from " + courseCode + "\n");
            if (waitlisted.size() > 0) {
                StudentEntry waitlistedStudent = null;
                try {
                    client.updateScheduleEntry(semester,waitlisted.get(0));
                    waitlistedStudent = client.getStudent(waitlisted.get(0).getStudentID());

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                String name = waitlistedStudent.getLastName() + ", " + waitlistedStudent.getFirstName() + " " + waitlistedStudent.getStudentID();
                dropCourseTextArea.append(name + " has been scheduled into " + courseCode);
            }
        }

        
        
    }//GEN-LAST:event_dropCourseDropCourseButtonActionPerformed

    private void currentStudentComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentStudentComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentStudentComboBoxActionPerformed

    private void changeStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeStudentButtonActionPerformed
        // TODO add your handling code here:
        currentStudentTextField.setText(currentStudentComboBox.getSelectedItem().toString());
        
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_changeStudentButtonActionPerformed

    private void listButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listButtonActionPerformed
        // TODO add your handling code here:
        ArrayList<ScheduleEntry> scheduledStudents = null;
        try {
            scheduledStudents = client.getScheduledStudentsByCourse(currentSemesterLabel.getText(), listSelectCourseComboBox.getSelectedItem().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        DefaultTableModel scheduledStudentsTabelModel = (DefaultTableModel) scheduledStudentsTable.getModel();
        Object[] rowData = new Object[3];
        scheduledStudentsTabelModel.setNumRows(0);
        StudentEntry student = null;
        
        for (ScheduleEntry se : scheduledStudents) {
            try {
                student = client.getStudent(se.getStudentID());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            rowData[0] = student.getLastName();
            rowData[1] = student.getFirstName();
            rowData[2] = student.getStudentID();
            
            scheduledStudentsTabelModel.addRow(rowData);
            
        }
        ArrayList<ScheduleEntry> waitlistedStudents = null;
        try {
            waitlistedStudents = client.getWaitlistedStudentsByCourse(currentSemesterLabel.getText(), listSelectCourseComboBox.getSelectedItem().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        DefaultTableModel waitlistedStudentsTabelModel = (DefaultTableModel) waitlistedStudentsTable.getModel();
        waitlistedStudentsTabelModel.setNumRows(0);
        for (ScheduleEntry se : waitlistedStudents) {
            try {
                student = client.getStudent(se.getStudentID());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            rowData[0] = student.getLastName();
            rowData[1] = student.getFirstName();
            rowData[2] = student.getStudentID();
            waitlistedStudentsTabelModel.addRow(rowData);
            
        }
    }//GEN-LAST:event_listButtonActionPerformed

    private void dropStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropStudentButtonActionPerformed
        // TODO add your handling code here:
        dropStudentTextArea.setText("");
        String studentID = dropStudentSelectStudentComboBox.getSelectedItem().toString().split(" ", 3)[2];
        StudentEntry student = null;
        try {
            student = client.getStudent(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        
        }
        String name = student.getLastName() + ", " + student.getFirstName() + " " + student.getStudentID();
        try {
            client.dropStudent(studentID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
        dropStudentTextArea.append(name + " has been dropped from the list of students. \n\n");
        ArrayList<String> semesters = null;
        try {
            semesters = client.getSemesterList();
        } catch (RemoteException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String s : semesters) {
            ArrayList <ScheduleEntry> schedule = null;
            try {
                schedule = client.getScheduleByStudent(s, studentID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (schedule.size() > 0) {
                dropStudentTextArea.append("For Semester " + s + "\n");
                for (ScheduleEntry se : schedule) {
                    if (se.getStatus().equals("W")) {
                        try {
                            client.dropStudentScheduleByCourse(s, studentID, se.getCourseCode());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        dropStudentTextArea.append(name + " has been dropped from the waitlist for " + se.getCourseCode() + "\n");
                    } else {
                        try {                        
                            client.dropStudentScheduleByCourse(s, studentID, se.getCourseCode());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        dropStudentTextArea.append(name + " has been dropped from " + se.getCourseCode() + "\n");
                        ArrayList <ScheduleEntry> waitlist = null;
                        try {
                            waitlist = client.getWaitlistedStudentsByCourse(s, se.getCourseCode());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        if (waitlist.size() > 0) {
                            StudentEntry waitlistedStudent = null;
                            try {
                                client.updateScheduleEntry(s, waitlist.get(0));
                                waitlistedStudent = client.getStudent(waitlist.get(0).getStudentID());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            String waitlistedName = waitlistedStudent.getLastName() + ", " + waitlistedStudent.getFirstName() + " " + waitlistedStudent.getStudentID();
                            dropStudentTextArea.append(waitlistedName + " has been scheduled into " + se.getCourseCode() + "\n");
                        }
                    }
                }
                dropStudentTextArea.append("\n");
            }
            
        }
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_dropStudentButtonActionPerformed

    private void dropCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropCourseButtonActionPerformed
        // TODO add your handling code here:
        dropTextArea.setText("");
        String courseCode = dropCourseComboBox.getSelectedItem().toString();
        String semester = currentSemesterLabel.getText();
        try {
            client.dropCourse(semester, courseCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        dropTextArea.append("Scheduled students dropped from the course: \n");
        ArrayList<ScheduleEntry> students = null;
        ArrayList<ScheduleEntry> waitlist = null;
        try {
            students = client.getScheduledStudentsByCourse(semester, courseCode);
            waitlist = client.getWaitlistedStudentsByCourse(semester, courseCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (ScheduleEntry s : students) {
            String studentID = s.getStudentID();
            StudentEntry student = null;
            try {
                student = client.getStudent(studentID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            String name = student.getLastName() + ", " + student.getFirstName() + " " + student.getStudentID();
            dropTextArea.append(name + "\n");
            try {
                client.dropStudentScheduleByCourse(semester, studentID, courseCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        dropTextArea.append("\nWaitlisted students dropped from the course: \n");
        
        for (ScheduleEntry s : waitlist) {
            String studentID = s.getStudentID();
            StudentEntry student = null;
            try {
                student = client.getStudent(studentID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            String name = student.getLastName() + ", " + student.getFirstName() + " " + student.getStudentID();
            dropTextArea.append(name + "\n");
            try {
                client.dropStudentScheduleByCourse(semester, studentID, courseCode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
        try {
            rebuildComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_dropCourseButtonActionPerformed

    
    private void checkData() {
        try {
            FileReader reader = new FileReader("xzq789yy.txt");
            BufferedReader breader = new BufferedReader(reader);

            String encodedAuthor = breader.readLine();
            String encodedProject = breader.readLine();
            byte[] decodedAuthor = Base64.getDecoder().decode(encodedAuthor);
            author = new String(decodedAuthor);
            byte[] decodedProject = Base64.getDecoder().decode(encodedProject);
            project = new String(decodedProject);
            reader.close();

        } catch (FileNotFoundException e) {
            //get user info and create file
            author = JOptionPane.showInputDialog("Enter your first and last name.");
            project = "Course Scheduler Spring 2023";

            //write data to the data file.
            try {
                FileWriter writer = new FileWriter("xzq789yy.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // encode the output data.
                String encodedAuthor = Base64.getEncoder().encodeToString(author.getBytes());

                bufferedWriter.write(encodedAuthor);
                bufferedWriter.newLine();

                String encodedProject = Base64.getEncoder().encodeToString(project.getBytes());
                bufferedWriter.write(encodedProject);

                bufferedWriter.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    try {
                        new MainFrame().setVisible(true);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                } catch (NotBoundException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aboutButton;
    private javax.swing.JButton addCourseButton;
    private javax.swing.JLabel addCourseCourseCodeLabel;
    private javax.swing.JTextField addCourseCourseCodeTextField;
    private javax.swing.JLabel addCourseCourseDescriptionLabel;
    private javax.swing.JTextField addCourseCourseDescriptionTextField;
    private javax.swing.JLabel addCourseLabel;
    private javax.swing.JLabel addCourseSeatsLabel;
    private javax.swing.JSpinner addCourseSeatsSpinner;
    private javax.swing.JLabel addCourseStatusLabel;
    private javax.swing.JLabel addSemesterStatusLabel;
    private javax.swing.JButton addSemesterSubmitButton;
    private javax.swing.JTextField addSemesterTextfield;
    private javax.swing.JButton addStudentButton;
    private javax.swing.JLabel addStudentFirstNameLabel;
    private javax.swing.JTextField addStudentFirstNameTextField;
    private javax.swing.JLabel addStudentLastNameLabel;
    private javax.swing.JTextField addStudentLastNameTextField;
    private javax.swing.JLabel addStudentStatusLabel;
    private javax.swing.JLabel addStudentStudentIDLabel;
    private javax.swing.JTextField addStudentStudentIDTextField;
    private javax.swing.JButton changeSemesterButton;
    private javax.swing.JButton changeStudentButton;
    private javax.swing.JComboBox<String> currentSemesterComboBox;
    private javax.swing.JLabel currentSemesterLabel;
    private javax.swing.JComboBox<String> currentStudentComboBox;
    private javax.swing.JLabel currentStudentLabel;
    private javax.swing.JLabel currentStudentTextField;
    private javax.swing.JButton displayCoursesButton;
    private javax.swing.JTable displayCoursesTable;
    private javax.swing.JButton displayScheduleButton;
    private javax.swing.JTable displayScheduleTable;
    private javax.swing.JButton dropCourseButton;
    private javax.swing.JComboBox<String> dropCourseComboBox;
    private javax.swing.JButton dropCourseDropCourseButton;
    private javax.swing.JLabel dropCourseLabel;
    private javax.swing.JComboBox<String> dropCourseSelectCourseComboBox;
    private javax.swing.JLabel dropCourseSelectCourseLabel;
    private javax.swing.JTextArea dropCourseTextArea;
    private javax.swing.JButton dropStudentButton;
    private javax.swing.JLabel dropStudentSelectStudent;
    private javax.swing.JComboBox<String> dropStudentSelectStudentComboBox;
    private javax.swing.JTextArea dropStudentTextArea;
    private javax.swing.JTextArea dropTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JButton listButton;
    private javax.swing.JLabel listScheduledStudentsLabel;
    private javax.swing.JComboBox<String> listSelectCourseComboBox;
    private javax.swing.JLabel listWaitlistedStudents;
    private javax.swing.JButton scheduleCoursesButton;
    private javax.swing.JComboBox<String> scheduleCoursesSelectCourseComboBox;
    private javax.swing.JLabel scheduleCoursesSelectCourseLabel;
    private javax.swing.JLabel scheduleCoursesStatusLabel;
    private javax.swing.JTable scheduledStudentsTable;
    private javax.swing.JTable waitlistedStudentsTable;
    // End of variables declaration//GEN-END:variables
    
}
