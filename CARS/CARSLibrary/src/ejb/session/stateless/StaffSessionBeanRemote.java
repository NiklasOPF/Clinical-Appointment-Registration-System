/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.StaffEntity;
import java.util.List;
import util.exception.InvalidLoginException;

public interface StaffSessionBeanRemote {

    public Long createStaffEntity(StaffEntity staffEntity);

    public StaffEntity retrieveStaffEntityByStaffId(Long staffId);

    public void updateStaffEntity(StaffEntity staffEntity);

    public void deleteStaffEntity(Long staffId);

    public StaffEntity retrieveStaffEntityByUserName(String userName);
    
    public StaffEntity login(String username, String password) throws InvalidLoginException;

    public List retrieveAllStaff();


}
