/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import entity.DoctorsLeaveEntity;
import java.sql.Date;
import java.util.List;
import util.exception.ClashWithAppointmentException;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;

public interface DoctorSessionBeanRemote {
    public Long createDoctorEntity(DoctorEntity doctorEntity);

    public DoctorEntity retrieveDoctorEntityByRegistration(String registration);

    public DoctorEntity retrieveDoctorEntityByDoctorId(Long doctorId);

    public void updateDoctorEntity(DoctorEntity doctorEntity);

    public void deleteDoctorEntity(Long doctorId);


    public List retrieveAllDoctors();

    public void requestDoctorsLeave(java.sql.Date date, Long doctorId) throws LeaveToCloseInTimeException, DoubleLeaveRequestException, ClashWithAppointmentException;

    public Long createDoctorsLeaveEntity(DoctorsLeaveEntity doctorsLeaveEntity);

    public DoctorsLeaveEntity retrieveDoctorsLeaveEntityById(Long doctorsLeaveId);

    public List getDoctorsOnLeaveBetweenDates(java.sql.Date startDate, java.sql.Date endDate);

    public void getAvailableDoctors(Date date);

    public List getDoctorsOnLeave(Date date);


    
}
