/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amsclient;

import java.util.Scanner;
import ws.client.ams.InvalidLoginException_Exception;
import ws.client.ams.PatientEntity;


/**
 *
 * @author StudentStudent
 */
public class MainAppAMS {
    // TODO code application logic here

    Scanner sc = new Scanner(System.in);

    public MainAppAMS() {
        while (true) {
            String identityNumber;
            String firstName;
            String lastName;
            String gender;
            int age;
            String phone;
            String address;
            String password;
            PatientEntity patientEntity;
            System.out.println("*** Welcome to AMS Client ***\n");
            System.out.println("1: Register");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            System.out.print("> ");
            Integer response = sc.nextInt();
            sc.nextLine();
            switch (response) {
                case 1:
                    System.out.println("*** AMS Client:: Register **** \n ");
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
                    createPatientEntity(identityNumber, firstName, lastName, gender, age, phone, address, password);
                    break;
                case 2:
                    System.out.println("*** AMS Client:: Login **** \n ");
                    System.out.print("Enter identity number> ");
                    identityNumber = sc.nextLine();
                    System.out.print("Enter password> ");
                    password = sc.nextLine();
                    try {
                        PatientEntity patient = patientLogin(identityNumber,password);
                        PatientMenu patientMenu = new PatientMenu(patient);
                    } catch (Exception e) {
                        System.out.println("Error logging in" + e);
                    }
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void createPatientEntity(java.lang.String identityNumber, java.lang.String firstName, java.lang.String lastName, java.lang.String gender, int age, java.lang.String phone, java.lang.String address, java.lang.String password) {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        port.createPatientEntity(identityNumber, firstName, lastName, gender, age, phone, address, password);
    }

    private static PatientEntity patientLogin(java.lang.String identityNumber, java.lang.String password) throws InvalidLoginException_Exception {
        ws.client.ams.AMSWebService_Service service = new ws.client.ams.AMSWebService_Service();
        ws.client.ams.AMSWebService port = service.getAMSWebServicePort();
        return port.patientLogin(identityNumber, password);
    }

 


}
