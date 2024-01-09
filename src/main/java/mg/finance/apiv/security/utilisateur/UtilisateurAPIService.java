package mg.finance.apiv.security.utilisateur;

import mg.finance.apiv.security.utilisateur.entity.PermissionAPI;
import mg.finance.apiv.security.utilisateur.entity.RoleAPI;
import mg.finance.apiv.security.utilisateur.entity.UtilisateurAPI;
import mg.finance.apiv.security.utilisateur.viewObject.UtilisateurAPIPasswordVO;

public interface UtilisateurAPIService {
    UtilisateurAPI findUtilisateurByUsername(String username);
    void ajouterTentativeConnexion(Long idUtilisateur) throws Exception;
    void reinitialiserTentativeConnexion(Long idUtilisateur) throws Exception;
    void desactiverUtilisateurTropDeTentative(Long idUtilisateur) throws Exception;
    UtilisateurAPI saveUtilisateur(UtilisateurAPI utilisateur) throws Exception;
    UtilisateurAPI updateUtilisateur(UtilisateurAPI utilisateur) throws Exception;
    UtilisateurAPI updateUtilisateurPassword(UtilisateurAPIPasswordVO utilisateurAPIPasswordVO) throws Exception;
    void deleteUtilisateur(UtilisateurAPI utilisateur);
    UtilisateurAPI getActiveUser();

    RoleAPI saveRole(RoleAPI role);
    RoleAPI updateRole(RoleAPI role) throws Exception;
    void deleteRole(RoleAPI role);
    PermissionAPI savePermission(PermissionAPI permission);
    PermissionAPI updatePermission(PermissionAPI permission) throws Exception;
    void deletePermission(PermissionAPI permission);
    void addPermissionToRole(String roleNom, String[] permissionNom);
    UtilisateurAPI addRoleToUtilisateur(String username, String roleNom);

}
