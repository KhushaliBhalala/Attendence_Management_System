package com.mycompany.attendence_system.rest;

import com.mycompany.attendence_system.FacultyMaster;
import com.mycompany.attendence_system.SubjectMaster;
import ejb.FacultyBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("faculty") // Matches the BASE_URL in your client
public class FacultyResource {

    @Inject
    FacultyBean facultyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FacultyMaster> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @POST
    @Path("add") // Matches BASE_URL + "/add" in your client
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFaculty(
            FacultyMaster faculty,
            @QueryParam("uname") String username,
            @QueryParam("pwd") String password,
            @QueryParam("subId") Integer subjectId) {

        try {
            facultyService.addFaculty(faculty, username, password, subjectId);
            return Response.ok().build(); // Status 200
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    // FacultyResource.java 
    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFaculty(
            FacultyMaster faculty,
            @QueryParam("uname") String username,
            @QueryParam("pwd") String password,
            @QueryParam("subId") Integer subjectId) {
        try {
            facultyService.updateFaculty(faculty, username, password, subjectId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("delete/{id}")
    public Response deleteFaculty(@PathParam("id") Integer id) {
        try {
            facultyService.deleteFaculty(id);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}
