/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import entity.PatientEntity;
import entity.StaffEntity;
import java.sql.Date;
import java.util.Scanner;
import util.Enum.Gender;

/**
 *
 * @author Niklas
 */
public class RegistrationModule {

    private PatientSessionBeanRemote patientSessionBeanRemote;
    private DoctorSessionBeanRemote doctorSessionBeanRemote;

    public RegistrationModule() {

    }

    public RegistrationModule(StaffEntity staff, PatientSessionBeanRemote patientSessionBeanRemote, DoctorSessionBeanRemote doctorSessionBeanRemote) {
        this.patientSessionBeanRemote = patientSessionBeanRemote;
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
                    PatientEntity patientEntity = new PatientEntity();
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
                //doctorSessionBeanRemote.getAvailableDoctors(new Date()); //TODO Make test class for this method call since it is not wortking

                break;
            case 3:
                System.out.println("*** CARS :: Registration operation :: Regiuster Consultaiton By Appointment**** \n ");

                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
        }
    }

}
