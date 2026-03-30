package client;

import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.SemesterMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.FileUploadEvent;
import org.apache.poi.ss.usermodel.*;

@Named(value = "facultyAttendanceClient")
@SessionScoped
public class FacultyAsAdminClient implements Serializable {

    @Inject
    private AuthClient authClient;

    private int selectedSubject;
    private int selectedDivision;
    private int selectedSemester;
    private List<StudentMaster> students = new ArrayList<>();
    private List<DivisionMaster> allDivisions;
    private List<SubjectMaster> allSubjects;

    private final String BASE_URL = "http://localhost:8080/Attendence_System/api/faculty";

    @PostConstruct
    public void init() {
        loadDropdownData();
    }

  public void loadDropdownData() {
    Client client = ClientBuilder.newClient();
    try {
        // Load Divisions
        this.allDivisions = client.target(BASE_URL + "/divisions").request().get(new GenericType<List<DivisionMaster>>(){});

        // Load dynamic Faculty ID
        Integer loggedInFacultyId = authClient.getCurrentUser().getFacultyId();
        
        if (loggedInFacultyId != null) {
            this.allSubjects = client.target(BASE_URL + "/subjects/" + loggedInFacultyId)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<SubjectMaster>>() {});
        } else {
            System.err.println("Error: Current user is not linked to any Faculty record.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        client.close();
    }
}
    public void loadStudents() {
        if (selectedDivision == 0 || selectedSubject == 0) {
            return;
        }

        // Find the semester associated with the selected subject from loaded list
        for (SubjectMaster s : allSubjects) {
            if (s.getId() == selectedSubject) {
                this.selectedSemester = s.getSemesterId().getId();
                break;
            }
        }

        Client client = ClientBuilder.newClient();
        try {
            this.students = client.target(BASE_URL + "/students/" + selectedDivision + "/" + selectedSemester)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<StudentMaster>>() {
                    });

            if (students.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No students found. Please import Excel file."));
            }
        } catch (Exception e) {
            this.students = new ArrayList<>();
        } finally {
            client.close();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            Workbook workbook = WorkbookFactory.create(event.getFile().getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<StudentMaster> newStudents = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                StudentMaster s = new StudentMaster();
                s.setName(row.getCell(0).getStringCellValue());
                s.setEnrollmentNo(row.getCell(1).getStringCellValue());

                SemesterMaster sem = new SemesterMaster();
                sem.setId(selectedSemester);
                DivisionMaster div = new DivisionMaster();
                div.setId(selectedDivision);
                s.setSemesterId(sem);
                s.setDivisionId(div);

                newStudents.add(s);
            }
            saveToDatabase(newStudents);
            loadStudents();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Import Failed"));
        }
    }

    private void saveToDatabase(List<StudentMaster> list) {
        Client client = ClientBuilder.newClient();
        try {
            client.target(BASE_URL + "/import-students")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(list, MediaType.APPLICATION_JSON));
        } finally {
            client.close();
        }
    }

    // Standard Getters and Setters
    public List<DivisionMaster> getAllDivisions() {
        return allDivisions;
    }

    public List<SubjectMaster> getAllSubjects() {
        return allSubjects;
    }

    public int getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(int selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

    public int getSelectedDivision() {
        return selectedDivision;
    }

    public void setSelectedDivision(int selectedDivision) {
        this.selectedDivision = selectedDivision;
    }

    public List<StudentMaster> getStudents() {
        return students;
    }
}
