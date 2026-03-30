package service;

import com.mycompany.attendence_system.DivisionMaster;
import com.mycompany.attendence_system.StudentMaster;
import com.mycompany.attendence_system.SubjectMaster;
import ejb.FacultyAsAdmin;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("faculty")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FacultyAsAdminResource {

    @Inject
    private FacultyAsAdmin fb;

    @GET
    @Path("divisions")
    public List<DivisionMaster> getDivisions() {
        return fb.getAllDivisions();
    }

    @GET
    @Path("subjects/{fid}")
    public List<SubjectMaster> getSubjects(@PathParam("fid") int fid) {
        return fb.getFacultySubjects(fid);
    }
    
    

    @GET
    @Path("students/{did}/{sid}")
    public List<StudentMaster> getStudents(@PathParam("did") int did, @PathParam("sid") int sid) {
        return fb.getStudentsByDivision(did, sid);
    }

    @POST
    @Path("import-students")
    public Response importStudents(List<StudentMaster> students) {
        try {
            fb.saveStudents(students);
            return Response.ok("{\"message\":\"Success\"}").build();
        } catch (Exception e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}