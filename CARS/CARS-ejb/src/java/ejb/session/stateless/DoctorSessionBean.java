/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import com.sun.xml.rpc.processor.schema.UnimplementedFeatureException;
import entity.DoctorEntity;
import entity.DoctorsLeaveEntity;
import java.util.ArrayList;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
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

    public void requestDoctorsLeave(Date date, Long doctorId) throws LeaveToCloseInTimeException, DoubleLeaveRequestException {//TODO throws ...
        Calendar lower = Calendar.getInstance();
        lower.add(Calendar.DAY_OF_MONTH, 3);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        
  
        if (lower.after(cal)){
            System.out.println("mitt fel ");
            throw new LeaveToCloseInTimeException();
        }
        
        //int daysDiff = (int)( (date. - now.getTime()) / (1000 * 60 * 60 * 24));
        
        
        
        // TODO think it inputs the date wrong. It interperets MM as MM-1 since it statrs from 0
        // Need to check for 1 week in advace, no appointments, no double leaves, only one day free per week.
        // Note that checking for one booking per week also takes care of the duplicates
        long dif1 = date.getTime() - new Date(Calendar.getInstance().getTime().getTime()).getTime();
        Long dif2 = new Date(0, 0, 2).getTime() - new Date(0, 0, 0).getTime();
        if (dif1 < dif2) { // In case the time is to close 
            throw new LeaveToCloseInTimeException();
        }
        // get start and end of week
        Date monday = new Date(date.getTime());
        Date sunday = new Date(date.getTime());
        while (monday.getDay() != 1) {
            monday = new Date(monday.getTime() - (new Date(0, 0, 1).getTime() - new Date(0, 0, 0).getTime()));
        }
        while (sunday.getDay() != 0) {
            sunday = new Date(sunday.getTime() + (new Date(0, 0, 1).getTime() - new Date(0, 0, 0).getTime()));
        }
        //retrieve entries between dates
        List docs = getDoctorsOnLeaveBetweenDates(monday, sunday);
        //see if your doctor is included
        if (docs.contains(doctorId)) {
            throw new DoubleLeaveRequestException("There was already an leave instance associated with this doctor during the week of interest");
        }
        //TODO check for appointments once they are implemented

        // Register the leave operation
        this.createDoctorsLeaveEntity(new DoctorsLeaveEntity(retrieveDoctorEntityByDoctorId(doctorId), date));

    }
    
    /**Returns a list where every entry is a doctor element*/
    public List getDoctorsOnLeaveBetweenDates(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT DISTINCT p.doctorEntity FROM DoctorsLeaveEntity p WHERE p.date > :date1 AND p.date < :date2");
        query.setParameter("date1", startDate);
        query.setParameter("date2", endDate);
        List doctorsIds = query.getResultList();
        return doctorsIds;
    }

    public List getDoctorsOnLeave(Date date) {
        Query query = em.createQuery("SELECT DISTINCT p.doctorEntity FROM DoctorsLeaveEntity p WHERE p.date = :date");
        query.setParameter("date", date, TemporalType.DATE);
        return query.getResultList();
    }

    @Override
    public List retrieveAllDoctors() {
        Query query = em.createQuery("SELECT p FROM DoctorEntity p");
        List doctors = query.getResultList();

        return doctors;
    }

    public void getAvailableDoctors(Date date) { // TODO. Fix the issues that we are having with java.sql.Date
        Query query = em.createQuery("SELECT DISTINCT p.doctorId FROM DoctorEntity p");
        List doctorsIds = query.getResultList();

        Query query2 = em.createQuery("SELECT DISTINCT p.doctorEntity.doctorId FROM DoctorsLeaveEntity p WHERE p.date > :date1 AND p.date < :date2");
        query2.setParameter("date1", new Date(date.getTime() - (new Date(0, 0, 1).getTime() - new Date(0, 0, 0).getTime())));
        query2.setParameter("date2", new Date(date.getTime() + (new Date(0, 0, 1).getTime() - new Date(0, 0, 0).getTime())));
        List list = query2.getResultList();

        doctorsIds.removeAll(list);
    }

    @Override
    public Long createDoctorEntity(DoctorEntity doctorEntity) {
        em.persist(doctorEntity);
        em.flush();

        return doctorEntity.getDoctorId();
    }

    public Long createDoctorsLeaveEntity(DoctorsLeaveEntity doctorsLeaveEntity) { // TODO make private and remove from tests / interface in the end. 
        em.persist(doctorsLeaveEntity);
        em.flush();

        return doctorsLeaveEntity.getDoctorsLeaveId();
    }

    @Override
    public DoctorEntity retrieveDoctorEntityByRegistration(String registration) {
        Query query = em.createQuery("SELECT DISTINCT p FROM DoctorEntity p WHERE p.registration = :name");
        query.setParameter("name", registration);
        return (DoctorEntity) query.getResultList().get(0);
    }

    @Override
    public DoctorEntity retrieveDoctorEntityByDoctorId(Long doctorId) {
        DoctorEntity doctorEntity = em.find(DoctorEntity.class, doctorId);
        return doctorEntity;
    }

    public DoctorsLeaveEntity retrieveDoctorsLeaveEntityById(Long doctorsLeaveId) {
        DoctorsLeaveEntity doctorsLeaveEntity = em.find(DoctorsLeaveEntity.class, doctorsLeaveId);
        return doctorsLeaveEntity;
    }

    @Override
    public void updateDoctorEntity(DoctorEntity doctorEntity) {
        em.merge(doctorEntity);
    }

    @Override
    public void deleteDoctorEntity(Long doctorId) {
        DoctorEntity doctorEntity = retrieveDoctorEntityByDoctorId(doctorId);
        em.remove(doctorEntity);
    }

}
