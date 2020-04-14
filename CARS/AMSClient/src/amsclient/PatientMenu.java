/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amsclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ws.client.appoint.AppointmentEntity;
import ws.client.appoint.DoctorEntity;
import ws.client.patient.PatientEntity;
import ws.client.appoint.PatientEntity;
import ws.client.appoint.Date;
/**
 *
 * @author StudentStudent
 */
public class PatientMenu {

    PatientEntity patient;
    Scanner sc = new Scanner(System.in);
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public PatientMenu() {

    }

    public PatientMenu(PatientEntity patient) {
        this.patient = patient;
        System.out.println("*** AMS Client :: Main **** \n ");

        while (true) {
            System.out.println("*** CARS :: Appointment Operation **** \n ");
            System.out.println("1: View Patient Appointments");
            System.out.println("2: Add Appointment");
            System.out.println("3: Cancel Appointment");
            System.out.println("4: Back \n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 1:
                    System.out.println("*** AMS Client :: View Appointments **** \n ");
                    System.out.println("\n Appointments: \nId | Date | Time | Doctor");
                    List appointments = viewPatientAppointments(patient.getIdentityNumber());
                    for (Object obj : appointments) {
                        AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
                        System.out.println(appointmentEntity);
                    }
                    break;
                case 2:
                    //Strategy: Maintain a list of all possible 
                    System.out.println("*** CARS :: Appointment Operation :: Add Appointment **** \n ");

                    //Print all doctors
                    System.out.println("Doctor:\nId | Name");
                    List doctors = retrieveAllDoctors();
                    for (Object obj : doctors) {
                        DoctorEntity my_doctor = (DoctorEntity) obj;
                        System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
                    }
                    System.out.print("Enter Doctor Id> ");
                    Long doctorId = sc.nextLong();
                    sc.nextLine();
                    DoctorEntity doctor = retrieveDoctorEntityByDoctorId(doctorId);
                    System.out.print("Enter Date> ");
                    String dateString = sc.nextLine();
                    ws.client.appoint.Date date = java.sql.Date.valueOf(dateString);
                    ArrayList<String> times = getAvailableTimes(date, doctor);
                    if (times.size() == 0) {
                        System.out.println("There are no available times for this doctor on the requested date \n");
                        break;
                    }
                    System.out.println("Availability for " + doctor.getFirstName() + " " + doctor.getLastName() + " on " + dateString + ":");
                    for (String time : times) {
                        System.out.print(time + " ");
                    }
                    System.out.print("\n\nEnter Time> ");
                    String timeString = sc.nextLine();
                    if (!times.contains(timeString)) {
                        System.out.println("That time was not allowed. Try again\n");
                        break;
                    }
                    System.out.print("Enter Patient Identity Number> ");
                    String patientId = sc.nextLine();
                    PatientEntity patientAppoint = makeAppointment(patientId, date, timeString, doctor);
                    System.out.println(patientAppoint.getFirstName() + " " + patientAppoint.getLastName() + " appointment with " + doctor.getFirstName()
                            + " " + doctor.getLastName() + " at " + timeString + " on " + dateString + " has been added. \n");
                    break;


                case 3:
                    System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
                    System.out.print("Enter Patient Identity Number> ");

                    try {
                        PatientEntity patientToCancel = retrievePatientEntityByIdentityNumber(sc.nextLine());
                    } catch (Exception e) {
                        System.out.println("Could not find a patient with that identity number.");
                        return; //not sure break or return

                    }
                    List app = retrievePatientAppointments(patientEntity);
                    //System.out.println("Appointments:");
                    System.out.println("\n Appointments:");
                    System.out.println("Id | Date | Time | Doctor");
                    for (Object obj : app) {
                        AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
                        System.out.println(appointmentEntity);
                    }
                    System.out.println("\n");
                    System.out.println("Enter Appointment Id> ");
                    Long appId = sc.nextLong();
                    AppointmentEntity my_app = retrieveAndDeletePatientAppointments(appId);
                    System.out.println(patientAppoint.getFirstName() + " "
                            + patientAppoint.getLastName() + " appointment with "
                            + my_app.getDoctorEntity().getFirstName()
                            + " " + my_app.getDoctorEntity().getLastName()
                            + " at " + timeFormatter.format(my_app.getTime())
                            + " on " + my_app.getDate() + " has been canceled.");
                    break;
                case 4:
                    return;
            }
        }
    }

    private static java.util.List<java.lang.String> getAvailableTimes(ws.client.appoint.Date date, ws.client.appoint.DoctorEntity doctor) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.getAvailableTimes(date, doctor);
    }

    private static ws.client.appoint.PatientEntity makeAppointment(java.lang.String patientId, ws.client.appoint.Date date, java.lang.String timeString, ws.client.appoint.DoctorEntity doctor) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.makeAppointment(patientId, date, timeString, doctor);
    }

    private static java.util.List<java.lang.Object> retrieveAllDoctors() {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.retrieveAllDoctors();
    }

    private static AppointmentEntity retrieveAndDeletePatientAppointments(java.lang.Long appId) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.retrieveAndDeletePatientAppointments(appId);
    }

    private static DoctorEntity retrieveDoctorEntityByDoctorId(java.lang.Long doctorId) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.retrieveDoctorEntityByDoctorId(doctorId);
    }

    private static java.util.List<java.lang.Object> retrievePatientAppointments(ws.client.appoint.PatientEntity patientToCancel) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.retrievePatientAppointments(patientToCancel);
    }

    private static ws.client.appoint.PatientEntity retrievePatientEntityByIdentityNumber(java.lang.String patientId) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.retrievePatientEntityByIdentityNumber(patientId);
    }

    private static java.util.List<java.lang.Object> viewPatientAppointments(java.lang.String identityNumber) {
        ws.client.appoint.AppointmentSessionBeanWebService_Service service = new ws.client.appoint.AppointmentSessionBeanWebService_Service();
        ws.client.appoint.AppointmentSessionBeanWebService port = service.getAppointmentSessionBeanWebServicePort();
        return port.viewPatientAppointments(identityNumber);
    }

}