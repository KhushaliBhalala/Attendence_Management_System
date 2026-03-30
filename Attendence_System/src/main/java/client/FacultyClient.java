/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.mycompany.attendence_system.FacultyMaster;
import com.mycompany.attendence_system.SubjectMaster;
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

/**
 *
 * @author HP
 */
@Named(value = "facultyClient")
@SessionScoped
public class FacultyClient implements Serializable {

    private FacultyMaster faculty = new FacultyMaster();
    private String username;
    private String password;
    private Integer selectedSubjectId;
    private List<FacultyMaster> facultyList;

    private final String BASE_URL = "http://localhost:8080/Attendence_System/api/faculty";

    public List<FacultyMaster> getFacultyList() {
        if (facultyList == null) {
            Client client = ClientBuilder.newClient();
            try {
                facultyList = client.target(BASE_URL)
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<FacultyMaster>>() {
                        });
            } finally {
                client.close();
            }
        }
        return facultyList;
    }

    public String save() {
        if (this.faculty == null || this.faculty.getId() == null) {
            return addFaculty();
        } else {
            return updateFaculty();
        }
    }

    public String addFaculty() {
        Client client = ClientBuilder.newClient();
        try {
            Response res = client.target(BASE_URL + "/add")
                    .queryParam("uname", username)
                    .queryParam("pwd", password)
                    .queryParam("subId", selectedSubjectId)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(faculty, MediaType.APPLICATION_JSON));

            if (res.getStatus() == 200 || res.getStatus() == 204) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Faculty Added"));

                resetForm();
                return null;
            }
        } finally {
            client.close();
        }
        return null;
    }

    public void resetForm() {
        this.faculty = new FacultyMaster();
        this.username = "";
        this.password = "";
        this.selectedSubjectId = null;
        this.facultyList = null; 
    }

    public FacultyMaster getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyMaster faculty) {
        this.faculty = faculty;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSelectedSubjectId() {
        return selectedSubjectId;
    }

    public void setSelectedSubjectId(Integer selectedSubjectId) {
        this.selectedSubjectId = selectedSubjectId;
    }

// Edit load the dialog box
    public void prepareEdit(FacultyMaster f) {
        this.faculty = f;
        this.username = (f.getUserId() != null) ? f.getUserId().getUsername() : "";
        this.password = f.getPassword(); // અથવા user_master માંથી લો
        this.selectedSubjectId = (f.getSubjectId() != null) ? f.getSubjectId().getId() : null;
    }

// Update 
    public String updateFaculty() {
        Client client = ClientBuilder.newClient();
        try {
            Response res = client.target(BASE_URL + "/update")
                    .queryParam("uname", username)
                    .queryParam("pwd", password)
                    .queryParam("subId", selectedSubjectId)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(faculty, MediaType.APPLICATION_JSON));

            if (res.getStatus() == 200) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Faculty Updated"));
                this.facultyList = null; // List રિફ્રેશ કરવા
            }
        } finally {
            client.close();
        }
        return null;
    }

// Delete 
    public void delete(Integer id) {
        Client client = ClientBuilder.newClient();
        try {
            Response res = client.target(BASE_URL + "/delete/" + id).request().delete();
            if (res.getStatus() == 200) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Faculty Deleted"));
                this.facultyList = null;
            }
        } finally {
            client.close();
        }
    }
}
