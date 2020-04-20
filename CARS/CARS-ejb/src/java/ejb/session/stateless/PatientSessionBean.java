/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import util.Encryption;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;

/**
 *
 * @author Niklas
 */
@Local(PatientSessionBeanLocal.class)
@Remote(PatientSessionBeanRemote.class)
@Stateless
public class PatientSessionBean implements PatientSessionBeanRemote, PatientSessionBeanLocal {

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;

    public PatientSessionBean() {

    }

    /**
     *
     * @param idenNo
     * @param password
     * @return
     * @throws InvalidLoginException
     * @throws PatientNotFoundException
     */
    @Override
    public PatientEntity patientLogin(String idenNo, String password) throws InvalidLoginException {
        PatientEntity user;
        try {
            user = retrievePatientEntityByIdentityNumber(idenNo);
        } catch (PatientNotFoundException ex) {
            throw new InvalidLoginException("Could not find a patient with that identity number.");
        }

        if (user.getPassword().equals(Encryption.encrypt(password + idenNo))) {
            return user;
        } else {
            throw new InvalidLoginException("The given password does not match the identity number of the patient.");
        }

    }

    @Override
    public Long createPatientEntity(PatientEntity patientEntity) {
        em.persist(patientEntity);
        em.flush();

        return patientEntity.getPatientId();
    }

    /**
     *
     * 
     * @param identityNumber: Identifier for the PatientEntity
     * @return An instance of PatientEntity associated with the unique identityNumber
     * @throws PatientNotFoundException
     */
    @Override
    public PatientEntity retrievePatientEntityByIdentityNumber(String identityNumber) throws PatientNotFoundException{
        try{Query query = em.createQuery("SELECT DISTINCT p FROM PatientEntity p WHERE p.identityNumber = :name");
        query.setParameter("name", identityNumber);
        return (PatientEntity) query.getResultList().get(0);
        }catch(java.lang.ArrayIndexOutOfBoundsException e){
            throw new PatientNotFoundException("Could not find a patient with that ID.");
        }

    }

    @Override
    public PatientEntity retrievePatientEntityByPatientId(Long patientId) {
        return em.find(PatientEntity.class, patientId);
    }

    @Override
    public void updatePatientEntity(PatientEntity patientEntity) {
        em.merge(patientEntity);
    }

    @Override
    public void deletePatientEntity(Long patientId) {
        PatientEntity patientEntity = retrievePatientEntityByPatientId(patientId);
        em.remove(patientEntity);
    }

    public List retrieveAllPatients() {
        Query query = em.createQuery("SELECT p FROM PatientEntity p");
        return query.getResultList();

    }

}
