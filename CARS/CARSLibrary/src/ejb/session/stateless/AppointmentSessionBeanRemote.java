/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.DoctorEntity;
import entity.PatientEntity;
import java.sql.Date;
import java.util.List;

public interface AppointmentSessionBeanRemote {

    public Long createAppointmentEntity(AppointmentEntity appointmentEntity);

    public List retrievePatientAppointments(PatientEntity patientEntity);

    public List retrieveOccupiedTimes(Date date, DoctorEntity doctorEntity);

    public void deleteAppointment(Long appointmentId);

    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId);

    public List retrieveDoctorAppointments(DoctorEntity doctorEntity);
    
}
