package ejb.session.stateless;

import entity.DoctorEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Niklas
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DoctorSessionBeanTest {

    private final DoctorSessionBeanRemote doctorSessionBeanRemote;

    public DoctorSessionBeanTest() {
        doctorSessionBeanRemote = lookupDoctorSessionBeanRemote();
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
    public void test01CreateDoctorEntity() {
        System.out.println("***** DoctorSesionBeanTest.test01CreateDoctorEntity");
        Long docId = doctorSessionBeanRemote.createDoctorEntity(new DoctorEntity("first name", "last name", "example no.", "example qualification"));
        assertNotNull(docId);
    }

    @Test()
    public void test02RetrieveDoctorByID() {
        System.out.println("***** DoctorSesionBeanTest.test02RetrieveDoctorByID");
        DoctorEntity doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(1));
        assertNotNull(doc);
    }
    
    @Test()
    public void test03RetrieveAllDoctors() {
        System.out.println("***** DoctorSesionBeanTest.test01RetrieveAllVneues");
        doctorSessionBeanRemote.createDoctorEntity(new DoctorEntity("first name", "last name", "example no.2", "example qualification2"));
        List docId = doctorSessionBeanRemote.retrieveAllDoctors();
        assertEquals(docId.size(), 2);
    }
    
    @Test()
    public void test99RemoveDoctorEntity() {
        System.out.println("***** DoctorSesionBeanTest.test99RemoveDoctorEntity");
        doctorSessionBeanRemote.deleteDoctorEntity(new Long(1));
        doctorSessionBeanRemote.deleteDoctorEntity(new Long(2));
        List docId = doctorSessionBeanRemote.retrieveAllDoctors();
        assertEquals(docId.size(), 0);
    }
    
    

    private DoctorSessionBeanRemote lookupDoctorSessionBeanRemote() {
        try {
            Context context = new InitialContext();
            System.out.println("ssdfs");
            //return (DoctorSessionBeanRemote) context.lookup("java:module/ejb.session.stateless/DoctorSessionBeanRemote");/Clinical-Appointment-Registration-System
            return (DoctorSessionBeanRemote) context.lookup("java:global/CARS/CARS-ejb/DoctorSessionBean!ejb.session.stateless.DoctorSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
