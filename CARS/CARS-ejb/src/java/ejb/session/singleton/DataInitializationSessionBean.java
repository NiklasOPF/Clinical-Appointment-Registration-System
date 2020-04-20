package ejb.session.singleton;

import ejb.session.stateless.PatientSessionBeanLocal;
import ejb.session.stateless.StaffSessionBeanLocal;
import entity.PatientEntity;
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

    @PostConstruct
    public void init() {
        try {
            Query query = em.createQuery("SELECT DISTINCT p FROM StaffEntity p WHERE p.userName = 'manager'");
            query.getResultList().get(0);
        } catch (Exception e) {
            em.persist(new StaffEntity("Niklas", "Forsstroem", "manager", "password"));
            em.flush();
            //staffSessionBeanLocal.createStaffEntity(new StaffEntity("Linda", "Chua", "manager", "password"));
            

        }

    }

}
