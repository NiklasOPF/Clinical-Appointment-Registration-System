/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import util.Encryption;
import util.exception.InvalidLoginException;

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

    @Override
    public PatientEntity patientLogin(String idenNo, String password) throws InvalidLoginException {
        PatientEntity user = retrievePatientEntityByIdentityNumber(idenNo);

        if (user.getPassword().equals(Encryption.encrypt(password + idenNo))) {
            return user;
        } else {
            throw new InvalidLoginException();
        }

    }

    @Override
    public Long createPatientEntity(PatientEntity patientEntity) {
        em.persist(patientEntity);
        em.flush();

        return patientEntity.getPatientId();
    }

    @Override
    public PatientEntity retrievePatientEntityByIdentityNumber(String identityNumber) {
        Query query = em.createQuery("SELECT DISTINCT p FROM PatientEntity p WHERE p.identityNumber = :name");
        query.setParameter("name", identityNumber);
        return (PatientEntity) query.getResultList().get(0);

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
