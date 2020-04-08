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
public class AdministrationModule {

    StaffEntity staffEntity;
    Scanner sc;
    int response;

    public AdministrationModule() {

    }

    public AdministrationModule(StaffEntity staff) {
        sc = new Scanner(System.in);
        staffEntity = staff;
        while (true) {
            System.out.println("*** CARS :: Administraion operation **** \n ");
            System.out.println("1: Patient Management");
            System.out.println("2: Doctor Management");
            System.out.println("3: Staff Management");
            System.out.println("4: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    patientManagement();
                    break;
                case 2:
                    doctorManagement();
                    break;
                case 3:
                    staffManagement();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }

    }
    
    private void staffManagement() { //TODO implement logic
        while(true){
            System.out.println("*** CARS :: Administraion operation :: Staff Management **** \n ");

            System.out.println("1: Add Staff");
            System.out.println("2: View Staff Details");
            System.out.println("3: Update Staff");
            System.out.println("4: Delete Staff");
            System.out.println("5: View All Staff");
            System.out.println("6: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        
        }

    }
    private void doctorManagement(){ //TODO implement logic
         while(true){
            System.out.println("*** CARS :: Administraion operation :: Doctor Management **** \n ");

            System.out.println("1: Add Doctor");
            System.out.println("2: View Doctor Details");
            System.out.println("3: Update Doctor");
            System.out.println("4: Delete Doctor");
            System.out.println("5: View All Doctors");
            System.out.println("6: Leave Management");
            System.out.println("7: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        
        }
        
    }

    private void patientManagement() { //TODO implement logic
        while(true){
            System.out.println("*** CARS :: Administraion operation :: Patient Management **** \n ");

            System.out.println("1: Add Patient");
            System.out.println("2: View Patient Details");
            System.out.println("3: Update Patient");
            System.out.println("4: Delete Patient");
            System.out.println("5: View All Patients");
            System.out.println("6: Back \n");
            System.out.print("> ");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid input");
            }
        
        }

    }

}
