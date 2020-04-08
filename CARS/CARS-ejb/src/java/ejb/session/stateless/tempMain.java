/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.sql.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;

/**
 *
 * @author Niklas
 */
public class tempMain {
    public static void main(String[] args) throws LeaveToCloseInTimeException, DoubleLeaveRequestException{
        DoctorSessionBean doc = new DoctorSessionBean();
//        try {
//            doc.requestDoctorsLeave(new Date(176685001 + Calendar.getInstance().getTime().getTime()), new Long(1));
//        } catch (LeaveToCloseInTimeException ex) {
//            Logger.getLogger(tempMain.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (DoubleLeaveRequestException ex) {
//            Logger.getLogger(tempMain.class.getName()).log(Level.SEVERE, null, ex);
//        }
        doc.requestDoctorsLeave(java.sql.Date.valueOf("2020-04-10"), new Long(1));
    }
    
}
