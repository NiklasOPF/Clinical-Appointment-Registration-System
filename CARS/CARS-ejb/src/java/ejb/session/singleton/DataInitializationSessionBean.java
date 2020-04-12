package ejb.session.singleton;

import ejb.session.stateless.StaffSessionBeanLocal;
import entity.StaffEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Startup
@LocalBean
@Singleton
public class DataInitializationSessionBean { //TODO fix this bean so that the program does not crash

    //@EJB(name = "StaffSessionBeanLocal")
    //private StaffSessionBeanLocal staffSessionBeanLocal;
    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            
            // cant add a check for entity, either by calling an session bean or by using entiy manager
            //if (staffSessionBeanLocal.retrieveStaffEntityByUserName("sdlsdf")==null){
             //   throw new Exception();
            
            //em.persist(new StaffEntity("first", "last", "admin", "password")); // TODO this initializes an staffentity object, which in turn increments the ID. do this in a better way
            //em.flush();
           // Can directly try to input since we don't allow duplicate usernames
           //em.persist(new StaffEntity("first", "last", "admin", "password")); // TODO this initializes an staffentity object, which in turn increments the ID. do this in a better way
           // em.flush();
            
        } catch (Exception e) { // TODO make exception mor especific
            em.persist(new StaffEntity("first", "last", "admin", "password")); // TODO this initializes an staffentity object, which in turn increments the ID. do this in a better way
            em.flush();
        }
//        try
//        {
//            em.persist(new StaffEntity("first", "last", "admin", "password"));
//            em.flush();
//            StaffEntity admin = (StaffEntity) em.createQuery("SELECT DISTINCT p FROM StaffEntity p WHERE p.userName = admin").getResultList().get(0);
//            //throw new Exception();
//            //staffSessionBeanLocal.retrieveStaffEntityByUserName("admin");
//
//        }
//        catch(Exception ex){
//            em.persist(new StaffEntity("first", "last", "admin", "password"));
//            em.flush();
    }

}
