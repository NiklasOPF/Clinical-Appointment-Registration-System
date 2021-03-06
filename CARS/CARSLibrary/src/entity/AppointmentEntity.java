/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Niklas
 */
@Entity
public class AppointmentEntity implements Serializable { 

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long appointmentId;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private Time time;
    
    
    @ManyToOne
    private DoctorEntity doctorEntity;
    
    @ManyToOne
    private PatientEntity patientEntity;

    public AppointmentEntity() {
    }

    public AppointmentEntity(Date date, Time time, DoctorEntity doctorEntity, PatientEntity patientEntity) {
        this.date = date;
        this.time = time;
        this.doctorEntity = doctorEntity;
        this.patientEntity = patientEntity;
    }


    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (appointmentId != null ? appointmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AppointmentEntity)) {
            return false;
        }
        AppointmentEntity other = (AppointmentEntity) object;
        if ((this.appointmentId == null && other.appointmentId != null) || (this.appointmentId != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return  appointmentId + " | " + date + " | " + new SimpleDateFormat("HH:mm").format(time) + " | " + doctorEntity.getFirstName() + " " + doctorEntity.getLastName();
    }
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public Time getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * @return the doctorEntity
     */
    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    /**
     * @return the patientEntity
     */
    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public Long getTimeToAppointmentInMills() {
        return    time.getTime() + date.getTime() - new java.sql.Date(new java.util.Date().getTime()).getTime();        
    }
    
}
