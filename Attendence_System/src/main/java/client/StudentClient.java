/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.mycompany.attendence_system.StudentMaster;
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

@Named(value = "studentClient")
@SessionScoped
public class StudentClient implements Serializable {

    private StudentMaster student = new StudentMaster();
    private List<StudentMaster> studentList;
    private Integer selectedSemId;
    private Integer selectedDivId;

    private final String BASE_URL = "http://localhost:8080/Attendence_System/api/student";

    public void resetForm() {
        this.student = new StudentMaster();
        this.selectedSemId = null;
        this.selectedDivId = null;
    }

   public List<StudentMaster> getStudentList() {
    Client client = ClientBuilder.newClient();
    try {
        studentList = client.target(BASE_URL)
                            .request(MediaType.APPLICATION_JSON)
                            .get(new GenericType<List<StudentMaster>>() {});
        return studentList;
    } catch (Exception e) {
        System.out.println("REST Call Failed: " + e.getMessage()); // આનાથી NetBeans ના Output માં એરર દેખાશે
        return null;
    } finally {
        client.close();
    }
}

    public void prepareEdit(StudentMaster s) {
        this.student = s;
        this.selectedSemId = (s.getSemesterId() != null) ? s.getSemesterId().getId() : null;
        this.selectedDivId = (s.getDivisionId() != null) ? s.getDivisionId().getId() : null;
    }

    public String save() {
        Client client = ClientBuilder.newClient();
        Response res;
        if (student.getId() == null) {
            res = client.target(BASE_URL + "/add")
                    .queryParam("semId", selectedSemId)
                    .queryParam("divId", selectedDivId)
                    .request().post(Entity.entity(student, MediaType.APPLICATION_JSON));
        } else {
            res = client.target(BASE_URL + "/update")
                    .queryParam("semId", selectedSemId)
                    .queryParam("divId", selectedDivId)
                    .request().put(Entity.entity(student, MediaType.APPLICATION_JSON));
        }

        if (res.getStatus() == 200 || res.getStatus() == 204) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student Saved Successfully"));
            resetForm();
            this.studentList = null; // લિસ્ટ રિફ્રેશ કરવા માટે
        }
        client.close();
        return null;
    }

    public void delete(Integer id) {
        Client client = ClientBuilder.newClient();
        client.target(BASE_URL + "/delete/" + id).request().delete();
        client.close();
    }

    // Getters and Setters
    public StudentMaster getStudent() {
        return student;
    }

    public void setStudent(StudentMaster student) {
        this.student = student;
    }

    public Integer getSelectedSemId() {
        return selectedSemId;
    }

    public void setSelectedSemId(Integer selectedSemId) {
        this.selectedSemId = selectedSemId;
    }

    public Integer getSelectedDivId() {
        return selectedDivId;
    }

    public void setSelectedDivId(Integer selectedDivId) {
        this.selectedDivId = selectedDivId;
    }
}
