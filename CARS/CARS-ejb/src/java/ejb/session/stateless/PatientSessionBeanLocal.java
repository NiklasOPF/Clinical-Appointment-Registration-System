/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import util.exception.InvalidLoginException;

public interface PatientSessionBeanLocal {

    public PatientEntity login(String idenNo, String password) throws InvalidLoginException;

    public Long createPatientEntity(PatientEntity patientEntity);

    public PatientEntity retrievePatientEntityByIdentityNumber(String identityNumber);

    public PatientEntity retrievePatientEntityByPatientId(Long patientId);

    public void updatePatientEntity(PatientEntity patientEntity);

    public void deletePatientEntity(Long patientId);

}
