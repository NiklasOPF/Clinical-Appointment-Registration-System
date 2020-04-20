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
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.PatientNotFoundException;

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

        while (true) {
            System.out.println("*** CARS :: Appointment Operation **** \n ");
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back \n");
            System.out.print("> ");
            try{
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    viewPatientAppointments();
                    break;
                case 2:
                    addAppointment();
                    break;
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input");
            }
            }catch(Exception e){
                System.out.println("Incorrect input. Expected an integer.\n\n");
                this.sc = new Scanner(System.in);
            }
        }

    }

    /**
     * Helper method for addAppointment(). It returns the available appointment
     * times for the provided doctor on the given date
     */
    private ArrayList<String> getAvailableTimes(Date date, DoctorEntity my_doc) {
        // if doctor is on leave this day, there are no times
        for (Object obj : doctorSessionBeanRemote.getDoctorsOnLeave(date)) {
            DoctorEntity doc = (DoctorEntity) obj;
            if (doc.getDoctorId().equals(my_doc.getDoctorId())) {
                return new ArrayList<>();
            }
        }

        // Get a list with all "allowed" consultations for the given day
        Calendar date_cal = Calendar.getInstance();
        date_cal.setTime(date);
        ArrayList<Calendar> all_allowed_calendars = TimeFiltrator.getAllTimesOfDate(date_cal);

        // Keep the ones that are at least 2 days in the future
        Calendar lower = Calendar.getInstance();
        lower.add(Calendar.DATE, 2);
        ArrayList<String> times = new ArrayList<>();
        for (Calendar cal : all_allowed_calendars) {
            if (lower.compareTo(cal) < 1) {
                times.add(timeFormatter.format(cal.getTime()));
            }
        }

        // Remove appointment times that are already occupied
        List occupiedTimes = appointmentSessionBeanRemote.retrieveOccupiedTimes(date, my_doc);
        for (Object obj : occupiedTimes) {
            Time my_time = (Time) obj;
            times.remove(timeFormatter.format(my_time));
        }

        return times;
    }

    private void viewPatientAppointments() {
        System.out.println("*** CARS :: Appointment Operation :: View Patient Appointments **** \n ");
        System.out.print("Enter Patient Identity Number> ");

        List appointments;
        try {
            appointments = appointmentSessionBeanRemote.retrievePatientAppointments(this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine()));
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        System.out.println("\n Appointments: \nId | Date | Time | Doctor");
        for (Object obj : appointments) {
            AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
            System.out.println(appointmentEntity);
        }
        System.out.println("\n");
    }

    private void addAppointment() {
        System.out.println("*** CARS :: Appointment Operation :: Add Appointment **** \n ");

        //Print all doctors
        System.out.println("Doctor:\nId | Name");
        for (Object obj : this.doctorSessionBeanRemote.retrieveAllDoctors()) {
            DoctorEntity my_doctor = (DoctorEntity) obj;
            System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
        }

        System.out.print("Enter Doctor Id> ");
        int doctorId = sc.nextInt();
        sc.nextLine();
        DoctorEntity my_doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(doctorId));

        System.out.print("Enter Date> ");
        String dateString = sc.nextLine();
        Date date = java.sql.Date.valueOf(dateString);
        ArrayList<String> times = getAvailableTimes(date, my_doc);

        if (times.isEmpty()) {
            System.out.println("There are no available times for this doctor on the requested date \n");
            return;
        }

        // Print the available times
        System.out.println("Availability for " + my_doc.getFirstName() + " " + my_doc.getLastName() + " on " + dateString + ":");
        for (String time : times) {
            System.out.print(time + " ");
        }

        System.out.print("\n\nEnter Time> ");
        String timeString = sc.nextLine();
        if (!times.contains(timeString)) {
            System.out.println("That time was not allowed. Try again\n");
            return;
        }
        
        System.out.print("Enter Patient Identity Number> ");
        PatientEntity patient;
        try {
            patient = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
            appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(date, java.sql.Time.valueOf(timeString + ":00"), my_doc, patient));
            System.out.println(patient.getFirstName() + " " + patient.getLastName() + " appointment with " + my_doc.getFirstName() + " " + my_doc.getLastName() + " at " + timeString + " on " + dateString + " has been added. \n");
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
        }


    }

    private void cancelAppointment() {
        PatientEntity patientEntity;
        System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
        System.out.print("Enter Patient Identity Number> ");
        try {
            patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Appointments:");
        List app = appointmentSessionBeanRemote.retrievePatientAppointments(patientEntity);

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

    }
}
