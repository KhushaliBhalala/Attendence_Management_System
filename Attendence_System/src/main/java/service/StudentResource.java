/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.attendence_system.rest;


import com.mycompany.attendence_system.StudentMaster;
import ejb.StudentBean;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("student")
public class StudentResource {

    @Inject
    StudentBean sb;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StudentMaster> getAll() {
        return sb.getAllStudents();
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(StudentMaster s, @QueryParam("semId") Integer semId, @QueryParam("divId") Integer divId) {
        sb.addStudent(s, semId, divId);
        return Response.ok().build();
    }

    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(StudentMaster s, @QueryParam("semId") Integer semId, @QueryParam("divId") Integer divId) {
        sb.updateStudent(s, semId, divId);
        return Response.ok().build();
    }

    @DELETE
    @Path("delete/{id}")
    public Response delete(@PathParam("id") Integer id) {
        sb.deleteStudent(id);
        return Response.ok().build();
    }
}