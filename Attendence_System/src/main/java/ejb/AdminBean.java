package ejb;

import com.mycompany.attendence_system.RoleMaster;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdminBean {
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    public List<RoleMaster> getAllRoles() {
        return em.createNamedQuery("RoleMaster.findAll", RoleMaster.class).getResultList();
    }
}
