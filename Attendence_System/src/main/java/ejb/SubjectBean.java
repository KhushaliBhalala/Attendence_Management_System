package ejb;

import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.RoleMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

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

public void addSubject(String name, String code, Integer semesterId) { 
    SubjectMaster sm = new SubjectMaster();
    sm.setSubjectName(name);
    sm.setSubjectCode(code);
    
    SemesterMaster sem = em.find(SemesterMaster.class, semesterId);
    if (sem != null) {
        sm.setSemesterId(sem);
        sm.setCreatedDate(new Date());
        em.persist(sm);
    }
}
    public void updateSubject(int id, String name, String code, int semesterId) {
        SubjectMaster sm = em.find(SubjectMaster.class, id);
        if (sm != null) {
            sm.setSubjectName(name);
            sm.setSubjectCode(code);
            SemesterMaster sem = em.find(SemesterMaster.class, semesterId);
            sm.setSemesterId(sem);
            em.merge(sm);
        }
    }

 public void deleteSubject(int id) {
    try {
        SubjectMaster sm = em.find(SubjectMaster.class, id);
        if (sm != null) {
            
            em.remove(em.merge(sm));
            em.flush(); 
        }
    } catch (Exception e) {
        throw new RuntimeException("not delete bez use in another");
    }
}
}
