package mg.finance.apiv.security.utilisateur;

import mg.finance.apiv.security.utilisateur.entity.UtilisateurAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurAPIRepo extends JpaRepository<UtilisateurAPI, Long> {
    Optional<UtilisateurAPI> findByUsername(String username);
}
