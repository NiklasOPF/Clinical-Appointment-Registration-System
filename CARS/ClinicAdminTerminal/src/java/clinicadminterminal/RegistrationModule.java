/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.singleton.QueueSessionBeanRemote;
import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Array;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import util.Enum.Gender;
import util.exception.AccesSystemOnWeekendException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author Niklas
 */
public class RegistrationModule {

    private PatientSessionBeanRemote patientSessionBeanRemote;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    Scanner sc;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public RegistrationModule() {

    }

    public RegistrationModule(StaffEntity staff, PatientSessionBeanRemote patientSessionBeanRemote, DoctorSessionBeanRemote doctorSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote, QueueSessionBeanRemote queueSessionBeanRemote) {
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        this.queueSessionBeanRemote = queueSessionBeanRemote;
        this.sc = new Scanner(System.in);

        System.out.println("*** CARS :: Registration operation **** \n ");
        System.out.println("1: Register new patient");
        System.out.println("2: Register Walk-In Consultaiton");
        System.out.println("3: Register Consultation By Appointment");
        System.out.println("4: Back \n");
        System.out.print("> ");
        int response = sc.nextInt();
        sc.nextLine();

        switch (response) {
            case 1:
                registerNewPatient();
                break;

            case 2:
                registerWalkInConsultation();
                break;

            case 3:
                registerConsultationByAppointment();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
        }
    }

    private void registerNewPatient() {
        PatientEntity patientEntity;
        int age;
        System.out.println("*** CARS :: Registration operation :: Register new patient**** \n ");
        System.out.print("Enter Identity Number> ");
        String identityNumber = sc.nextLine();
        System.out.print("Enter Password> ");
        String password = sc.nextLine();
        System.out.print("Enter First Name> ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name> ");
        String lastName = sc.nextLine();
        System.out.print("Enter Gender> ");
        String gender = sc.nextLine();
        System.out.print("Enter Age> ");
        try {
            age = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Age must be an integer");
            return;
        }
        System.out.print("Enter Phone> ");
        String phone = sc.nextLine();
        System.out.print("Enter Address> ");
        String address = sc.nextLine();

        switch (gender) {
            case "M":
            case "m":
                patientEntity = new PatientEntity(identityNumber, firstName, lastName, Gender.M, age, phone, address, password);
                break;
            case "F":
            case "f":
                patientEntity = new PatientEntity(identityNumber, firstName, lastName, Gender.F, age, phone, address, password);
                break;
            default:
                System.err.println("The gender must be one of the following {M, m, F, f}");
                return;
        }
        try {
            patientSessionBeanRemote.createPatientEntity(patientEntity);
        } catch (Exception e) {
            System.err.println("Could not create patientEntity. Make sure the Identity number is not duplicated");
            return;
        }

        System.out.println("Patient has been registered successfully!\n ");

    }

    private void registerWalkInConsultation() {
        HashMap<Long, Calendar> firstAvailableTimes = new HashMap<>(); // Keeps track of the first available time associated with each doctor
        PatientEntity patientEntity = null;
        DoctorEntity doctorEntity = null;
        Time timeToBook;
        boolean isBooked;
        List doctors = this.doctorSessionBeanRemote.retrieveAllDoctors();
        if(doctors.isEmpty()){
            System.out.println("No doctors are registered in the system. Please register doctors before you try to book appointments.");
            return;
        }
        List[] occupiedTimes = new List[doctors.size()]; // Array where each element is a list, each list contains the appointments associated with a doctor

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
            for (Calendar time : TimeFiltrator.getAllTimesToday()) {
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
        try{
            doctorEntity = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(sc.nextInt()));
        } catch(Exception e){
            System.out.println("Input must be an integer. Please try again. \n\n");   
        }
        sc.nextLine();
        if (doctorEntity == null){
            System.out.println("There is no registered doctor with that id. \n\n");
            return;
        }
        try{
            timeToBook = new Time(firstAvailableTimes.get(doctorEntity.getDoctorId()).getTimeInMillis());
        }catch(NullPointerException e){
            System.out.println("This doctor does no thave any available slots for the upcomming 3 hours. \nPlease try another doctor. \n\n");
            return;
        }
        System.out.print("Enter Patient Identity Number> ");
        try {
            patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
        } catch (PatientNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
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
