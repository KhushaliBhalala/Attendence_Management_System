/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DTO.ApiResponse;
import com.mycompany.attendence_system.UserMaster;
import ejb.UserBean;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author HP
 */
@Stateless
@Path("users")
public class UserMasterFacadeREST extends AbstractFacade<UserMaster> {

    @EJB
    private UserBean user;

    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    @GET
    public List<UserMaster> getUsers() {
        return user.getAllUsers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(UserMaster u) {
        try {
            user.addUser(u);

            ApiResponse res = new ApiResponse("success", "User inserted successfully");

            return Response.status(Response.Status.CREATED)
                    .entity(res)
                    .build();

        } catch (Exception e) {

            ApiResponse res = new ApiResponse("error", "Failed to insert user");

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(res)
                    .build();
        }
    }

    public UserMasterFacadeREST() {
        super(UserMaster.class);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserMaster> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
