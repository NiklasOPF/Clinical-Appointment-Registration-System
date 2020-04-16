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
import util.exception.ClashWithAppointmentException;
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

    private AppointmentSessionBean appointmentSessionBean;
    private final Long DAYSOF7 = new Long(7 * 24 * 60 * 60 * 1000);

    public DoctorSessionBean() {
    }

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;


    public void requestDoctorsLeave(Date date, Long doctorId) throws LeaveToCloseInTimeException, DoubleLeaveRequestException, ClashWithAppointmentException {//TODO throws ...

        Calendar calToday = new GregorianCalendar();
        Calendar calLeaveDay = new GregorianCalendar();

        calToday.getTime();
        calLeaveDay.setTime(date);
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        calLeaveDay.set(Calendar.HOUR_OF_DAY, 0);
        if (calLeaveDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calLeaveDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            throw new ClashWithAppointmentException("It's a weekend! Choose a weekday.");
        }

        //check for appointment
        try{
            List appointments = appointmentSessionBean.retrieveOccupiedTimes(date, retrieveDoctorEntityByDoctorId(doctorId));
            if (!appointments.isEmpty()) {
                throw new ClashWithAppointmentException("There is an appointment at that time.");
            }
        }catch(NullPointerException e){}
        //check if it is 7 days apart
        //set them to 00:00:00
        Long difference =  calLeaveDay.getTimeInMillis() - calToday.getTimeInMillis();
        if (difference < DAYSOF7) {
            throw new LeaveToCloseInTimeException("To close in time");
        }

        Calendar mondayCal = new GregorianCalendar();
        Calendar fridayCal = new GregorianCalendar();
        mondayCal.setTime(date);
        fridayCal.setTime(date);
        while (mondayCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            mondayCal.add(Calendar.DATE, -1);
        }
        while (fridayCal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            fridayCal.add(Calendar.DATE, 1);
        }
        Date monday = new Date(mondayCal.getTimeInMillis());
        Date friday = new Date(fridayCal.getTimeInMillis());


        //retrieve entries between dates
        List docs = getDoctorsOnLeaveBetweenDates(monday, friday);
        for (Object obj : docs){
            DoctorEntity doc = (DoctorEntity) obj;
            if (doc.getDoctorId().equals(doctorId)){throw new DoubleLeaveRequestException("There was already an leave instance associated with this doctor during the week of interest");}
        }

        // Register the leave operation
        this.createDoctorsLeaveEntity(new DoctorsLeaveEntity(retrieveDoctorEntityByDoctorId(doctorId), date));

    }

    /**
     * Returns a list where every entry is a doctor element
     */
    public List getDoctorsOnLeaveBetweenDates(Date startDate, Date endDate) {
        Query query = em.createQuery("SELECT DISTINCT p.doctorEntity FROM DoctorsLeaveEntity p WHERE p.date > :date1 AND p.date < :date2");
        query.setParameter("date1", startDate);
        query.setParameter("date2", endDate);
        List doctorsIds = query.getResultList();
        return doctorsIds;
    }

    public List getLeavesForDoctor(DoctorEntity doctorEntity) {
        Query query = em.createQuery("SELECT DISTINCT p FROM DoctorsLeaveEntity p WHERE p.doctorEntity = :doc");
        query.setParameter("doc", doctorEntity);
        return query.getResultList();
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

    public void getAvailableDoctors(Date date) {
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

    public Long createDoctorsLeaveEntity(DoctorsLeaveEntity doctorsLeaveEntity) { 
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

    public void deleteDoctorsLeaveEntity(Long doctorsLeaveId) {
        DoctorsLeaveEntity leave = retrieveDoctorsLeaveEntityById(doctorsLeaveId);
        em.remove(leave);
    }

    @Override
    public void deleteDoctorEntity(Long doctorId) {
        DoctorEntity doctorEntity = retrieveDoctorEntityByDoctorId(doctorId);
        em.remove(doctorEntity);
    }

}
