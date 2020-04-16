/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amsclient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import ws.client.ams.AppointmentEntity;
import ws.client.ams.DoctorEntity;
import ws.client.ams.PatientEntity;
/**
 *
 * @author StudentStudent
 */
public class PatientMenu {

    PatientEntity patient;
    Scanner sc = new Scanner(System.in);
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
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
            System.out.println("4: Logout \n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 1:
                    viewAppointment(patient);
                    break;
                case 2:
                    try{
                    //Strategy: Maintain a list of all possible 
                    System.out.println("*** CARS :: Appointment Operation :: Add Appointment **** \n ");

                    //Print all doctors
                    System.out.println("Doctor:\nId | Name");
                    List doctors = retrieveAllDoctors();
                    if(doctors.isEmpty()) {
                        System.out.println("There is no doctors available.");
                    }
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
                    
                    //long seconds = dateCal.getTimeInMillis();
                    System.out.println("shtap");
                    List<String> times = getAvailableTimes(dateString, doctor);
                    if (times.size() == 0) {
                        System.out.println("There are no available times for this doctor on the requested date \n");
                        break;
                    }
                    System.out.println("Availability for " + doctor.getFirstName() + " " + doctor.getLastName() + " on " + dateString + ":");
                    for (Object time : times) {
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
                    System.out.println("shtap");
                    Date date;
                    //PatientEntity patientAppoint = makeAppointment(patientId, date, timeString, doctor);
                   // System.out.println(patientAppoint.getFirstName() + " " + patientAppoint.getLastName() + " appointment with " + doctor.getFirstName()
                    //        + " " + doctor.getLastName() + " at " + timeString + " on " + dateString + " has been added. \n");
                    break;
                    } catch(Exception ex){
                        System.out.println("Error occured during adding appointment.");
                        return;
                    }
                case 3:
                    System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
                    try {
                        patient = retrievePatientEntityByIdentityNumber(patient.getIdentityNumber());
                    } catch (Exception e) {
                        System.out.println("Could not find a patient with that identity number.");
                        return; //not sure break or return
                    }
                    List<String> appoint = viewAppointment(patient);
                    if(appoint.isEmpty()){
                        System.out.println("There is no appointment available.");
                        break;
                    }
                    System.out.println("\n");
                    System.out.println("Enter Appointment Id> ");
                    Long appId = sc.nextLong();
                    List<String> my_app = retrieveAndDeletePatientAppointments(appId);
                    //doc first, doc last, time,date - make an arraylist out of that
                    System.out.println(patient.getFirstName() + " "
                            + patient.getLastName() + " appointment with "
                            + my_app.get(0)
                            + " " + my_app.get(1)
                            + " at " + my_app.get(2)
                            + " on " + my_app.get(3) + " has been canceled.");
                    break;
                case 4:
                    return;
            }
        }
    }

    public List<String> viewAppointment(PatientEntity patient) {
        System.out.println("*** AMS Client :: View Appointments **** \n ");
        System.out.println("\n Appointments: \nId | Date | Time | Doctor");
        List<String> appointments = viewPatientAppointments(patient.getIdentityNumber());
        for (String string : appointments) {
            System.out.println(string);
        }
        return appointments;
    }

    private static PatientEntity makeAppointment(java.lang.String patientId, ws.client.ams.Date date, java.lang.String timeString, ws.client.ams.DoctorEntity doctor) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.makeAppointment(patientId, date, timeString, doctor);
    }

    private static java.util.List<java.lang.Object> retrieveAllDoctors() {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveAllDoctors();
    }



    private static DoctorEntity retrieveDoctorEntityByDoctorId(java.lang.Long doctorId) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveDoctorEntityByDoctorId(doctorId);
    }

    private static PatientEntity retrievePatientEntityByIdentityNumber(java.lang.String patientId) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.retrievePatientEntityByIdentityNumber(patientId);
    }

    private static java.util.List<java.lang.String> viewPatientAppointments(java.lang.String identityNumber) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.viewPatientAppointments(identityNumber);
    }

    private static java.util.List<java.lang.Object> retrievePatientAppointments(ws.client.ams.PatientEntity patientToCancel) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.retrievePatientAppointments(patientToCancel);
    }

    private static java.util.List<java.lang.String> retrieveAndDeletePatientAppointments(java.lang.Long appId) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.retrieveAndDeletePatientAppointments(appId);
    }

    private static java.util.List<java.lang.String> getAvailableTimes(java.lang.String dateString, ws.client.ams.DoctorEntity doctor) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.getAvailableTimes(dateString, doctor);
    }

}
