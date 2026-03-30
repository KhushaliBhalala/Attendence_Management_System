/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejb;

import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author HP
 */
@Stateless
public class AuthBean {
    
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    
    public UserMaster login(String username, String password, Integer roleId) {
        try {
            return em.createNamedQuery("UserMaster.validate", UserMaster.class)
                    .setParameter("uname", username)
                    .setParameter("pwd", password)
                    .setParameter("rid", roleId)
                    .getSingleResult();
        } catch (Exception e) {
            return null; 
        }
    }
}
