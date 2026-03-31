/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import com.mycompany.attendence_system.*;
import ejb.AdminService;
import ejb.AuthBean;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;
    @Inject
    private AuthBean authService;

    @Inject
    private AdminService adminService;

    @POST
    @Path("login")
    public Response doLogin(UserMaster userReq) {
        try {
            UserMaster u = em.createNamedQuery("UserMaster.validate", UserMaster.class)
                    .setParameter("uname", userReq.getUsername())
                    .setParameter("pwd", userReq.getPassword())
                    .setParameter("rid", userReq.getRoleId().getId())
                    .getSingleResult();

            if (u != null) {
                // If the user is a Faculty (Role ID 2)
                if (u.getRoleId().getId() == 2) {
                    try {
                        // Fetch the PRIMARY KEY (id) from faculty_master where user_id matches
                        Integer fid = em.createQuery("SELECT f.id FROM FacultyMaster f WHERE f.userId.id = :uid", Integer.class)
                                .setParameter("uid", u.getId())
                                .getSingleResult();
                        u.setFacultyIdForSession(fid);
                    } catch (Exception e) {
                        System.err.println("Faculty record not found for user: " + u.getUsername());
                    }
                }
                return Response.ok(u).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("roles")
    public List<RoleMaster> getRoles() {
        return adminService.getAllRoles();
    }
}
