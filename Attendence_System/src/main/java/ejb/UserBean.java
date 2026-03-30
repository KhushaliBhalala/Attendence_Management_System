/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package ejb;

import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author HP
 */
@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public void addUser(UserMaster user) {
        em.persist(user);
    }

    public List<UserMaster> getAllUsers() {
        return em.createNamedQuery("UserMaster.findAll", UserMaster.class).getResultList();
    }

    public UserMaster findUser(int id) {
        return em.find(UserMaster.class, id);
    }

    public void deleteUser(int id) {
        UserMaster u = em.find(UserMaster.class, id);
        if (u != null) {
            em.remove(u);
        }
    }
}
