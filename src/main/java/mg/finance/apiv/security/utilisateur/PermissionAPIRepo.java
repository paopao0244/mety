package mg.finance.apiv.security.utilisateur;

import mg.finance.apiv.security.utilisateur.entity.PermissionAPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionAPIRepo extends JpaRepository<PermissionAPI, Long> {
    PermissionAPI findByNom(String nom);
}
