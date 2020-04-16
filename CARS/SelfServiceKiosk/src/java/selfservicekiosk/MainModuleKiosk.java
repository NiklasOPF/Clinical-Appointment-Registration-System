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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import util.exception.AccesSystemOnWeekendException;

/**
 *
 * @author StudentStudent
 */
public class MainModuleKiosk {

    Scanner sc = new Scanner(System.in);
    PatientEntity patient;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private StaffSessionBeanRemote staffSessionBeanRemote;
    private PatientSessionBeanRemote patientSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public MainModuleKiosk() {
    }

    public MainModuleKiosk(PatientEntity patient, DoctorSessionBeanRemote doctorSessionBeanRemote,
            StaffSessionBeanRemote staffSessionBeanRemote,
            PatientSessionBeanRemote patientSessionBeanRemote,
            AppointmentSessionBeanRemote appointmentSessionBeanRemote,
            QueueSessionBeanRemote queueSessionBeanRemote) {
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
            System.out.println("6: Logout \n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 1:
                    registerWalkInConsultation();
                    break;
                case 2:
                    registerConsultationByAppointment();
                    break;
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
        ArrayList<Calendar> all_allowed_calendars = TimeFiltratorKiosk.getAllTimesOfDate(date_cal);

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
        try {
            patientEntity = this.patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(patient.getIdentityNumber());
        } catch (Exception e) {
            System.out.println("Could not find a patient with that identity number.");
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
    
    private void registerWalkInConsultation() {
        HashMap<Long, Calendar> firstAvailableTimes = new HashMap<>(); // Keeps track of the first available time associated with each doctor
        PatientEntity patientEntity;
        DoctorEntity doctorEntity;
        boolean isBooked;
        List doctors = this.doctorSessionBeanRemote.retrieveAllDoctors();
        List[] occupiedTimes = new List[doctors.size()]; // Arary where each element is a list, each list contains the appointments associated with a doctor

        // Print the list of doctors
        System.out.println("*** CARS :: Registration operation :: Register Walk-In Consultation **** \n ");
        System.out.println("Doctor:\nId | Name");
        for (Object obj : doctors) {
            DoctorEntity my_doctor = (DoctorEntity) obj;
            System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
        }

        // Print header of time slots
        System.out.print("\nAvailability:\nTime");
        for (int i = 0; i < doctors.size(); i++) {
            doctorEntity = ((DoctorEntity) doctors.get(i));
            System.out.print(" | " + doctorEntity.getDoctorId());
            occupiedTimes[i] = appointmentSessionBeanRemote.retrieveOccupiedTimes(new Date(new Long(Calendar.getInstance().getTimeInMillis())), doctorEntity);
        }

        // Print body of time slots
        try {
            for (Calendar time : TimeFiltratorKiosk.getAllTimesToday()) {
                System.out.print("\n" + timeFormatter.format(time.getTime()) + " ");
                for (int i = 0; i < doctors.size(); i++) {
                    doctorEntity = (DoctorEntity) doctors.get(i);
                    isBooked = false; // keeps track if the current consultation time has already been booked for the current doctor
                    for (Object obj : occupiedTimes[i]) { //iterate over all appointments associated with that doctor
                        if (timeFormatter.format((Time) obj).equals(timeFormatter.format(new Time(time.getTimeInMillis())))) {
                            System.out.print("| X ");
                            isBooked = true;
                        }
                    }
                    if (!isBooked) {
                        System.out.print("| O ");
                        if (firstAvailableTimes.get(doctorEntity.getDoctorId()) == null) { // Check if it's the first available time for the current doctor
                            firstAvailableTimes.put(doctorEntity.getDoctorId(), time);
                        }
                    }
                }
            }
        } catch (AccesSystemOnWeekendException e) { // Add output for how to go into test mode
            System.out.println("\n" + e.getMessage());
            return;
        }

        System.out.print("\n\n Enter Doctor Id> ");
        doctorEntity = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(sc.nextInt()));
        sc.nextLine();
        Time timeToBook = new Time(firstAvailableTimes.get(doctorEntity.getDoctorId()).getTimeInMillis());

        System.out.print("Enter Patient Identity Nuber> ");
        patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(new Date(new Long(Calendar.getInstance().getTimeInMillis())), timeToBook, doctorEntity, patientEntity));

        System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment with Dr. " + doctorEntity.getFirstName() + " " + doctorEntity.getLastName() + " has been booked at " + timeFormatter.format(timeToBook));
        System.out.println("Queue number is " + queueSessionBeanRemote.getNewQueueNumber());

    }
    
    private void registerConsultationByAppointment() {
        AppointmentEntity appointmentEntity;
        PatientEntity patientEntity;
        System.out.println("*** CARS :: Registration operation :: Register Consultaiton By Appointment**** \n ");
        System.out.print("Enter Patient Identity Nuber> ");
        try {
            patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Could not find a patient with this identity number.");
            return;
        }
        
        //Print the pre-booked appointments
        System.out.println("\n Appointments: \nId | Date | Time | Doctor");
        for (Object obj : appointmentSessionBeanRemote.retrievePatientAppointments(patientEntity)) {
            appointmentEntity = (AppointmentEntity) obj;
            System.out.println(appointmentEntity);
        }

        System.out.print("\n\nEnter Appointment Id> ");
        try{
            appointmentEntity = appointmentSessionBeanRemote.retrieveAppointmentByAppointmentId(new Long(sc.nextInt()));
            sc.nextLine();
        }catch(Exception e){
            System.out.println("That is not a valid appointment id. Please try again");
            return;
        }
        
        if ( appointmentEntity.getTimeToAppointmentInMills() > 3*3600000 || appointmentEntity.getTimeToAppointmentInMills()<0){
            System.out.println("Appointment time has either passed or is in more than three hours.");
            return;
        } 
        
        System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment is confirmed with Dr. " + appointmentEntity.getDoctorEntity().getFirstName() + " " + appointmentEntity.getDoctorEntity().getLastName() + " at " + timeFormatter.format(appointmentEntity.getTime()));
        System.out.println("Queue number is " + queueSessionBeanRemote.getNewQueueNumber());
        

    }
}
