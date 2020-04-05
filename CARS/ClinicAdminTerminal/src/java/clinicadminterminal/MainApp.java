/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.DoctorEntity;
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
    Scanner sc;

    public MainApp() {
        sc = new Scanner(System.in);
    }

    public MainApp(DoctorSessionBeanRemote doctorSessionBeanRemote, StaffSessionBeanRemote staffSessionBeanRemotes, PatientSessionBeanRemote patientSessionBeanRemote) {
        this();

        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.staffSessionBeanRemote = staffSessionBeanRemotes;
        this.patientSessionBeanRemote = patientSessionBeanRemote;
        while (true) {
            System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) **** \n ");
            System.out.println("1: Login ");
            System.out.println("2: Exit \n ");
            System.out.print("> ");
            int response = sc.nextInt();
            sc.nextLine();
            if (response == 1) {
                try{
                    StaffEntity staff = login(sc);
                    mainModule(sc, staff);
                    
                }catch(InvalidLoginException e){ // TODO hanle this in a way that does not crash
                    System.out.println("no such user");
                    break;
                }
                
            } else if (response == 2) {
                System.out.println("Goodbye");
                break;
            } else {
                System.out.println("Incorrect input, try again.");
            }
        }

    }

    private StaffEntity login(Scanner sc) throws InvalidLoginException {
        System.out.println("*** CARS :: Login **** \n ");
        System.out.print("Enter username> ");
        String username = sc.nextLine();
        System.out.print("Enter password> ");
        String password = sc.nextLine();
        return staffSessionBeanRemote.login(username, password);

    }
    
    private void mainModule(Scanner sc, StaffEntity staff){
        while (true) {
            System.out.println("*** CARS :: Main **** \n ");
            System.out.println("You are logged in as " + staff.getFirstName() + staff.getLastName());
            System.out.println("1: Registration Operation");
            System.out.println("2: Appointment Operation");
            System.out.println("3: Administration Operation");
            System.out.println("4: Logout \n");
            System.out.print("> ");
            int response = sc.nextInt();
            sc.nextLine();

            switch(response) {
                case 1:
                    RegistrationModule registrationModule = new RegistrationModule(staff, patientSessionBeanRemote, doctorSessionBeanRemote);
                    break;
                case 2:
                    AppointmentModule appointmentModule = new AppointmentModule(staff);
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
 


    }
}


        
  

//StaffEntity staffEntity = staffSessionBeanRemote.retrieveStaffEntityByUserName("NiklasOPF");
//StaffEntity staffEntity2 = staffSessionBeanRemote.retrieveStaffEntityByUserName("NiklasOPfF");
//System.out.println("Done");
// TESTS
//System.out.println("***creating new doctor  ****");
//DoctorEntity doctorEntity = new DoctorEntity("firstName", "lastName", "registration", "qualifications");
//Long id1 = this.doctorSessionBeanRemote.createDoctorEntity(doctorEntity);
//System.out.println("***creating new staff  ****");
//StaffEntity staffEntity =  new StaffEntity("firstName", "lastName", "userName", "password");
//Long id = staffSessionBeanRemotes.createStaffEntity(staffEntity);
//System.out.println("***done  ****");
//scanner.next();
//System.out.println("***retrieveStaffEntityByStaffId  ****");
//StaffEntity staffEntity = staffSessionBeanRemotes.retrieveStaffEntityByStaffId(1L);
//StaffEntity staffEntity2 = staffSessionBeanRemotes.retrieveStaffEntityByUserName("NiklasOPF");
//System.out.println(staffEntity.getFirstName());
//System.out.println("***done  ****");
//scanner.next();
        
        /*
        System.out.println("***update  ****");
        staffEntity.setFirstName("name");
        staffSessionBeanRemotes.updateStaffEntity(staffEntity);
        System.out.println("***done ****");
        scanner.next();
        
        System.out.println("***retrieveStaffEntityByStaffId  ****");
        staffEntity = staffSessionBeanRemotes.retrieveStaffEntityByStaffId(id);
        System.out.println(staffEntity.getFirstName());
        System.out.println("***done  ****");
        scanner.next();
        
       
        System.out.println("***inputign doctor  ****");
        staffSessionBeanRemotes.deleteStaffEntity(staffEntity.getStaffId());
        System.out.println("***done ****");
        scanner.next();
        
        */
