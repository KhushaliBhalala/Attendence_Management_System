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
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.event.FileUploadEvent;
import org.apache.poi.ss.usermodel.*;
import java.util.Date;

@Named(value = "facultyAttendanceClient")
@SessionScoped
public class FacultyAsAdminClient implements Serializable {

    @Inject
    private AuthClient authClient;

    private Date attendanceDate = new Date();
    private int selectedSubject = 0;
    private int selectedDivision = 0;
    private int selectedSemester = 0;
    private boolean selectAll;
    private List<StudentMaster> students = new ArrayList<>();
    private List<DivisionMaster> allDivisions;
    private List<SubjectMaster> allSubjects;
    private List<StudentMaster> filteredStudents;
    private Date currentDate = new Date();
    private List<StudentMaster> selectedStudentsList;
    private final String BASE_URL = "http://localhost:8080/Attendence_System/api/faculty";

    @PostConstruct
    public void init() {
        loadDivisions();
        loadFacultySubjects();
    }
    
    public void toggleSelectAll() {
        if (students != null) {
            for (StudentMaster s : students) {
                s.setPresent(selectAll);
            }
        }
    }

    public void loadDivisions() {
        Client client = ClientBuilder.newClient();
        try {
            this.allDivisions = client.target(BASE_URL + "/divisions")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<DivisionMaster>>() {
                    });
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
                            .get(new GenericType<List<SubjectMaster>>() {
                            });
                } catch (Exception e) {
                    System.err.println("API Error: " + e.getMessage());
                } finally {
                    client.close();
                }
            }
        }
    }

    public void loadStudents() {
        if (selectedDivision == 0 || selectedSubject == 0) {
            this.students = new ArrayList<>();
            return;
        }

        for (SubjectMaster s : allSubjects) {
            if (s.getId() == selectedSubject) {
                this.selectedSemester = s.getSemesterId().getId();
                break;
            }
        }

        Client client = ClientBuilder.newClient();
        try {
            List<StudentMaster> result = client.target(BASE_URL + "/students/" + selectedDivision + "/" + selectedSemester)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<StudentMaster>>() {
                    });

            this.students = (result != null) ? result : new ArrayList<>();
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

            if (!this.students.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Denied", "Students already exist for this division in Database."));
                return;
            }

            Workbook workbook = WorkbookFactory.create(event.getFile().getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            List<StudentMaster> newStudents = new ArrayList<>();
            Integer facultyUserId = authClient.getCurrentUser().getId();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Cell rollCell = row.getCell(0);
                Cell nameCell = row.getCell(1);
                Cell emailCell = row.getCell(2);
                Cell mobileCell = row.getCell(3);

                if (rollCell == null || nameCell == null) {
                    continue;
                }

                StudentMaster s = new StudentMaster();

                String rollStr = (rollCell.getCellType() == CellType.NUMERIC)
                        ? String.valueOf((int) rollCell.getNumericCellValue())
                        : rollCell.getStringCellValue();
                s.setRollNo(rollStr);

                String fullName = nameCell.getStringCellValue();
                s.setName(fullName);

                if (emailCell != null) {
                    s.setEmail(emailCell.getStringCellValue());
                }
                if (mobileCell != null) {
                    String mob = (mobileCell.getCellType() == CellType.NUMERIC)
                            ? String.valueOf((long) mobileCell.getNumericCellValue())
                            : mobileCell.getStringCellValue();
                    s.setMobileNo(mob);
                }

                SemesterMaster sem = new SemesterMaster();
                sem.setId(selectedSemester);
                DivisionMaster div = new DivisionMaster();
                div.setId(selectedDivision);
                UserMaster creator = new UserMaster();
                creator.setId(facultyUserId);

                s.setSemesterId(sem);
                s.setDivisionId(div);
                s.setCreatedBy(creator);

                newStudents.add(s);
            }

            saveToDatabase(newStudents);
            loadStudents();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Import Success", "Students Imported and Loaded."));

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
                String errorMsg = response.readEntity(String.class);
                System.err.println("API Error: " + errorMsg);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Database Error", errorMsg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    public void submitAttendance() {
        List<StudentMaster> presentStudents = new ArrayList<>();
        for (StudentMaster s : students) {
            if (s.isPresent()) {
                presentStudents.add(s);
            }
        }

        if (presentStudents.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No Selection", "Please mark at least one student present."));
            return;
        }
        // Logic to save attendance records...
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    
    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }
    
    public List<StudentMaster> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(List<StudentMaster> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }
    
    // Getters and Setters
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
