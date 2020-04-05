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
import javax.ejb.EJB;


/**
 *
 * @author Niklas
 */
public class Main {

    @EJB(name = "PatientSessionBeanRemote")
    private static PatientSessionBeanRemote patientSessionBeanRemote;

    @EJB(name = "StaffSessionBeanRemote")
    private static StaffSessionBeanRemote staffSessionBeanRemote;

    @EJB(name = "DoctorSessionBeanRemote")
    private static DoctorSessionBeanRemote doctorSessionBeanRemote;
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //doctorSessionBeanRemote.createDoctorEntity(new DoctorEntity("Niklas", "Forsstroem", "dunno", "none"));
        new MainApp(doctorSessionBeanRemote, staffSessionBeanRemote, patientSessionBeanRemote);
    }
    
}
