/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicadminterminal;

import ejb.session.singleton.QueueSessionBeanRemote;
import ejb.session.stateless.AppointmentSessionBeanRemote;
import ejb.session.stateless.DoctorSessionBeanRemote;
import ejb.session.stateless.PatientSessionBeanRemote;
import ejb.session.stateless.StaffSessionBeanRemote;
import entity.DoctorEntity;
import entity.DoctorsLeaveEntity;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;


/**
 *
 * @author Niklas
 */
public class Main { 

    @EJB(name = "QueueSessionBeanRemote")
    private static QueueSessionBeanRemote queueSessionBeanRemote;

    @EJB(name = "AppointmentSessionBeanRemote")
    private static AppointmentSessionBeanRemote appointmentSessionBeanRemote;

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
            new MainApp(doctorSessionBeanRemote, staffSessionBeanRemote, patientSessionBeanRemote, appointmentSessionBeanRemote, queueSessionBeanRemote);
    }
    
}
