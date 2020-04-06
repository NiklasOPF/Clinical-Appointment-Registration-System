/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import com.sun.xml.rpc.processor.schema.UnimplementedFeatureException;
import entity.DoctorEntity;
import java.util.ArrayList;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;

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
    public void requestDoctorsLeave(Date date, Long doctorId) throws LeaveToCloseInTimeException, DoubleLeaveRequestException{//TODO throws ...
        // Need to check for 1 week in advace, no appointments, no double leaves, only one day free per week.
        // Note that checking for one booking per week also takes care of the duplicates
        long dif1 = date.getTime() - new Date(Calendar.getInstance().getTime().getTime()).getTime();
        Long dif2 = new Date(0, 0, 2).getTime() - new Date(0, 0, 0).getTime();
        if (dif1<dif2){ // In case the time is to close 
            throw new LeaveToCloseInTimeException();
        }
        // Check for duplicate bookings
//        List ids = getDoctorsOnLeave(date);
//        if(ids.contains(doctorId)){
//            throw new DoubleLeaveRequestException();
//        }
        
        // TODO check for appontment once that functionallity is set up
        // Can only apply for one leave per week
        Date monday = new Date(0);
        Date friday = new Date(0);
        Locale locale = null;
        final DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
        final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 5) % DayOfWeek.values().length) + 1);
        System.err.println("asd");
        
        
        
    }
    private List getDoctorsOnLeave(Date startDate, Date endDate){
        Query query = em.createQuery("SELECT DISTINCT p.doctorEntity FROM DoctorsLeaveEntity p WHERE p.date = :date");
        query.setParameter("date", startDate);
        List doctorsIds =  query.getResultList();
        
        return doctorsIds;
    }
    
    public List getDoctorsOnLeave(Date date){
        Query query2 = em.createQuery("SELECT DISTINCT p.doctorsLeaveId FROM DoctorsLeaveEntity p WHERE p.date = :date");
        Query query = em.createQuery("SELECT DISTINCT p.doctorEntity FROM DoctorsLeaveEntity p WHERE p.date = :date");
        query.setParameter("date", date);
        List doctorsIds =  query.getResultList();
        
        return doctorsIds;
    }
    
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

    @Override
    public Long[] getAvailableDoctors(java.util.Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
