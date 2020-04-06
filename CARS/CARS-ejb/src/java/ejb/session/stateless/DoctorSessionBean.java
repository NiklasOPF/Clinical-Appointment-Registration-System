/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Niklas
 */
@Stateless
@Local(DoctorSessionBeanLocal.class)
@Remote(DoctorSessionBeanRemote.class)
public class DoctorSessionBean implements DoctorSessionBeanRemote, DoctorSessionBeanLocal {

    public DoctorSessionBean() {
    }
    
    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;
    
    
    @Override
    public List retrieveAllDoctors(){
        Query query = em.createQuery("SELECT DISTINCT p.doctorId FROM DoctorEntity p");
        List doctorsIds =  query.getResultList();
        
        return doctorsIds;
    }
    
    public Long[] getAvailableDoctors(Date date){ // TODO. Finish implementation of this after the leave table is working
        Query query = em.createQuery("SELECT DISTINCT p.doctorId FROM DoctorEntity p");
        List doctorsIds =  query.getResultList();
        
        
        Query query2 = em.createQuery("SELECT DISTINCT p.doctorsLeaveId FROM DoctorsLeaveEntity p WHERE p.date = :date");
        query2.setParameter("date", date);
        ArrayList<Long> leaves = (ArrayList<Long>) query2.getResultList();
        
        doctorsIds.removeAll(leaves);
        return (Long[]) doctorsIds.toArray();
    }
    
    @Override
    public Long createDoctorEntity(DoctorEntity doctorEntity){
        em.persist(doctorEntity);
        em.flush();
        
        return doctorEntity.getDoctorId();
    }
    
    
 
    @Override
    public DoctorEntity retrieveDoctorEntityByRegistration(String registration){
        Query query = em.createQuery("SELECT DISTINCT p FROM DoctorEntity p WHERE p.registration = :name");
        query.setParameter("name", registration);
        return (DoctorEntity) query.getResultList().get(0);
    }
    
    
    @Override
    public DoctorEntity retrieveDoctorEntityByDoctorId(Long doctorId) {
        DoctorEntity doctorEntity = em.find(DoctorEntity.class, doctorId);
        return doctorEntity;
    }
    
    @Override
    public void updateDoctorEntity (DoctorEntity doctorEntity) {
        em.merge(doctorEntity);
    }
 
    @Override
    public void deleteDoctorEntity(Long doctorId){
        DoctorEntity doctorEntity = retrieveDoctorEntityByDoctorId(doctorId);
        em.remove(doctorEntity);
    }

    
}
