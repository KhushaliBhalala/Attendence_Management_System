/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package client;

import com.mycompany.attendence_system.RoleMaster;
import com.mycompany.attendence_system.UserMaster;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import jakarta.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:AuthResource [auth]<br>
 * USAGE:
 * <pre>
 *        AuthClient client = new AuthClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author HP
 */
@Named(value = "AuthClient")
@SessionScoped
public class AuthClient implements Serializable {

    private String username;
    private String password;
    private int roleId;
    private String message;
    private List<RoleMaster> roles;

    private final String BASE_URL = "http://localhost:8080/Attendence_System/api/auth";

    public List<RoleMaster> getRoles() {
        if (roles == null) {
            Client client = ClientBuilder.newClient();
            try {
                roles = client.target(BASE_URL + "/roles")
                        .request(MediaType.APPLICATION_JSON)
                        .get(new GenericType<List<RoleMaster>>() {
                        });
            } catch (Exception e) {
                System.out.println("Error fetching roles: " + e.getMessage());
            } finally {
                client.close();
            }
        }
        return roles;
    }

    public String doLogin() {
        Client client = ClientBuilder.newClient();

        UserMaster userReq = new UserMaster();
        userReq.setUsername(username);
        userReq.setPassword(password);

        RoleMaster rm = new RoleMaster();
        rm.setId(roleId);
        userReq.setRoleId(rm);
        
        System.out.println("UI Role ID : " + roleId);

        try {
            Response res = client.target(BASE_URL + "/login")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(userReq, MediaType.APPLICATION_JSON));

            if (res.getStatus() == 200) {
                UserMaster authenticatedUser = res.readEntity(UserMaster.class);
                System.out.println("Authenticated User: " + authenticatedUser.getUsername());

                // Ensure the nested roleId object is not null
                if (authenticatedUser.getRoleId() != null) {
                    int userRole = authenticatedUser.getRoleId().getId();
                    System.out.println("User Role ID: " + userRole);

                    if (userRole == 1) {
                        return "admin/admin_dashboard?faces-redirect=true";
                    }
                    if (userRole == 2) {
                        return "faculty_dashboard?faces-redirect=true";
                    }
                    return "student_dashboard?faces-redirect=true";
                }
                return null;
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Credentials", "Please check username or password."));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Connection Error", e.getMessage()));
            return null;
        } finally {
            client.close();
        }
    }

    // --- Getters and Setters ---
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setRoles(List<RoleMaster> roles) {
        this.roles = roles;
    }
}
