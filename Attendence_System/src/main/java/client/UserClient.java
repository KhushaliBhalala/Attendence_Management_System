/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import DTO.ApiResponse;
import com.mycompany.attendence_system.UserMaster;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Named
@RequestScoped
public class UserClient {

    private String username;
    private String password;
    private int createdBy;
    private int modifiedBy;
    private String message;

    public void saveUser() {

        Client client = ClientBuilder.newClient();

        String url = "http://localhost:8081/Attendence_System/api/users";

        UserMaster u = new UserMaster();
        u.setUsername(username);
        u.setPassword(password);
        u.setCreatedBy(createdBy);
        u.setModifiedBy(modifiedBy);

        try (Response res = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(u, MediaType.APPLICATION_JSON))) {

            if (res.getStatus() == 201) {
                ApiResponse response = res.readEntity(ApiResponse.class);
                message = response.getMessage();
            } else {
                message = "Error: " + res.getStatus();
            }
        } catch (Exception e) {
            message = "Connection failed: " + e.getMessage();
        }
    }

    // getters setters
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getMessage() {
        return message;
    }
}
