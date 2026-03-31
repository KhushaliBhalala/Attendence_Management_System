package ejb;

import com.mycompany.attendence_system.AttendanceMaster;
import com.mycompany.attendence_system.AttendenceReport;
import com.mycompany.attendence_system.ClassMaster;
import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.FacultyMaster;
import com.mycompany.attendence_system.RoleMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
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

    
  public void saveAttendance(int facultyId, int subjectId, List<StudentMaster> studentList) {
    Date today = new Date();
    
    // 1. Fetch ClassMaster (Fakat total lectures janva mate, badlav karva mate nahi)
    ClassMaster cm;
    try {
        cm = em.createQuery("SELECT c FROM ClassMaster c WHERE c.facultyId.id = :fid AND c.subjectId.id = :sid", ClassMaster.class)
                .setParameter("fid", facultyId)
                .setParameter("sid", subjectId)
                .getSingleResult();
    } catch (Exception e) {
        System.err.println("ClassMaster record not found: " + e.getMessage());
        return;
    }

    int classTotalLectures = cm.getTotalLectures(); // Aa fix value rehse (e.g., 40)
    FacultyMaster faculty = em.find(FacultyMaster.class, facultyId);
    SubjectMaster subject = em.find(SubjectMaster.class, subjectId);

    for (StudentMaster s : studentList) {
        // 2. AttendanceMaster (Har vakhte entry padse j)
        AttendanceMaster am = new AttendanceMaster();
        am.setAttendanceDate(today);
        am.setFacultyId(faculty);
        am.setSubjectId(subject);
        am.setStudentId(em.find(StudentMaster.class, s.getId()));
        am.setStatus(s.isPresent() ? 'P' : 'A');
        am.setCreatedBy(facultyId); 
        am.setCreatedDate(today);
        am.setModifiedDate(today);
        em.persist(am);
        em.flush(); 

        // 3. AttendenceReport logic
        try {
            List<AttendenceReport> existingReports = em.createQuery(
                "SELECT r FROM AttendenceReport r WHERE r.classId.id = :cid AND r.attendenceId.studentId.id = :sid", AttendenceReport.class)
                .setParameter("cid", cm.getId())
                .setParameter("sid", s.getId())
                .getResultList();

            if (!existingReports.isEmpty()) {
                // --- UPDATE ---
                AttendenceReport report = existingReports.get(0);
                if (s.isPresent()) {
                    report.setTotalPresent(report.getTotalPresent() + 1);
                    report.setTotalAbsent(report.getTotalAbsent() - 1); 
                }
                em.merge(report);
            } else {
                // --- INSERT ---
                AttendenceReport report = new AttendenceReport();
                report.setClassId(cm);
                report.setAttendenceId(am);
                
                if (s.isPresent()) {
                    report.setTotalPresent(1);
                    report.setTotalAbsent(classTotalLectures - 1);
                } else {
                    report.setTotalPresent(0);
                    report.setTotalAbsent(classTotalLectures);     
                }
                em.persist(report);
            }
        } catch (Exception e) {
            System.err.println("Error for student " + s.getId() + ": " + e.getMessage());
        }
    }
}
public List<Object[]> getAttendanceReport(int divisionId, int semesterId, int subjectId) {
    // This query calculates: Student Name, Total Present, Total Lectures, and Percentage
    return em.createQuery(
        "SELECT s.name, " +
        "SUM(CASE WHEN a.status = 'P' THEN 1 ELSE 0 END), " +
        "c.totalLectures, " +
        "(CAST(SUM(CASE WHEN a.status = 'P' THEN 1 ELSE 0 END) AS float) / c.totalLectures * 100) " +
        "FROM AttendanceMaster a JOIN a.studentId s, ClassMaster c " +
        "WHERE s.divisionId.id = :did AND s.semesterId.id = :sid AND a.subjectId.id = :subid " +
        "AND c.divisionId.id = :did AND c.semesterId.id = :sid AND c.subjectId = :subid " +
        "GROUP BY s.name, c.totalLectures", Object[].class)
        .setParameter("did", divisionId)
        .setParameter("sid", semesterId)
        .setParameter("subid", subjectId)
        .getResultList();
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
