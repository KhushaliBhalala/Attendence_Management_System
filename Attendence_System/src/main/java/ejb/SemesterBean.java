/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package ejb;

import com.mycompany.attendence_system.SemesterMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SemesterBean {
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<SemesterMaster> getAllSemesters() {
        return em.createNamedQuery("SemesterMaster.findAll", SemesterMaster.class).getResultList();
    }
}