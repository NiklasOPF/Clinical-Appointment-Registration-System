/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AppointmentSessionBeanLocal;
import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanLocal;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanLocal;
import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginException;

/**
 *
 * @author StudentStudent
 */
@WebService(serviceName = "AMSWebService")
@Stateless()
public class AMSWebService {

    @EJB
    private AppointmentSessionBeanLocal appointmentSessionBeanLocal;
    @EJB
    private PatientSessionBeanLocal patientSessionBeanLocal;
    @EJB
    private DoctorSessionBeanLocal doctorSessionBeanLocal;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    @WebMethod(operationName = "patientLogin")
    public PatientEntity patientLogin(@WebParam(name = "identityNumber") String identityNumber,
            @WebParam(name = "password") String password)
            throws InvalidLoginException {

         return patientSessionBeanLocal.patientLogin(identityNumber, password);
    }

    @WebMethod(operationName = "createPatientEntity")
    public void createPatientEntity(@WebParam(name = "identityNumber") String identityNumber,
            @WebParam(name = "firstName") String firstName,
            @WebParam(name = "lastName") String lastName,
            @WebParam(name = "gender") String gender,
            @WebParam(name = "age") int age,
            @WebParam(name = "phone") String phone,
            @WebParam(name = "address") String address,
            @WebParam(name = "password") String password) {

        if (gender.equals("M") || gender.equals("m")) {
            patientSessionBeanLocal.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.M, age, phone, address, password));
        } else if (gender.equals("F") || gender.equals("f")) {
            patientSessionBeanLocal.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.F, age, phone, address, password));
        } else {
            System.out.println("wrong gender input, if should either be 'M' of 'F'!");
        }
    }

    //view add cancel
    @WebMethod(operationName = "viewPatientAppointments")
    public List viewPatientAppointments(@WebParam(name = "identityNumber") String identityNumber) {
        List appointments = appointmentSessionBeanLocal.
                retrievePatientAppointments(this.patientSessionBeanLocal
                        .retrievePatientEntityByIdentityNumber(identityNumber));
        return appointments;
    }

    private void addAppointment() {

        // for (Object obj : this.doctorSessionBeanLocal.retrieveAllDoctors()) {
        //   DoctorEntity my_doctor = (DoctorEntity) obj;
        //  System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
        // }
        //
        //System.out.print("Enter Doctor Id> ");
        //int doctorId = sc.nextInt();
        // sc.nextLine();
        //DoctorEntity my_doc = doctorSessionBeanLocal.retrieveDoctorEntityByDoctorId(new Long(doctorId));
        //System.out.print("Enter Date> ");
        //String dateString = sc.nextLine();
        //Date date = java.sql.Date.valueOf(dateString);
        //ArrayList<String> times = getAvailableTimes(date, my_doc);
        //if (times.size() == 0) {
        //    System.out.println("There are no available times for this doctor on the requested date \n");
        //    return;
        //}
        // Print the available times
        //System.out.println("Availability for " + my_doc.getFirstName() + " " + my_doc.getLastName() + " on " + dateString + ":");
        //for (String time : times) {
        //    System.out.print(time + " ");
        //}
        //System.out.print("\n\nEnter Time> ");
        //String timeString = sc.nextLine();
        //if (!times.contains(timeString)) {
        //    System.out.println("That time was not allowed. Try again\n");
        //    return;
        //}
        //System.out.print("Enter Patient Identity Number> ");
        //PatientEntity patient = this.patientSessionBeanLocal.retrievePatientEntityByIdentityNumber(sc.nextLine());
        //appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(date, java.sql.Time.valueOf(timeString + ":00"), my_doc, patient));
        //System.out.println(patient.getFirstName() + " " + patient.getLastName() + " appointment with " + my_doc.getFirstName() + " " + my_doc.getLastName() + " at " + timeString + " on " + dateString + " has been added. \n");
    }

    private void cancelAppointment() {
        //PatientEntity patientEntity;
        //System.out.println("*** CARS :: Appointment Operation :: Cancel Appointment **** \n ");
        //System.out.print("Enter Patient Identity Number> ");
        //try {
        //    patientEntity = this.patientSessionBeanLocal.retrievePatientEntityByIdentityNumber(sc.nextLine());
        //} catch (Exception e) {
        //    System.out.println("Could not find a patient with that identity number.");
        //    return;
        //}

        //System.out.println("Appointments:");
        //List app = appointmentSessionBeanLocal.retrievePatientAppointments(patientEntity);
        //System.out.println("wfsdf");
        //System.out.println("\n Appointments:");
        //System.out.println("Id | Date | Time | Doctor");
        //for (Object obj : app) {
        //    AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
        //    System.out.println(appointmentEntity);
        //}
        //System.out.println("\n");
        //System.out.println("Enter Appointment Id> ");
        //int appId = sc.nextInt();
        //AppointmentEntity my_app = appointmentSessionBeanLocal.retrieveAppointmentByAppointmentId(new Long(appId));
        //sc.nextLine();
        //appointmentSessionBeanRemote.deleteAppointment(new Long(appId));
        //System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment with " + my_app.getDoctorEntity().getFirstName() + " " + my_app.getDoctorEntity().getLastName() + " at " + timeFormatter.format(my_app.getTime()) + " on " + my_app.getDate() + " has been canceled.");
    }

    @WebMethod(operationName = "retrieveAllDoctors")
    public List retrieveAllDoctors() {
        return doctorSessionBeanLocal.retrieveAllDoctors();
    }

    @WebMethod(operationName = "retrieveDoctorEntityByDoctorId")
    public DoctorEntity retrieveDoctorEntityByDoctorId(@WebParam(name = "doctorId") Long doctorId) {
        return doctorSessionBeanLocal.retrieveDoctorEntityByDoctorId(doctorId);
    }

    @WebMethod(operationName = "getAvailableTimes")
    public ArrayList<String> getAvailableTimes(@WebParam(name = "date") Date date,
            @WebParam(name = "doctor") DoctorEntity doctor) {
        // Get a list with all "allowed" consultations for the given day
        Calendar date_cal = Calendar.getInstance();
        date_cal.setTime(date);
        ArrayList<Calendar> all_allowed_calendars = getAllTimesOfDate(date_cal);

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
        List occupiedTimes = appointmentSessionBeanLocal.retrieveOccupiedTimes(date, doctor);
        for (Object obj : occupiedTimes) {
            Time my_time = (Time) obj;
            times.remove(timeFormatter.format(my_time));
        }

        return times;
    }

    @WebMethod(operationName = "makeAppointment")
    public PatientEntity makeAppointment(@WebParam(name = "patientId") String patientId,
            @WebParam(name = "date") Date date,
            @WebParam(name = "timeString") String timeString,
            @WebParam(name = "doctor") DoctorEntity doctor) {
        PatientEntity patient = this.patientSessionBeanLocal.retrievePatientEntityByIdentityNumber(patientId);
        appointmentSessionBeanLocal.createAppointmentEntity(new AppointmentEntity(date, java.sql.Time.valueOf(timeString + ":00"), doctor, patient));
        return patient;
    }

    @WebMethod(operationName = "retrievePatientEntityByIdentityNumber")
    public PatientEntity retrievePatientEntityByIdentityNumber(@WebParam(name = "patientId") String identityNumber) {
        return patientSessionBeanLocal.retrievePatientEntityByIdentityNumber(identityNumber);
    }

    @WebMethod(operationName = "retrievePatientAppointments")
    public List retrievePatientAppointments(@WebParam(name = "patientToCancel") PatientEntity patientToCancel) {
        List app = appointmentSessionBeanLocal.retrievePatientAppointments(patientToCancel);
        return app;
    }

    @WebMethod(operationName = "retrieveAndDeletePatientAppointments")
    public AppointmentEntity retrieveAndDeletePatientAppointments(
            @WebParam(name = "appId") Long appId) {
        AppointmentEntity my_app = appointmentSessionBeanLocal.retrieveAppointmentByAppointmentId(new Long(appId));
        appointmentSessionBeanLocal.deleteAppointment(appId);
        return my_app;
    }
    @WebMethod(operationName = "valueOf")
    public Date valueOf(@WebParam(name = "appId") String dateString){
        
        return Date.valueOf(dateString);
    }

    private ArrayList<Calendar> getAllTimesOfDate(Calendar date) {
        // Idea, start with the current time and iterate every 30 mins to get the relevant times
        ArrayList<Calendar> times = new ArrayList<>();

        Calendar lower = Calendar.getInstance();
        lower.setTime(date.getTime());
        lower.set(Calendar.HOUR_OF_DAY, 8);
        lower.set(Calendar.MINUTE, 30);
        lower.set(Calendar.SECOND, 0);

        Calendar upper = getClosingTime(date);
        if (upper == null) {
            return times;
        }

        // increment the lower bound by 30 mins at a time until we reach the last time before closing
        // note that the tiem upper is closing time & is therefore not allowed
        Calendar calendarCopy;
        while (lower.compareTo(upper) < 0) {
            String tmp = timeFormatter.format(lower.getTime());
            if (!"12:30".equals(tmp) && !"13:00".equals(tmp) && !"13:30".equals(tmp)) {
                //times.add(tmp);
                calendarCopy = Calendar.getInstance();
                calendarCopy.setTimeInMillis(lower.getTimeInMillis());
                times.add(calendarCopy);
            }
            lower.add(Calendar.MINUTE, 30);
        }

        return times;

    }

    private Calendar getClosingTime(Calendar date) {
        Calendar upper = Calendar.getInstance();
        upper.setTime(date.getTime());
        // Note that last consultation is 30 mins before closing
        if (new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY)).contains(date.get(Calendar.DAY_OF_WEEK))) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 30);
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 16);
            upper.set(Calendar.MINUTE, 30);
        } else if (date.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 00);
        } else {
            return null;
        }
        return upper;
    }

}
