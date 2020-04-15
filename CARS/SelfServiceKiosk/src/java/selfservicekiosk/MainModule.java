/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selfservicekiosk;

import ejb.session.singleton.QueueSessionBeanRemote;
import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author StudentStudent
 */
public class MainModule {

    Scanner sc = new Scanner(System.in);
    PatientEntity patient;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private StaffSessionBeanRemote staffSessionBeanRemote;
    private PatientSessionBeanRemote patientSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public MainModule() {
    }

    public MainModule(PatientEntity patient, DoctorSessionBeanRemote doctorSessionBeanRemote, StaffSessionBeanRemote staffSessionBeanRemote, PatientSessionBeanRemote patientSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote, QueueSessionBeanRemote queueSessionBeanRemote) {
        this.patient = patient;
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.staffSessionBeanRemote = staffSessionBeanRemote;
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        this.queueSessionBeanRemote = queueSessionBeanRemote;
        while (true) {
            System.out.println("*** CARS :: Appointment Operation **** \n ");
            System.out.println("You are login as " + patient.getFirstName() + " " + patient.getLastName());
            System.out.println("1: Register Walk-In Consultaiton");
            System.out.println("2: Register Consultation By Appointment");
            System.out.println("3: View Patient Appointments");
            System.out.println("4: Add Appointment");
            System.out.println("5: Cancel Appointment");
            System.out.println("6: Back \n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 3:
                    viewPatientAppointments();
                    break;
                case 4:
                    addAppointment();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    return;
            }
        }
    }

    private void viewPatientAppointments() {
        System.out.println("*** CARS :: Appointment Operation :: View Patient Appointments **** \n ");
        System.out.print("Enter Patient Identity Number> ");

        List appointments = appointmentSessionBeanRemote.retrievePatientAppointments(this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine()));
        System.out.println("\n Appointments: \nId | Date | Time | Doctor");
        for (Object obj : appointments) {
            AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
            System.out.println(appointmentEntity);
        }
        System.out.println("\n");
    }

    private void addAppointment() {
        //Strategy: Maintain a list of all possible 
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
        if (times.size() == 0) {
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
        PatientEntity patient = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(date, java.sql.Time.valueOf(timeString + ":00"), my_doc, patient));

        System.out.println(patient.getFirstName() + " " + patient.getLastName() + " appointment with " + my_doc.getFirstName() + " " + my_doc.getLastName() + " at " + timeString + " on " + dateString + " has been added. \n");

    }

    private ArrayList<String> getAvailableTimes(Date date, DoctorEntity my_doc) {
        // Get a list with all "allowed" consultations for the given day
        Calendar date_cal = Calendar.getInstance();
        date_cal.setTime(date);
        ArrayList<Calendar> all_allowed_calendars = TimeFiltratorKisok.getAllTimesOfDate(date_cal);

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

    private void cancelAppointment() {
        PatientEntity patientEntity;
        System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
        System.out.print("Enter Patient Identity Number> ");
        try {
            patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Could not find a patient with that identity number.");
            return;
        }

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

    }
}
