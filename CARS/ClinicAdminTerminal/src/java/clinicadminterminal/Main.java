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
import entity.DoctorsLeaveEntity;
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
        //DoctorEntity doc = new DoctorEntity("Niklas", "Forsstrom", "reg", "qual");
        //doctorSessionBeanRemote.createDoctorEntity(doc);
        //doctorSessionBeanRemote.createDoctorsLeaveEntity(new DoctorsLeaveEntity(doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(1)), java.sql.Date.valueOf("2020-04-12")));
        
        doctorSessionBeanRemote.getAvailableDoctors(java.sql.Date.valueOf("2020-04-12"));
        //doctorSessionBeanRemote.getDoctorsOnLeaveBetweenDates(java.sql.Date.valueOf("2020-01-01"), java.sql.Date.valueOf("2020-05-01"));
        //new MainApp(doctorSessionBeanRemote, staffSessionBeanRemote, patientSessionBeanRemote);
    }
    
}
