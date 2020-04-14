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
public class DataInitializationSessionBean {

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;
//TODO fix this bean so that the program does not crash

    @PostConstruct
    public void init() {
        System.out.println("osödnfpoasözvnkjdfzvbpközdfbvözkxcsdofnsdkfnlkfnlsdknfl");

        try {
            Query query = em.createQuery("SELECT DISTINCT p FROM StaffEntity p WHERE p.userName = :name");
            query.setParameter("name", "bananer");
            query.getResultList().get(0);
        } catch (Exception e) {
            StaffEntity staffEntity = new StaffEntity("apan", "torsten", "bananer", "pass");
            em.persist(staffEntity);
            em.flush();
        }

    }

}
