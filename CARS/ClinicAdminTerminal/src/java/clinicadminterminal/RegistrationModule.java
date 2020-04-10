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
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import util.Enum.Gender;

/**
 *
 * @author Niklas
 */
public class RegistrationModule {

    private PatientSessionBeanRemote patientSessionBeanRemote;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public RegistrationModule() {

    }

    public RegistrationModule(StaffEntity staff, PatientSessionBeanRemote patientSessionBeanRemote, DoctorSessionBeanRemote doctorSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote, QueueSessionBeanRemote queueSessionBeanRemote) {
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        this.queueSessionBeanRemote = queueSessionBeanRemote;
        //TODO
        Scanner sc = new Scanner(System.in);
        System.out.println("*** CARS :: Registration operation **** \n ");
        System.out.println("You are logged in as " + staff.getFirstName() + staff.getLastName());
        System.out.println("1: Register new patient");
        System.out.println("2: Register Walk-In Consultaiton");
        System.out.println("3: Register Consultation By Appointment");
        System.out.println("4: Back \n");
        System.out.print("> ");
        int response = sc.nextInt();
        sc.nextLine();

        
        PatientEntity patientEntity;
        switch (response) {
            case 1:
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
                int age = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Phone> ");
                String phone = sc.nextLine();
                System.out.print("Enter Address> ");
                String address = sc.nextLine();

                try {
                    patientEntity = new PatientEntity();
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
                            throw new Exception();
                    }
                    Long id = patientSessionBeanRemote.createPatientEntity(patientEntity);
                } catch (Exception e) {
                    System.err.println("wrong input"); // TODO make specific input error
                }

                break;

            case 2:
                System.out.println("*** CARS :: Registration operation :: Register Walk-In Consultation**** \n ");

                System.out.println("Doctor:\nId | Name");
                List doctors = this.doctorSessionBeanRemote.retrieveAllDoctors();
                for (Object obj : doctors) {
                    DoctorEntity my_doctor = (DoctorEntity) obj;
                    System.out.println(my_doctor.getDoctorId() + " | " + my_doctor.getFirstName() + " " + my_doctor.getLastName());
                }

                System.out.print("\nAvailability:\nTime");
                ArrayList<List> occupiedTimes = new ArrayList<>();
                List[] occTimes = new List[doctors.size()];

                for (int i = 0; i < doctors.size(); i++) {
                    DoctorEntity my_doctor = (DoctorEntity) doctors.get(i);
                    System.out.print(" | " + my_doctor.getDoctorId());
                    Date sdlkfsdlk = new Date(new Long(Calendar.getInstance().getTimeInMillis()));
                    occTimes[i] = appointmentSessionBeanRemote.retrieveOccupiedTimes(sdlkfsdlk, my_doctor);
                }

//                for (Object obj : doctors) {
//                    DoctorEntity my_doctor = (DoctorEntity) obj;
//                    System.out.print(" | " + my_doctor.getDoctorId());
//                    Date sdlkfsdlk = new Date(new Long(Calendar.getInstance().getTimeInMillis()));
//                    occupiedTimes.add(appointmentSessionBeanRemote.retrieveOccupiedTimes(sdlkfsdlk, my_doctor));
//                }
                //appointmentSessionBeanRemote.createAppointmentEntity(new AppointmentEntity(new Date(new Long(Calendar.getInstance().getTimeInMillis())),java.sql.Time.valueOf("14:00:00"), (DoctorEntity) doctors.get(0), patientSessionBeanRemote.retrievePatientEntityByIdentityNumber("sdfsd")));
                ArrayList<Calendar> times = getAllTimesToday();
                HashMap<Long, Calendar> firstAvailableTimes = new HashMap<>();

                boolean flag;

                for (Calendar time : times) {
                    System.out.print("\n" + timeFormatter.format(time.getTime()) + " ");
                    //for (Object obj : doctors) {
                    for (int i = 0; i < doctors.size(); i++) {
                        DoctorEntity my_doctor = (DoctorEntity) doctors.get(i);
                        List doctime = occTimes[i];
                        flag = false;
                        for (Object obj : doctime) {
                            Time tmp_time = (Time) obj;
                            if (timeFormatter.format(tmp_time).equals(timeFormatter.format(new Time(time.getTimeInMillis())))) {
                                System.out.print("| X ");
                                flag = true;
                                
                            } 
                        }
                        
                        if (!flag){
                            System.out.print("| O ");
                            if(firstAvailableTimes.get(my_doctor.getDoctorId()) == null){ // Check if it's the first available time
                                    firstAvailableTimes.put(my_doctor.getDoctorId(), time);
                            }
                        }
                    }
                }
                System.out.print("\n\n Enter Doctor Id> ");
                int docId = sc.nextInt();
                sc.nextLine();
                DoctorEntity doctorEntity = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(docId));
                
                Time timeToBoo = new Time(firstAvailableTimes.get(doctorEntity.getDoctorId()).getTimeInMillis());
                
                System.out.print("Enter Patient Identity Nuber> ");
                String patId = sc.nextLine();
                patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(patId);
                AppointmentEntity appEnt = new AppointmentEntity(new Date(new Long(Calendar.getInstance().getTimeInMillis())), timeToBoo, doctorEntity, patientEntity);
                appointmentSessionBeanRemote.createAppointmentEntity(appEnt);
                System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment with Dr. " + doctorEntity.getFirstName() + " " + doctorEntity.getLastName() + " has been booked at " + timeFormatter.format(timeToBoo));
                System.out.println("Queue number is " + queueSessionBeanRemote.getNewQueueNumber());

