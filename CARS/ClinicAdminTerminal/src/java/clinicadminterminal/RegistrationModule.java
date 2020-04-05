/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import entity.StaffEntity;
import java.util.Scanner;

/**
 *
 * @author Niklas
 */
public class RegistrationModule {

    public RegistrationModule() {

    }

    public RegistrationModule(StaffEntity staff) {
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
                System.out.println("Enter Identity Number> ");
                String identityNumber = sc.nextLine();
                System.out.println("Enter Password> ");
                String password  = sc.nextLine();
                System.out.println("Enter First Name> ");
                String firstName  = sc.nextLine();
                System.out.println("Enter Last Name> ");
                String lastName  = sc.nextLine();
                System.out.println("Enter Gender> ");
                String gender = sc.nextLine();
                System.out.println("Enter Age> ");
                int age = sc.nextInt();
                sc.nextLine();
                System.out.println("Enter Phone> ");
                String phone  = sc.nextLine();
                System.out.println("Enter Address> ");
                String address = sc.nextLine();
                break;
            case 2:
                System.out.println("*** CARS :: Registration operation :: Register Walk-In Consultation**** \n ");
                
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
