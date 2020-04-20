/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.lang.reflect.InvocationTargetException;
import util.exception.InvalidLoginException;
import util.exception.PatientNotFoundException;

public interface PatientSessionBeanLocal {

    public PatientEntity patientLogin(String idenNo, String password) throws InvalidLoginException;

    public Long createPatientEntity(PatientEntity patientEntity);

    public PatientEntity retrievePatientEntityByIdentityNumber(String identityNumber) throws PatientNotFoundException;

    public PatientEntity retrievePatientEntityByPatientId(Long patientId) throws PatientNotFoundException;

    public void updatePatientEntity(PatientEntity patientEntity);

    public void deletePatientEntity(Long patientId);

}
