package service;

import com.mycompany.attendence_system.SemesterMaster;
import ejb.SubjectBean;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import com.mycompany.attendence_system.SubjectMaster;

@Path("subjects")
@RequestScoped
public class SubjectResource {

    @EJB
    private SubjectBean sb;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubjectMaster> getSubjects() {
        return sb.getAllSubjects();
    }

    @GET
    @Path("semesters")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SemesterMaster> getSemesters() {
        return sb.getAllSemesters();
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(SubjectMaster sm) {
        try {
            if (sm == null || sm.getSemesterId() == null) {
                return Response.status(400).entity("{\"error\": \"Invalid JSON data\"}").build();
            }

            sb.addSubject(sm.getSubjectName(), sm.getSubjectCode(), sm.getSemesterId().getId());
            return Response.ok("{\"message\": \"Subject Added Successfully\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}
