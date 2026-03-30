/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejb;

import com.mycompany.attendence_system.FacultyMaster;
import com.mycompany.attendence_system.RoleMaster;
import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HP
 */
@Stateless
public class FacultyBean {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<FacultyMaster> getAllFaculties() {
        return em.createNamedQuery("FacultyMaster.findAll", FacultyMaster.class).getResultList();
    }

    public void addFaculty(FacultyMaster faculty, String username, String password, Integer subjectId) {
        // 1. Create and Persist UserMaster
        UserMaster user = new UserMaster();
        user.setUsername(username);
        user.setPassword(password);
        // Assuming ID 2 is the 'Faculty' role in your RoleMaster table
        user.setRoleId(em.find(RoleMaster.class, 2));
        em.persist(user);

        // 2. Set Faculty Details
        faculty.setUserId(user);
        faculty.setPassword(password);
        faculty.setSubjectId(em.find(SubjectMaster.class, subjectId));
        faculty.setCreatedDate(new Date());
        faculty.setModifiedDate(new Date());

        // 3. Persist Faculty
        em.persist(faculty);
    }

    public void deleteFaculty(Integer id) {
        FacultyMaster f = em.find(FacultyMaster.class, id);
        if (f != null) {
            UserMaster u = f.getUserId();
            em.remove(f);
            if (u != null) {
                em.remove(u);
            }
        }
    }

    // FacultyBean.java 
    public void updateFaculty(FacultyMaster faculty, String username, String password, Integer subjectId) {
        FacultyMaster existing = em.find(FacultyMaster.class, faculty.getId());
        if (existing != null) {
            existing.setName(faculty.getName());
            existing.setMobileNo(faculty.getMobileNo());
            existing.setEmail(faculty.getEmail());
            existing.setPassword(password); // faculty_master 
            existing.setSubjectId(em.find(SubjectMaster.class, subjectId));
            existing.setModifiedDate(new Date());

            // UserMaster (Login Credentials) 
            UserMaster user = existing.getUserId();
            if (user != null) {
                user.setUsername(username);
                user.setPassword(password);
                em.merge(user);
            }
            em.merge(existing);
        }
    }
}
