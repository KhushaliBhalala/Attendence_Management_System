package ejb;

import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.RoleMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
@LocalBean
public class FacultyAsAdmin {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<DivisionMaster> getAllDivisions() {
        return em.createNamedQuery("DivisionMaster.findAll", DivisionMaster.class).getResultList();
    }

    public List<SubjectMaster> getFacultySubjects(int facultyId) {
        return em.createQuery("SELECT f.subjectId FROM FacultyMaster f WHERE f.id = :fid", SubjectMaster.class)
                .setParameter("fid", facultyId).getResultList();
    }

    public List<StudentMaster> getStudentsByDivision(int divisionId, int semesterId) {
        return em.createQuery("SELECT s FROM StudentMaster s WHERE s.divisionId.id = :did AND s.semesterId.id = :sid", StudentMaster.class)
                .setParameter("did", divisionId)
                .setParameter("sid", semesterId).getResultList();
    }

    public void saveStudents(List<StudentMaster> students) {
    for (StudentMaster s : students) {
        try {
            String fullName = s.getName().trim();
            String firstName = fullName.contains(" ") ? fullName.split("\\s+")[0] : fullName;
            
            // ૧. યુનિક યુઝરનેમ બનાવો (FirstName + RollNo)
            String uniqueUsername = firstName.toLowerCase() + "_" + s.getRollNo();

            UserMaster studentUser = new UserMaster();
            studentUser.setUsername(uniqueUsername);
            studentUser.setPassword(uniqueUsername); // પાસવર્ડ પણ યુનિક રાખો શરૂઆતમાં

            RoleMaster studentRole = em.find(RoleMaster.class, 3);
            if (studentRole == null) throw new RuntimeException("Role ID 3 not found.");
            
            studentUser.setRoleId(studentRole);
            studentUser.setCreatedAt(new java.util.Date());

            // Faculty ID સેટ કરો
            if (s.getCreatedBy() != null) {
                studentUser.setCreatedBy(s.getCreatedBy().getId());
            }

            em.persist(studentUser);
            em.flush(); // આ લાઇન ID જનરેટ કરવા માટે જરૂરી છે

            // ૨. Student record સેટ કરો
            s.setUserId(studentUser);
            s.setPassword(uniqueUsername);
            
            // Managed Entities શોધો
            s.setSemesterId(em.find(SemesterMaster.class, s.getSemesterId().getId()));
            s.setDivisionId(em.find(DivisionMaster.class, s.getDivisionId().getId()));
            
            if (s.getCreatedBy() != null) {
                s.setCreatedBy(em.find(UserMaster.class, s.getCreatedBy().getId()));
            }
            
            s.setCreatedDate(new java.util.Date());
            s.setModifiedDate(new java.util.Date());

            em.persist(s);
            System.out.println("Inserted: " + uniqueUsername);

        } catch (Exception e) {
            System.err.println("Error saving student " + s.getName() + ": " + e.getMessage());
            throw e; 
        }
    }
}
}
