package ejb;

import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.SubjectMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


//hello
@Stateless
public class SubjectBean {

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<SubjectMaster> getAllSubjects() {
        return em.createNamedQuery("SubjectMaster.findAll", SubjectMaster.class).getResultList();
    }

    public List<SemesterMaster> getAllSemesters() {
        return em.createNamedQuery("SemesterMaster.findAll", SemesterMaster.class).getResultList();
    }

    public void addSubject(String name, String code, int semesterId) {
        SubjectMaster sm = new SubjectMaster();
        sm.setSubjectName(name);
        sm.setSubjectCode(code);
        // Find the actual Semester object from DB
        SemesterMaster sem = em.find(SemesterMaster.class, semesterId);
        if (sem != null) {
            sm.setSemesterId(sem);
            sm.setCreatedDate(new Date());
            em.persist(sm);
        } else {
            throw new RuntimeException("Semester not found with ID: " + semesterId);
        }
    }
}
