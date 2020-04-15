/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;  
import javax.persistence.Column;


/**
 *
 * @author Niklas
 */
@Entity
public class DoctorsLeaveEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorsLeaveId;
    @ManyToOne
    private DoctorEntity doctorEntity;
    @Column(nullable = false)
    private Date date;

    public DoctorsLeaveEntity() {
    }

    public DoctorsLeaveEntity(DoctorEntity doctorEntity, Date date) {
        this();
        this.date = date;
        this.doctorEntity = doctorEntity;
        
    }
    
    

    public Long getDoctorsLeaveId() {
        return doctorsLeaveId;
    }

    public void setDoctorsLeaveId(Long doctorsLeaveId) {
        this.doctorsLeaveId = doctorsLeaveId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doctorsLeaveId != null ? doctorsLeaveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DoctorsLeaveEntity)) {
            return false;
        }
        DoctorsLeaveEntity other = (DoctorsLeaveEntity) object;
        if ((this.doctorsLeaveId == null && other.doctorsLeaveId != null) || (this.doctorsLeaveId != null && !this.doctorsLeaveId.equals(other.doctorsLeaveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DoctorsLeaveEntity[ id=" + doctorsLeaveId + " ]";
    }
    
}
