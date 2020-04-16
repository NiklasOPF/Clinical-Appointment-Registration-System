/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.sql.Date;
import java.util.List;


public interface AppointmentSessionBeanLocal {
        public List retrieveOccupiedTimes(Date date, DoctorEntity doctorEntity);

}
