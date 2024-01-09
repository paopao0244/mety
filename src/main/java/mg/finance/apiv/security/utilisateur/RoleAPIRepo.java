package mg.finance.apiv.security.utilisateur;

import mg.finance.apiv.security.utilisateur.entity.RoleAPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAPIRepo extends JpaRepository<RoleAPI, Long> {
    RoleAPI findByNom(String nom);
}
