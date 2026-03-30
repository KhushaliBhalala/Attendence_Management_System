package client;

import com.mycompany.attendence_system.SubjectMaster;
import com.mycompany.attendence_system.SemesterMaster;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Named(value = "subjectClient")
@SessionScoped
public class SubjectClient implements Serializable {

    private String subjectName;
    private String subjectCode;
    private Integer semesterId;
    private List<SubjectMaster> subjectList;

    private final String BASE_URL = "http://localhost:8081/Attendence_System/api/subjects";
    private List<SemesterMaster> allSemesters;

    public List<SubjectMaster> getSubjectList() {
        // Force refresh if list is null
        if (subjectList == null) {
            Client client = ClientBuilder.newClient();
            try {
                subjectList = client.target(BASE_URL)
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<SubjectMaster>>() {
                        });
            } catch (Exception e) {
                System.out.println("Fetch Error: " + e.getMessage());
            } finally {
                client.close();
            }
        }
        return subjectList;
    }
    
    public List<SemesterMaster> getAllSemesters() {
    if (allSemesters == null) {
        Client client = ClientBuilder.newClient();
        try {
            allSemesters = client.target("http://localhost:8080/Attendence_System/api/subjects/semesters")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<SemesterMaster>>() {});
        } catch (Exception e) {
            System.out.println("Semester Load Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }
    return allSemesters;
}

    public String addSubject() {
        Client client = ClientBuilder.newClient();
        SubjectMaster newSubject = new SubjectMaster();
        newSubject.setSubjectName(subjectName);
        newSubject.setSubjectCode(subjectCode);

        SemesterMaster sem = new SemesterMaster();
        sem.setId(semesterId);
        newSubject.setSemesterId(sem);

        try {
            Response res = client.target(BASE_URL + "/add")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newSubject, MediaType.APPLICATION_JSON));

            if (res.getStatus() == 200 || res.getStatus() == 204) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Subject Added!"));

                // RESET STATE
                this.subjectName = "";
                this.subjectCode = "";
                this.semesterId = null;
                this.subjectList = null; // CRITICAL: Forces re-fetch on next GET

                return null; // Stay on page for AJAX update
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        } finally {
            client.close();
        }
        return null;
    }

    // Getters and Setters...
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }
}
