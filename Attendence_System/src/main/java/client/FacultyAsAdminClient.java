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
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
        loadDivisions();
        loadFacultySubjects();
    }

    public void loadDivisions() {
        Client client = ClientBuilder.newClient();
        try {
            this.allDivisions = client.target(BASE_URL + "/divisions")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<DivisionMaster>>() {});
        } catch (Exception e) {
            System.err.println("Division Load Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }

    public void loadFacultySubjects() {
        if (authClient.getCurrentUser() != null) {
            Integer fid = authClient.getCurrentUser().getFacultyIdForSession();
            if (fid != null && fid > 0) {
                Client client = ClientBuilder.newClient();
                try {
                    this.allSubjects = client.target(BASE_URL + "/subjects/" + fid)
                            .request(MediaType.APPLICATION_JSON)
                            .get(new GenericType<List<SubjectMaster>>() {});
                } catch (Exception e) {
                    System.err.println("API Error: " + e.getMessage());
                } finally {
                    client.close();
                }
            }
        }
    }

    public void loadStudents() {
        if (selectedDivision == 0 || selectedSubject == 0) return;

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
                    .get(new GenericType<List<StudentMaster>>() {});
        } catch (Exception e) {
            this.students = new ArrayList<>();
        } finally {
            client.close();
        }
    }

  public void handleFileUpload(FileUploadEvent event) {
    try {
        if (selectedDivision == 0 || selectedSubject == 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Required", "Select Subject and Division first."));
            return;
        }

        for (SubjectMaster s : allSubjects) {
            if (s.getId() == selectedSubject) {
                this.selectedSemester = s.getSemesterId().getId();
                break;
            }
        }

        Workbook workbook = WorkbookFactory.create(event.getFile().getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        List<StudentMaster> newStudents = new ArrayList<>();
        Integer facultyUserId = authClient.getCurrentUser().getId();

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header

            Cell rollCell = row.getCell(0);
            Cell nameCell = row.getCell(1);
            Cell emailCell = row.getCell(2);
            Cell mobileCell = row.getCell(3);
            
            if (rollCell == null || nameCell == null) continue;

            StudentMaster s = new StudentMaster();
            
            // 1. Roll No
            String rollStr = (rollCell.getCellType() == CellType.NUMERIC)
                    ? String.valueOf((int) rollCell.getNumericCellValue())
                    : rollCell.getStringCellValue();
            s.setRollNo(rollStr);

            // 2. Name
            String fullPathName = nameCell.getStringCellValue();
            s.setName(fullPathName);

            // 3. Email & Mobile (Fixing the NULL issue)
            if (emailCell != null) s.setEmail(emailCell.getStringCellValue());
            if (mobileCell != null) {
                String mob = (mobileCell.getCellType() == CellType.NUMERIC)
                    ? String.valueOf((long) mobileCell.getNumericCellValue())
                    : mobileCell.getStringCellValue();
                s.setMobileNo(mob);
            }

            // 4. Logic for Username/Password (Name before space)
            String firstName = fullPathName.split(" ")[0].trim();
            
            // We use a temporary field or handle this in EJB. 
            // For now, let's pass the 'firstName' logic via a field if your StudentMaster has one, 
            // otherwise the EJB will handle it.
            
            SemesterMaster sem = new SemesterMaster();
            sem.setId(selectedSemester);
            DivisionMaster div = new DivisionMaster();
            div.setId(selectedDivision);
            UserMaster creator = new UserMaster();
            creator.setId(facultyUserId);

            s.setSemesterId(sem);
            s.setDivisionId(div);
            s.setCreatedBy(creator);
            s.setCreatedDate(new java.util.Date());
            s.setModifiedDate(new java.util.Date());

            newStudents.add(s);
        }

        saveToDatabase(newStudents);
        loadStudents();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Import Success", "Students Imported."));

    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Import Error", e.getMessage()));
    }
}

    private void saveToDatabase(List<StudentMaster> list) {
        Client client = ClientBuilder.newClient();
        try {
            Response response = client.target(BASE_URL).path("import-students")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(list, MediaType.APPLICATION_JSON));

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                System.err.println("Error saving: " + response.readEntity(String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    // Getters and Setters
    public List<DivisionMaster> getAllDivisions() { return allDivisions; }
    public List<SubjectMaster> getAllSubjects() { return allSubjects; }
    public int getSelectedSubject() { return selectedSubject; }
    public void setSelectedSubject(int selectedSubject) { this.selectedSubject = selectedSubject; }
    public int getSelectedDivision() { return selectedDivision; }
    public void setSelectedDivision(int selectedDivision) { this.selectedDivision = selectedDivision; }
    public List<StudentMaster> getStudents() { return students; }
}