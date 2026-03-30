package ejb;

import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
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
    // We select the Subject object linked to this specific Faculty Primary Key
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
        // Ensure the ID exists in DB before linking
        s.setSemesterId(em.find(SemesterMaster.class, s.getSemesterId().getId()));
        s.setDivisionId(em.find(DivisionMaster.class, s.getDivisionId().getId()));
        s.setCreatedDate(new java.util.Date());
        em.persist(s);
    }
}
}