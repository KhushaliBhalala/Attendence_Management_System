/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package ejb;

import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.UserMaster;
import com.mycompany.attendence_system.RoleMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class StudentBean {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<StudentMaster> getAllStudents() {
        try {
            return em.createQuery("SELECT s FROM StudentMaster s", StudentMaster.class).getResultList();
        } catch (Exception e) {
            System.out.println("Error in EJB: " + e.getMessage());
            return null;
        }
    }

    public void addStudent(StudentMaster student, Integer semId, Integer divId) {
        // યુઝર ક્રિએટ કરો
        UserMaster user = new UserMaster();
        user.setUsername(student.getEnrollmentNo());
        user.setPassword("123");
        user.setRoleId(em.find(RoleMaster.class, 3));
        em.persist(user);
        em.flush(); // ID તરત જનરેટ કરવા માટે

        // સ્ટુડન્ટ સેટ કરો
        student.setUserId(user);
        student.setSemesterId(em.find(SemesterMaster.class, semId));
        student.setDivisionId(em.find(DivisionMaster.class, divId));
        student.setCreatedDate(new Date());
        student.setModifiedDate(new Date());

        em.persist(student);
    }

    public void updateStudent(StudentMaster student, Integer semId, Integer divId) {
        StudentMaster s = em.find(StudentMaster.class, student.getId());
        if (s != null) {
            s.setName(student.getName());
            s.setEmail(student.getEmail());
            s.setEnrollmentNo(student.getEnrollmentNo());
            s.setSemesterId(em.find(SemesterMaster.class, semId));
            s.setDivisionId(em.find(DivisionMaster.class, divId));
            s.setModifiedDate(new Date());
            em.merge(s);
        }
    }

    public void deleteStudent(Integer id) {
        StudentMaster s = em.find(StudentMaster.class, id);
        if (s != null) {
            UserMaster u = s.getUserId();
            em.remove(s);
            if (u != null) {
                em.remove(u);
            }
        }
    }
}
