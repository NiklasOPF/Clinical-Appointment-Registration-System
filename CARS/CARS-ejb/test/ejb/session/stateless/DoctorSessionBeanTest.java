package ejb.session.stateless;

import entity.DoctorEntity;
import entity.DoctorsLeaveEntity;
import java.sql.Date;
import java.util.Calendar;
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
import util.exception.ClashWithAppointmentException;
import util.exception.DoubleLeaveRequestException;
import util.exception.LeaveToCloseInTimeException;

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
    public void test04CreateDoctorsLeaveEntity() {
        System.out.println("***** DoctorSesionBeanTest.test04CreateDoctorsLeaveEntity");
        // make leave that's five days from now
        Long time = Calendar.getInstance().getTime().getTime() + (new Date(0, 0, 5).getTime() - new Date(0, 0, 0).getTime());
        Long id = doctorSessionBeanRemote.createDoctorsLeaveEntity(new DoctorsLeaveEntity(doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(1)), new Date(time)));
        assertNotNull(id);
    }
    
    @Test()
    public void test05RetrieveDoctorsLeaveEntity() {
        System.out.println("***** DoctorSesionBeanTest.test05RetrieveDoctorsLeaveEntity");
        DoctorsLeaveEntity entity = doctorSessionBeanRemote.retrieveDoctorsLeaveEntityById(new Long(1));
        assertNotNull(entity);
    }
    

    
    @Test()
    public void test06requestDoctorsLeave() {
        System.out.println("***** DoctorSesionBeanTest.test04requestDoctorsLeave");
        // make leave that's three days from now to avoid exception
        Long time = Calendar.getInstance().getTime().getTime() + (new Date(0, 0, 3).getTime() - new Date(0, 0, 0).getTime());
        try {
            doctorSessionBeanRemote.requestDoctorsLeave(new Date(time), new Long(1));
        } catch (LeaveToCloseInTimeException ex) {
            Logger.getLogger(DoctorSessionBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DoubleLeaveRequestException ex) {
            Logger.getLogger(DoctorSessionBeanTest.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ClashWithAppointmentException ex ){
            Logger.getLogger(DoctorSessionBeanTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        //DoctorEntity doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(1));
        //assertNotNull(doc);
    }
    
    @Test()
    public void test08getDoctorsOnLeaveBetweenDates() {
        System.out.println("***** DoctorSesionBeanTest.test07getDoctorsOnLeaveBetweenDates");
        
        List docs = doctorSessionBeanRemote.getDoctorsOnLeaveBetweenDates(java.sql.Date.valueOf("2020-04-03"), java.sql.Date.valueOf("2020-04-20"));
        assertEquals(docs.size(), 1);
    }
    
    
    
    
    
    @Test()
    public void test99RemoveDoctorEntity() {  

        System.out.println("***** DoctorSesionBeanTest.test99RemoveDoctorEntity");
        DoctorEntity doc = doctorSessionBeanRemote.retrieveDoctorEntityByDoctorId(new Long(1));
        //doctorSessionBeanRemote.deleteDoctorEntity(new Long(1));
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
