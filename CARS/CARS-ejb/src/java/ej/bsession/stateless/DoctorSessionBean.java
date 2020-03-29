/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ej.bsession.stateless;

import entity.DoctorEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Niklas
 */
@Stateless
@Local(DoctorSessionBeanLocal.class)
@Remote(DoctorSessionBeanRemote.class)
public class DoctorSessionBean implements DoctorSessionBeanRemote, DoctorSessionBeanLocal {

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;
    
    public Long createDoctorEntity(DoctorEntity doctorEntity){
        em.persist(doctorEntity);
        em.flush();
        
        return doctorEntity.getDoctorId();
    }


    
    
}
