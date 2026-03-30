/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import com.mycompany.attendence_system.*;
import ejb.AdminService;
import ejb.AuthBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.*;

/**
 *
 * @author HP
 */
@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @Inject
    private AuthBean authService;

    @Inject
    private AdminService adminService;
    
    @POST
    @Path("login")
    public Response login(UserMaster user) {
        System.out.println("Login Attempt: User=" + user.getUsername() + ", RoleID=" + user.getRoleId().getId());
        UserMaster authenticated = authService.login(user.getUsername(), user.getPassword(), user.getRoleId().getId());
        if (authenticated != null) {
            return Response.ok(authenticated).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    
    @GET
    @Path("roles")
    public List<RoleMaster> getRoles() {
        return adminService.getAllRoles();
    }
}