                //doctorSessionBeanRemote.getAvailableDoctors(new Date()); //TODO Make test class for this method call since it is not wortking
                break;

            case 3:
                System.out.println("*** CARS :: Registration operation :: Regiuster Consultaiton By Appointment**** \n ");
                
                System.out.print("Enter Patient Identity Nuber> ");
                //String patId2 = sc.nextLine();
                patientEntity = patientSessionBeanRemote.retrievePatientEntityByIdentityNumber(sc.nextLine());
                
                List appointments = appointmentSessionBeanRemote.retrievePatientAppointments(patientEntity);
                    System.out.println("\n Appointments: \nId | Date | Time | Doctor");
                    //System.out.println("Id | Date | Time | Doctor");
                    for (Object obj : appointments) {
                        AppointmentEntity appointmentEntity = (AppointmentEntity) obj;
                        System.out.println(appointmentEntity);
                    }
                System.out.println("\n");
                System.out.print("Enter Appointment Id> ");
                AppointmentEntity appointmentEntity = appointmentSessionBeanRemote.retrieveAppointmentByAppointmentId(new Long(sc.nextInt()));
                sc.nextLine();
                
               
                
                System.out.println(patientEntity.getFirstName() + " " + patientEntity.getLastName() + " appointment is confirmed with Dr. " + appointmentEntity.getDoctorEntity().getFirstName() + " " + appointmentEntity.getDoctorEntity().getLastName() + " at " + timeFormatter.format(appointmentEntity.getTime()));
                System.out.println("Queue number is " + queueSessionBeanRemote.getNewQueueNumber());
                
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
        }
    }

//    private ArrayList<String> getConsultationTimeList() {
//
//        ArrayList<String> times = new ArrayList<>();
//        times.add(timeFormatter.format(java.sql.Time.valueOf("09:00:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("09:30:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("10:00:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("10:30:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("14:00:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("14:30:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("15:00:00")));
//        times.add(timeFormatter.format(java.sql.Time.valueOf("15:30:00")));
//
//        Calendar lower = Calendar.getInstance();
//        Calendar upper = Calendar.getInstance();
//        upper.add(Calendar.HOUR, 3);
//
//        return times;
//    }

    private ArrayList<Calendar> getAllTimesToday() {
        //Idea, start with the current time and iterate every 30 mins to get the relevant times
        ArrayList<Calendar> times = new ArrayList<>();
        Calendar current = Calendar.getInstance();
        Calendar lower = Calendar.getInstance();
        lower.set(Calendar.HOUR_OF_DAY, 8);
        lower.set(Calendar.MINUTE, 30);
        lower.set(Calendar.SECOND, 0);
        Calendar upper = Calendar.getInstance();
        // Note that last consultation is 30 mins before closing
        if (new HashSet<>(Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY)).contains(current.get(Calendar.DAY_OF_WEEK))) { // Mon Tu Wed
            upper.set(Calendar.HOUR_OF_DAY, 17);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            upper.set(Calendar.HOUR_OF_DAY, 16);
            upper.set(Calendar.MINUTE, 30);

        } else if (current.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) { //FRI
            upper.set(Calendar.HOUR_OF_DAY, 22); //TODO change back to 17
            upper.set(Calendar.MINUTE, 00);

        } else {
            System.out.println("Get back on a weekday!");
            return null;
        }

        if (current.compareTo(lower) < 1) { // If earlier then opening time
            current.setTimeInMillis(lower.getTimeInMillis());
            //current.set(Calendar.HOUR_OF_DAY, lower.get(Calendar.HOUR_OF_DAY));
            //current.set(Calendar.MINUTE, lower.get(Calendar.MINUTE));
            //current.set(Calendar.SECOND, lower.get(Calendar.SECOND));
        } else {
            lower.setTimeInMillis(current.getTimeInMillis());
            //lower.set(Calendar.HOUR_OF_DAY, current.get(Calendar.HOUR_OF_DAY));
            //lower.set(Calendar.MINUTE, current.get(Calendar.MINUTE));
            //lower.set(Calendar.SECOND, current.get(Calendar.SECOND));

            if (current.get(Calendar.SECOND) != 0) { // round to next minute
                current.add(Calendar.MINUTE, 1);
                current.set(Calendar.SECOND, 0);
            }

            // round to next half an hour
            if (!new HashSet<>(Arrays.asList(0, 30)).contains(current.get(Calendar.MINUTE))) {
                current.add(Calendar.MINUTE, 30 - (current.get(Calendar.MINUTE) % 30));
            }
        }
        Calendar calendarCopy;
        //                calendarCopy = Calendar.getInstance();
        //        calendarCopy.setTimeInMillis(lower.getTimeInMillis());
        //        calendarCopy.add(Calendar.HOUR_OF_DAY, 3);

        lower.add(Calendar.HOUR_OF_DAY, 3);
        while (current.compareTo(upper) < 1 && current.compareTo(lower) < 1) {

            String tmp = timeFormatter.format(current.getTime());
            if (!"12:30".equals(tmp) && !"13:00".equals(tmp) && !"13:30".equals(tmp)) {
                //times.add(tmp);
                calendarCopy = Calendar.getInstance();
                calendarCopy.setTimeInMillis(current.getTimeInMillis());
                times.add(calendarCopy);
            }
            current.add(Calendar.MINUTE, 30);
        }

        return times;
    }

}
