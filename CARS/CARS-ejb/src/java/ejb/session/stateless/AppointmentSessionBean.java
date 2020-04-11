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
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

@Local(AppointmentSessionBeanLocal.class)
@Remote(AppointmentSessionBeanRemote.class)
@Stateless
public class AppointmentSessionBean implements AppointmentSessionBeanRemote, AppointmentSessionBeanLocal {

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;

    public AppointmentSessionBean() {
    }


    public List retrievePatientAppointments(PatientEntity patientEntity){
        Query query = em.createQuery("SELECT p FROM AppointmentEntity p WHERE p.patientEntity = :patient");
        query.setParameter("patient", patientEntity);
        List appointments = query.getResultList();
        return appointments;
    }

    
    public void deleteAppointment(Long appointmentId){
        AppointmentEntity appointmentEntity = retrieveAppointmentEntityByAppointmentId(appointmentId);
        em.remove(appointmentEntity);
    }
    
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId){
        return em.find(AppointmentEntity.class, appointmentId); 
    }
           
    public Long createAppointmentEntity(AppointmentEntity appointmentEntity) {
        em.persist(appointmentEntity);
        em.flush();

        return appointmentEntity.getAppointmentId();
    }
    
    public List retrieveOccupiedTimes(Date date, DoctorEntity doctorEntity){
        //Query query = em.createQuery("SELECT p FROM AppointmentEntity p WHERE p.patientEntity == :patient");
        Query query = em.createQuery("SELECT p.time FROM AppointmentEntity p WHERE p.doctorEntity = :doctor AND p.date = :date");
        query.setParameter("doctor", doctorEntity);
        query.setParameter("date", date, TemporalType.DATE);
        List times = query.getResultList();
        return times;
    }
    
    public AppointmentEntity retrieveAppointmentByAppointmentId(Long appointmentId){
        return em.find(AppointmentEntity.class, appointmentId);

    }


    
}
