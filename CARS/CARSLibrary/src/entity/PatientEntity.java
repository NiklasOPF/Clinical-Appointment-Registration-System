/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import util.Enum.Gender;

/**
 *
 * @author Niklas
 */
@Entity
public class PatientEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    @Column(unique = true, nullable = false)
    private String identityNumber;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    @Size(min = 6, max = 6)
    private String password;

    @OneToMany(mappedBy = "patientEntity")
    private List<AppointmentEntity> appointmentEntities;

    public PatientEntity() {
    }

    public PatientEntity(String identityNumber, String firstName, String lastName, Gender gender, int age, String phone, String address, String password) {
        this();
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getPatientId() != null ? getPatientId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PatientEntity)) {
            return false;
        }
        PatientEntity other = (PatientEntity) object;
        if ((this.getPatientId() == null && other.getPatientId() != null) || (this.getPatientId() != null && !this.patientId.equals(other.patientId))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        if (gender.equals(Gender.F)) {
            return identityNumber + " | " + firstName + " | " + lastName + " | F" + " | " + age + " | " + phone + " | " + address + " | " + password;

        } else {
            return identityNumber + " | " + firstName + " | " + lastName + " | M" + " | " + age + " | " + phone + " | " + address + " | " + password;
        }
    }

    /**
     * @return the identityNumber
     */
    public String getIdentityNumber() {
        return identityNumber;
    }

    /**
     * @param identityNumber the identityNumber to set
     */
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the appointmentEntities
     */
    public List<AppointmentEntity> getAppointmentEntities() {
        return appointmentEntities;
    }

    /**
     * @param appointmentEntities the appointmentEntities to set
     */
    public void setAppointmentEntities(List<AppointmentEntity> appointmentEntities) {
        this.appointmentEntities = appointmentEntities;
    }

}
