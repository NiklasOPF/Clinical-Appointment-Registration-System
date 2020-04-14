/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PatientSessionBeanRemote;
import entity.PatientEntity;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginException;

/**
 *
 * @author StudentStudent
 */
@WebService(serviceName = "PatientSessionBeanWebService")
@Stateless()
public class PatientSessionBeanWebService {

    @EJB
    private PatientSessionBeanRemote patientSessionBeanRemote;

    @WebMethod(operationName = "login")
    public PatientEntity login(@WebParam(name = "identityNumber") String identityNumber,
            @WebParam(name = "password") String password)
            throws InvalidLoginException {

        return patientSessionBeanRemote.login(identityNumber, password);
    }

    @WebMethod(operationName = "createPatientEntity")
    public void createPatientEntity(@WebParam(name = "identityNumber") String identityNumber,
            @WebParam(name = "firstName") String firstName,
            @WebParam(name = "lastName") String lastName,
            @WebParam(name = "gender") String gender,
            @WebParam(name = "age") int age,
            @WebParam(name = "phone") String phone,
            @WebParam(name = "address") String address,
            @WebParam(name = "password") String password) {

        if (gender.equals("M") || gender.equals("m")) {
            patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.M, age, phone, address, password));
        } else if (gender.equals("F") || gender.equals("f")) {
            patientSessionBeanRemote.createPatientEntity(new PatientEntity(identityNumber, firstName, lastName, util.Enum.Gender.F, age, phone, address, password));
        } else {
            System.out.println("wrong gender input, if should either be 'M' of 'F'!");
        }

    }
}
