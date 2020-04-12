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
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

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

                    //patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());//staffSessionBeanRemote.retrieveStaffEntityByUserName(sc.nextLine());
                    List appointments = appointmentSessionBeanRemote.retrievePatientAppointments(this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine()));
                    System.out.println("\n Appointments: \nId | Date | Time | Doctor");
                    //System.out.println("Id | Date | Time | Doctor");
                    for (Object obj : appointments) {
                        AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
                        System.out.println(appointmentEntity);
                    }
                    System.out.println("\n");

                    break;
                case 2:
                    System.out.println("*** CARS :: Appointment Operation :: Add Appointment **** \n ");

                    //appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(java.sql.Date.valueOf("2020-10-10"), java.sql.Time.valueOf("12:30:00"), doctorSessionBeanRemote.retrieveDoctorEntityByRegistration("reg"), patientSessionBeanRemote.retrievePatientEntityByIdentityNumber("i")));
                    //List availableDoctors = this.doctorSessionBeanRemote.retrieveAllDoctors();
                    // seems like i should filter times afterwards availableDoctors.removeAll(doctorSessionBeanRemote.getDoctorsOnLeave(java.sql.Date.valueOf("2020-10-10")));

                    System.out.println("Doctor:\nId | Name");
                    //System.out.println("Id | Name");
                    for (Object obj : this.doctorSessionBeanRemote.retrieveAllDoctors()) {
                        DoctorEntity my_doctor = (DoctorEntity) obj;
                        System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
                    }
                    //SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

                    System.out.print("Enter Doctor Id> ");
                    int doctorId = sc.nextInt();
                    sc.nextLine();
                    DoctorEntity my_doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(doctorId));

                    System.out.print("Enter Date> ");
                    String dateString = sc.nextLine();
                    Date date = java.sql.Date.valueOf(dateString);
                    ArrayList<String> times = getTimeList();
                    List occupiedTimes = appointmentSessionBeanRemote.retrieveOccupiedTimes(date, my_doc);

                    for (Object obj : occupiedTimes) {
                        Time my_time = (Time) obj;
                        times.remove(timeFormatter.format(my_time));
                    }

                    System.out.println("Availability for " + my_doc.getFirstName() + " " + my_doc.getLastName() + " on " + dateString + ":");
                    for (String time : times) {
                        System.out.print(time + " ");
                    }

                    System.out.print("\n\n Enter Time> ");
                    String timeString = sc.nextLine();
                    Time time = java.sql.Time.valueOf(timeString + ":00");
                    System.out.print("Enter Patient Identity Number> ");
                    PatientEntity patient = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
                    appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(date, time, my_doc, patient));
                    // TODO see time managin functionality from rigistrationsessionbean and add iot here
                    //TODO add the remaining functionality
                    break;
                case 3:
                    System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
                    System.out.print("Enter Patient Identity Number> ");
                    patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());

                    System.out.println("Appointments:");
                    List app = appointmentSessionBeanRemote.retrievePatientAppointments(patientEntity);
                    System.out.println("wfsdf");

                    System.out.println("\n Appointments:");
                    System.out.println("Id | Date | Time | Doctor");
                    for (Object obj : app) {
                        AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
                        System.out.println(appointmentEntity);
                    }
                    System.out.println("\n");

                    System.out.println("Enter Appointment Id> ");
                    int appId = sc.nextInt();
                    AppointmentEntity my_app = appointmentSessionBeanRemote.retrieveAppointmentByAppointmentId(new Long(appId));

                    sc.nextLine();
                    appointmentSessionBeanRemote.deleteAppointment(new Long(appId));
                    System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment with " + my_app.getDoctorEntity().getFirstName() + " " + my_app.getDoctorEntity().getLastName() + " at " + timeFormatter.format(my_app.getTime()) + " on " + my_app.getDate() + " has been canceled.");
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }

    }

    private ArrayList<String> getTimeList() {
        ArrayList<String> times = new ArrayList<>();
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
