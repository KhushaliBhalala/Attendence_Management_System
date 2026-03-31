package ejb;

import com.mycompany.attendence_system.*;
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
        return em.createQuery("SELECT d FROM DivisionMaster d", DivisionMaster.class).getResultList();
    }

    public List<SubjectMaster> getFacultySubjects(int facultyId) {
        return em.createQuery("SELECT f.subjectId FROM FacultyMaster f WHERE f.id = :fid", SubjectMaster.class)
                .setParameter("fid", facultyId)
                .getResultList();
    }

    public List<StudentMaster> getStudentsByDivision(int divisionId, int semesterId) {
        return em.createQuery("SELECT s FROM StudentMaster s WHERE s.divisionId.id = :did AND s.semesterId.id = :sid", StudentMaster.class)
                .setParameter("did", divisionId)
                .setParameter("sid", semesterId)
                .getResultList();
    }

    public void saveStudents(List<StudentMaster> students) {
        for (StudentMaster s : students) {
            // 1. Logic for Name before space (First Name)
            String firstName = "User"; // Fallback default
            if (s.getName() != null && !s.getName().trim().isEmpty()) {
                // Split by space and take the first index
                firstName = s.getName().trim().split("\\s+")[0];
            }

            // 2. Create and Persist User (UserMaster Table)
            UserMaster studentUser = new UserMaster();
            studentUser.setUsername(firstName);
            studentUser.setPassword(firstName);

            // Ensure Role ID 3 (Student) exists
            RoleMaster studentRole = em.find(RoleMaster.class, 3);
            if (studentRole == null) {
                throw new RuntimeException("Role ID 3 not found in database.");
            }
            studentUser.setRoleId(studentRole);
            studentUser.setCreatedAt(new java.util.Date());
           
            em.persist(studentUser);
            em.flush(); // Generates the ID for studentUser

            // 3. Map IDs and set Student Password (StudentMaster Table)
            s.setUserId(studentUser);

            // SETTING PASSWORD IN STUDENT TABLE AS WELL
            s.setPassword(firstName);

            // Ensure foreign keys are managed entities to avoid "detached entity" errors
            s.setSemesterId(em.find(SemesterMaster.class, s.getSemesterId().getId()));
            s.setDivisionId(em.find(DivisionMaster.class, s.getDivisionId().getId()));
//            s.setCreatedBy(em.find(UserMaster.class, s.getCreatedBy().getId()));
            if (s.getCreatedBy() != null) {
                UserMaster facultyUser = em.find(UserMaster.class, s.getCreatedBy().getId());
                s.setCreatedBy(facultyUser);
                studentUser.setCreatedBy(facultyUser.getId());
            }
            // These fields are already populated from the Excel parsing in the Client bean
            // s.setEmail(s.getEmail());
            // s.setMobileNo(s.getMobileNo());
            em.persist(s);
        }
    }
}
