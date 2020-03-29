/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.DoctorEntity;


public interface DoctorSessionBeanLocal {
    
    public Long createDoctorEntity(DoctorEntity doctorEntity);

    public DoctorEntity retrieveDoctorEntityByRegistration(String registration);

    public DoctorEntity retrieveDoctorEntityByDoctorId(Long doctorId);

    public void updateDoctorEntity(DoctorEntity doctorEntity);

    public void deleteDoctorEntity(Long doctorId);
    
}
