/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Niklas
 */
public class AppointmentModule {

    Scanner sc;
    StaffEntity staffEntity;
    PatientSessionBeanRemote patientSessionBeanRemote;
    DoctorSessionBeanRemote doctorSessionBeanRemote;
    AppointmentSessionBeanRemote appointmentSessionBeanRemote;

    public AppointmentModule() {

    }

    public AppointmentModule(StaffEntity staff, PatientSessionBeanRemote patientSessionBeanRemote, DoctorSessionBeanRemote doctorSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote) {
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        
        sc = new Scanner(System.in);
        staffEntity = staff;
        int response;
        PatientEntity patientEntity;

        while (true) {
            System.out.println("*** CARS :: Appointment Operation **** \n ");
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    System.out.println("*** CARS :: Appointment Operation :: View Patient Appointments **** \n ");
                    System.out.print("Enter Patient Identity Number> ");
                    
                    patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());//staffSessionBeanRemote.retrieveStaffEntityByUserName(sc.nextLine());
                    //TODO
                    //this.patientSessionBeanRemote.retrieveAppointMentsOfPatient(patientEntity.getPatientId());

                    break;
                case 2:
                    System.out.println("*** CARS :: Appointment Operation :: Add Appointment **** \n ");
                    
                    //appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(java.sql.Date.valueOf("2020-10-10"), java.sql.Time.valueOf("12:30:00"), doctorSessionBeanRemote.retrieveDoctorEntityByRegistration("reg"), patientSessionBeanRemote.retrievePatientEntityByIdentityNumber("i")));
                    List availableDoctors = this.doctorSessionBeanRemote.retrieveAllDoctors();
                    // seems like i should filter times afterwards availableDoctors.removeAll(doctorSessionBeanRemote.getDoctorsOnLeave(java.sql.Date.valueOf("2020-10-10")));
                    
                    System.out.println("Doctor:");
                    System.out.println("Id | Name");
                    for (Object obj : availableDoctors) {
                        DoctorEntity my_doctor = (DoctorEntity) obj;
                        System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
                    }
                    //SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                   
                    System.out.print("Enter Doctor Id> ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();
                    
                    System.out.print("Enter Date> ");
                    Date date = java.sql.Date.valueOf(sc.nextLine());
                    ArrayList<String> times = getTimeList();
                    DoctorEntity my_doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(doctorId));
                    List occupiedTimes = appointmentSessionBeanRemote.retrieveOccupiedTimes(date, my_doc);
                    
                    //TODO add the remaining functionality
                    
                    //SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
                    //String s = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(myTimestamp);
                    //Time[] times = [java.sql.Time.valueOf(date)]
                    //List 
                            
                    // TODO
                    break;
                case 3:
                    System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
                    System.out.print("Enter Patient Identity Number> ");
                    patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
                    
                    System.out.println("Appointments:");
                    List app = appointmentSessionBeanRemote.retrievePatientAppointments(patientEntity);
                    System.out.println("wfsdf");
                    
                    // TODO
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }

    }
    
    private ArrayList<String> getTimeList(){
        ArrayList<String> times = new ArrayList<>();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        times.add(timeFormatter.format(java.sql.Time.valueOf("09:00:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("09:30:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("10:00:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("10:30:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("14:00:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("14:30:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("15:00:00")));
        times.add(timeFormatter.format(java.sql.Time.valueOf("15:30:00")));
        return times;
    }
    


}
