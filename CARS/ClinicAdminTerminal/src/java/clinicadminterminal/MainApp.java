/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.DoctorEntity;
import entity.StaffEntity;
import java.util.Scanner;

/**
 *
 * @author Niklas
 */
public class MainApp {
    private DoctorSessionBeanRemote doctorSessionBeanRemote;
    private StaffSessionBeanRemote staffSessionBeanRemote;

    public MainApp() {
    }
    
    public MainApp(DoctorSessionBeanRemote doctorSessionBeanRemote, StaffSessionBeanRemote staffSessionBeanRemotes) {
        this();
        this.doctorSessionBeanRemote = doctorSessionBeanRemote;
        this.staffSessionBeanRemote = staffSessionBeanRemotes;
        
        System.out.println("*** Welcome to Clinic Appointment Registration System (CARS) ****");
        Scanner scanner = new Scanner(System.in);

        
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
        
        System.out.println("***retrieveStaffEntityByStaffId  ****");
        //StaffEntity staffEntity = staffSessionBeanRemotes.retrieveStaffEntityByStaffId(1L);
        StaffEntity staffEntity2 = staffSessionBeanRemotes.retrieveStaffEntityByUserName("NiklasOPF");
        //System.out.println(staffEntity.getFirstName());
        System.out.println("***done  ****");
        scanner.next();
        
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
    
    }
    
    
    
    
    
}
