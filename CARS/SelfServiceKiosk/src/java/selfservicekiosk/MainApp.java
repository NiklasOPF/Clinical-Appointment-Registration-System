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
import java.util.Scanner;
import util.exception.InvalidLoginException;

/**
 *
 * @author Niklas
 */
public class MainApp {

    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private StaffSessionBeanRemote staffSessionBeanRemote;
    private PatientSessionBeanRemote patientSessionBeanRemote;
    private AppointmentSessionBeanRemote appointmentSessionBeanRemote;
    private QueueSessionBeanRemote queueSessionBeanRemote;
    Scanner sc;

    public MainApp() {
        sc = new Scanner(System.in);
    }

    public MainApp(DoctorSessionBeanRemote doctorSessionBeanRemote, StaffSessionBeanRemote staffSessionBeanRemotes, PatientSessionBeanRemote patientSessionBeanRemote, AppointmentSessionBeanRemote appointmentSessionBeanRemote, QueueSessionBeanRemote queueSessionBeanRemote) {
        this();

        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.staffSessionBeanRemote = staffSessionBeanRemotes;
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
                System.out.println("heh");
            } else if (response == 2) {
                try {
                    PatientEntity patient = login(sc);
                    System.out.println("Login successful!\n");
                    //mainModule(sc, staff);

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
            return patientSessionBeanRemote.login(idenNo, password);
        } catch (Exception e) {
            throw new InvalidLoginException("That combination of login credentials is not valid!");
        }

    }

    /*private void mainModule(Scanner sc, StaffEntity staff) {
        while (true) {
            System.out.println("*** CARS :: Main **** \n ");
            System.out.println("You are logged in as " + staff.getFirstName() + " " + staff.getLastName());
            System.out.println("\n1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout \n");
            System.out.print("> ");
            int response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    RegistrationModule registrationModule = new RegistrationModule(staff, patientSessionBeanRemote, doctorSessionBeanRemote, appointmentSessionBeanRemote, queueSessionBeanRemote);
                    break;
                case 2:
                    AppointmentModule appointmentModule = new AppointmentModule(staff, patientSessionBeanRemote, doctorSessionBeanRemote, appointmentSessionBeanRemote);
                    break;
                case 3:
                    AdministrationModule administrationModule = new AdministrationModule(staff);
                    break;
                case 4:
                    System.out.println("Logging out");
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }

    }*/
}

