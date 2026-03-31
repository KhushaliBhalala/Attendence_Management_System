/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.mycompany.attendence_system.SemesterMaster;
import ejb.SemesterBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named(value = "semesterClient")
@RequestScoped
public class SemesterClient {
    @Inject
    SemesterBean sb;

    public List<SemesterMaster> getSemesterList() {
        return sb.getAllSemesters();
    }
}
