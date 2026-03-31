/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import com.mycompany.attendence_system.DivisionMaster;
import ejb.DivisionBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named(value = "divisionClient")
@RequestScoped
public class DivisionClient {
    @Inject
    DivisionBean db;

    public List<DivisionMaster> getDivisionList() {
        return db.getAllDivisions();
    }
}
