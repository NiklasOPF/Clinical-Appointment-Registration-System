/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Niklas
 */
@Stateless
public class PatientSessionBean implements PatientSessionBeanRemote, PatientSessionBeanLocal {

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;

    public PatientSessionBean(){
        
        
    }
    
    public Long createPatientEntity(PatientEntity patientEntity){
        em.persist(patientEntity);
        em.flush();
        
        return patientEntity.getPatientId();
    }

    
//    public PatientEntity retrievePatientEntityByIdentityNumber(String identityNumber)
//        Query query = em.createQuery("SELECT DISTINCT p FROM DoctorEntity p WHERE p.registration = :name");
//        query.setParameter("name", iden);
//        return (DoctorEntity) query.getResultList().get(0);
//        
//    }

}

    
 