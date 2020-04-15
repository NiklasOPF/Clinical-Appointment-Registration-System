/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PatientEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Niklas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PatientSessionBeanTest {
    
    private final PatientSessionBeanRemote patientSessionBeanRemote;

    public PatientSessionBeanTest() {
        patientSessionBeanRemote = lookUppatientSessionBeanRemote();
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    
    
    @Test()
    public void test01CreatePatientEntity() {
        System.out.println("***** PatientSesionBeanTest.test01CreatePatientEntity");
        Long patId = null;
        try{
        patId = patientSessionBeanRemote.createPatientEntity(new PatientEntity("S1234567S", "Niklas", "Forsstrom", util.Enum.Gender.M, 23, "+65 8212 8054", "vetlandavagen 23", "123456"));
        } catch(Exception e){
            
        }
        assertNotNull(patId);
    }
    
    @Test()
    public void test02RetrievePatientEntityByPatientId() {
        System.out.println("***** PatientSesionBeanTest.test02RetrievePatientEntity");
        PatientEntity patient = patientSessionBeanRemote.retrievePatientEntityByPatientId(new Long(1));
        //Long patId = patientSessionBeanRemote.createPatientEntity(new PatientEntity("identity number", "Niklas", "Forsstrom", util.Enum.Gender.M, 23, "+65 8212 8054", "vetlandavagen 23", "password"));
        assertNotNull(patient);
    }
    
    
    
    
    @Test()
    public void test99RemovePatientEntity() {
        System.out.println("***** PatientSesionBeanTest.test99RemovePatientEntity");
        patientSessionBeanRemote.deletePatientEntity(new Long(1));
        PatientEntity patient = patientSessionBeanRemote.retrievePatientEntityByPatientId(new Long(1));
        //Long patId = patientSessionBeanRemote.createPatientEntity(new PatientEntity("identity number", "Niklas", "Forsstrom", util.Enum.Gender.M, 23, "+65 8212 8054", "vetlandavagen 23", "password"));
        assertNull(patient);
    }
    

    
    

    private PatientSessionBeanRemote lookUppatientSessionBeanRemote() {
        try {
            Context context = new InitialContext();
            System.out.println("ssdfs");
            return (PatientSessionBeanRemote) context.lookup("java:global/CARS/CARS-ejb/PatientSessionBean!ejb.session.stateless.PatientSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    
}




    


