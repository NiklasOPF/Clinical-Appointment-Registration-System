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
import entity.DoctorEntity;
import entity.PatientEntity;
import entity.StaffEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.ejb.EJBException;
import util.exception.InvalidLoginException;

/**
 *
 * @author Niklas
 */
public class MainAppKiosk {

    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private PatientSessionBeanRemote patientSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    Scanner sc;

    public MainAppKiosk() {
        sc = new Scanner(System.in);
    }

    public MainAppKiosk(DoctorSessionBeanRemote doctorSessionBeanRemote, PatientSessionBeanRemote patientSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote, QueueSessionBeanRemote queueSessionBeanRemote) {
        this();

        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        this.appointmentSessionBeanRemote = appointmentSessionBeanRemote;
        this.queueSessionBeanRemote = queueSessionBeanRemote;
        while (true) {
            System.out.println("*** Welcome to Self-Service Kiosk **** \n ");
            System.out.println("1: Register ");
            System.out.println("2: Login ");
            System.out.println("3: Exit \n ");
            System.out.print("> ");
            int response = sc.nextInt();
            sc.nextLine();
            if (response == 1) {
                String identityNumber;
                String firstName;
                String lastName;
                String gender;
                int age;
                String phone;
                String address;
                String password;
                System.out.println("*** CARS :: Administraion operation :: Patient Management :: Add Patient **** \n ");
                try {
                    System.out.print("Enter Identity Number> ");
                    identityNumber = sc.nextLine();
                    System.out.print("Enter First Name> ");
                    firstName = sc.nextLine();
                    System.out.print("Enter Last Name> ");
                    lastName = sc.nextLine();
                    System.out.print("Enter Gender> ");
                    gender = sc.nextLine();
                    System.out.print("Enter Age> ");
                    age = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Phone> ");
                    phone = sc.nextLine();
                    System.out.print("Enter Address> ");
                    address = sc.nextLine();
                    while (true) {
                        System.out.print("Enter Password> ");
                        password = sc.nextLine();
                        if (password.matches("[0-9]+") && password.length() == 6) {
                            break;
                        } else {
                            System.out.println("wrong password input, password has to be in 6 digits");
                        }
                    }
                    if (gender.equals("M") || gender.equals("m")) {
                        patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.M, age, phone, address, password));
                    } else if (gender.equals("F") || gender.equals("f")) {
                        patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.F, age, phone, address, password));
                    } else {
                        System.out.println("wrong gender input, if should either be 'M' of 'F'!");
                    }
                } catch (Exception ex) {
                    System.out.println("Please enter the particulars");
                }
            } else if (response == 2) {
                try {
                    PatientEntity patient = login(sc);
                    System.out.println("Login successful!\n");
                    MainModuleKiosk mainModule = new MainModuleKiosk(patient, doctorSessionBeanRemote,
                            patientSessionBeanRemote, appointmentSessionBeanRemote, queueSessionBeanRemote);

                } catch (InvalidLoginException e) {
                    System.out.println(e.getMsg() + "\n");
                }
            } else if (response == 3) {
                System.out.println("Goodbye");
                break;
            } else {
                System.err.println("Incorrect input, try again.");
            }
        }

    }

    private PatientEntity login(Scanner sc) throws InvalidLoginException {
        System.out.println("*** CARS :: Login **** \n ");
        System.out.print("Enter identity number> ");
        String idenNo = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();
        try {
            return patientSessionBeanRemote.patientLogin(idenNo, password);
        } catch (Exception e) {
            throw new InvalidLoginException("That combination of login credentials is not valid!");
        }

    }

}
