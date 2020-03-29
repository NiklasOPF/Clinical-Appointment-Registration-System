/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Niklas
 */
@Stateless
@Local(StaffSessionBeanLocal.class)
@Remote(StaffSessionBeanRemote.class)
public class StaffSessionBean implements StaffSessionBeanRemote, StaffSessionBeanLocal {

    public StaffSessionBean() {
    }
    
    

    @PersistenceContext(unitName = "CARS-ejbPU")
    private EntityManager em;

    @Override
    public Long createStaffEntity(StaffEntity staffEntity){
        em.persist(staffEntity);
        em.flush();
        
        return staffEntity.getStaffId();
    }
    
    @Override
    public StaffEntity retrieveStaffEntityByStaffId(Long staffId){
        StaffEntity staffEntity = em.find(StaffEntity.class, staffId);
        
        return staffEntity;
    }
    
    @Override
    public void updateStaffEntity (StaffEntity staffEntity) {
        em.merge(staffEntity);
    }

    @Override
    public void deleteStaffEntity(Long staffId){
        // We don't pass the etity itself since it would be unmanaged (pass by value in the function)
        StaffEntity staffEntity = retrieveStaffEntityByStaffId(staffId);
        em.remove(staffEntity);
    }
    
    
}
