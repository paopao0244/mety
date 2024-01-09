package mg.finance.apiv.security.utilisateur;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.finance.apiv.security.utilisateur.entity.PermissionAPI;
import mg.finance.apiv.security.utilisateur.entity.RoleAPI;
import mg.finance.apiv.security.utilisateur.entity.UtilisateurAPI;
import mg.finance.apiv.security.utilisateur.viewObject.UtilisateurAPIPasswordVO;
import mg.finance.utils.FonctionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class UtilisateurAPIServiceImpl implements UtilisateurAPIService, UserDetailsService {
    private final UtilisateurAPIRepo utilisateurAPIRepo;
    private final RoleAPIRepo roleAPIRepo;
    private final PermissionAPIRepo permissionAPIRepo;

    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username = FonctionUtils.traiterString(username);
        Optional<UtilisateurAPI> optionalUser = utilisateurAPIRepo.findByUsername(username);
        if(!optionalUser.isPresent()){
            log.error("L'username {} est introuvable dans la base de données", username);
            throw new UsernameNotFoundException("L'utilisateur est introuvable");
        }else{
            log.info("L'username {} est présent dans la base de données", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        UtilisateurAPI user = optionalUser.get();
        user.getListPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        return new User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public UtilisateurAPI findUtilisateurByUsername(String username) {
        username = FonctionUtils.traiterString(username);
        log.info("Recupération de l'utilisateur username = {}", username);
        Optional<UtilisateurAPI> byUsername = utilisateurAPIRepo.findByUsername(username);
        return byUsername.orElse(null);
    }

    @Override
    public void ajouterTentativeConnexion(Long idUtilisateur) throws Exception {
        log.info("Ajout du nombre de tentative de connexion de l'utilisateurAPI id ={} car le mot de passe est erroné" ,idUtilisateur);
        if(idUtilisateur != null){
            Optional<UtilisateurAPI> optionalUtilisateurUpdate = utilisateurAPIRepo.findById(idUtilisateur);
            if(!optionalUtilisateurUpdate.isPresent()){
                log.error("L'utilisateur à ajouter le nombre de tentative est introuvable");
                throw new Exception("L'utilisateur à ajouter le nombre de tentative est introuvable");
            }
            UtilisateurAPI utilisateurUpdate = optionalUtilisateurUpdate.get();
            utilisateurUpdate.setTentativeConnexion(utilisateurUpdate.getTentativeConnexion()+1);
        }else{
            log.error("L'id de l'utilisateur n'est pas spécifié");
            throw new Exception("L'id de l'utilisateur n'est pas spécifié");
        }
    }

    @Override
    public void reinitialiserTentativeConnexion(Long idUtilisateur) throws Exception {
        log.info("Réinitialisation du nombre de tentative de connexion de l'utilisateurAPI id ={} car la connexion a été un succès" ,idUtilisateur);
        if(idUtilisateur != null){
            Optional<UtilisateurAPI> optionalUtilisateurUpdate = utilisateurAPIRepo.findById(idUtilisateur);
            if(!optionalUtilisateurUpdate.isPresent()){
                log.error("L'utilisateur à reinitialiser le nombre de tentative est introuvable");
                throw new Exception("L'utilisateur à reinitialiser le nombre de tentative est introuvable");
            }
            UtilisateurAPI utilisateurUpdate = optionalUtilisateurUpdate.get();
            utilisateurUpdate.setTentativeConnexion(utilisateurUpdate.getTentativeConnexion()+1);
            utilisateurUpdate.setTentativeConnexion(0);
        }else{
            log.error("L'id de l'utilisateur n'est pas spécifié");
            throw new Exception("L'id de l'utilisateur n'est pas spécifié");
        }
    }

    @Override
    public void desactiverUtilisateurTropDeTentative(Long idUtilisateur) throws Exception {
        log.info("Réinitialisation du nombre de tentative de connexion de l'utilisateurAPI id ={} car la connexion a été un succès" ,idUtilisateur);
        if(idUtilisateur != null){
            Optional<UtilisateurAPI> optionalUtilisateurUpdate = utilisateurAPIRepo.findById(idUtilisateur);
            if(!optionalUtilisateurUpdate.isPresent()){
                log.error("L'utilisateur à vérouiller est introuvable");
                throw new Exception("L'utilisateur à vérouiller est introuvable");
            }
            UtilisateurAPI utilisateurUpdate = optionalUtilisateurUpdate.get();
            utilisateurUpdate.setTentativeConnexion(utilisateurUpdate.getTentativeConnexion()+1);
            utilisateurUpdate.setIsActive(false);
        }else{
            log.error("L'id de l'utilisateur n'est pas spécifié");
            throw new Exception("L'id de l'utilisateur n'est pas spécifié");
        }
    }

    @Override
    public UtilisateurAPI saveUtilisateur(UtilisateurAPI utilisateur) throws Exception {
        log.info("Enregistrement d'un nouvel utilisateur API {} dans la base" , utilisateur.getUsername());
        UtilisateurAPI activeUser = this.getActiveUser();
//        if (utilisateur.getPasswordFront() != null) utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPasswordFront()));
        //Mot de passe aléatoire
        String generatedPassword = "123456789";
//        String generatedPassword = RandomPassword.getAlphaNumericString(10);
        utilisateur.setPassword(passwordEncoder.encode(generatedPassword));

        utilisateur.setDateCreation(LocalDateTime.now());
        utilisateur.setIsActive(true);
        utilisateur.setGeneratedPassword(generatedPassword);

        // Enregistrement de l'utilisateur
        UtilisateurAPI utilisateurSaved = utilisateurAPIRepo.save(utilisateur);
        // Role par defaut ROLE_USER
        String roleToSave = utilisateur.getRoleFront() != null ? (utilisateur.getRoleFront().startsWith("ROLE_") ? utilisateur.getRoleFront() : "ROLE_USER") : "ROLE_USER";

        return addRoleToUtilisateur(utilisateurSaved.getUsername(), roleToSave);
    }

    @Override
    public UtilisateurAPI updateUtilisateur(UtilisateurAPI utilisateur) throws Exception {
        log.info("Modification de l'utilisateur API id ={} dans la base" , utilisateur.getId());
        if(utilisateur.getId() != null){
            try {
                UtilisateurAPI utilisateurUpdate = utilisateurAPIRepo.getById(utilisateur.getId());
                if(utilisateur.getRoleFront() != null && !utilisateurUpdate.getRole().getNom().equals(utilisateur.getRoleFront())){
                    return addRoleToUtilisateur(utilisateurUpdate.getUsername(), utilisateur.getRoleFront());
                }
                return utilisateurUpdate;
            }catch (Exception e){
                e.printStackTrace();
                log.error("L'utilisateur API à modifier est introuvable");
                throw new Exception("L'utilisateur API à modifier est introuvable");
            }
        }
        log.error("L'utilisateur API à modifier est introuvable");
        throw new Exception("L'id de l'utilisateur API à modifier n'est pas spécifié");
    }

    @Override
    public UtilisateurAPI updateUtilisateurPassword(UtilisateurAPIPasswordVO utilisateurAPI) throws Exception {
        log.info("Modification du mot de passe d l'utilisateur API id ={} " , utilisateurAPI.getId());
        if(utilisateurAPI.getId() != null){
            try {
                UtilisateurAPI utilisateurUpdate = utilisateurAPIRepo.getById(utilisateurAPI.getId());
                if (utilisateurAPI.getNewPassword() != null && passwordEncoder.matches(utilisateurAPI.getOldPassword(),utilisateurUpdate.getPassword())){
                    utilisateurUpdate.setPassword(passwordEncoder.encode(utilisateurAPI.getNewPassword()));
                }else{
                    log.error("L'ancien mot de passe ne correspond pas");
                    throw new Exception("L'ancien mot de passe ne correspond pas");
                }
                return utilisateurUpdate;
            }catch (IllegalArgumentException e){
                log.error("L'utilisateur API à modifier est introuvable");
                throw new Exception("L'utilisateur API à modifier est introuvable");
            }catch (Exception e){
                log.error(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }else {
            log.error("L'id de l'utilisateur API à modifier n'est pas spécifié");
            throw new Exception("L'id de l'utilisateur API à modifier n'est pas spécifié");
        }
    }

    @Override
    public void deleteUtilisateur(UtilisateurAPI utilisateur) {
        if(utilisateur.getId() != null){
            log.info("Suppression d'un utilisateur API id = {}", utilisateur.getId());
            utilisateurAPIRepo.deleteById(utilisateur.getId());
        }
    }

    @Override
    public UtilisateurAPI getActiveUser() {
        try{
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if(principal != null) username = principal.toString();
            if(!username.equals("")){
                return this.findUtilisateurByUsername(username);
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public RoleAPI saveRole(RoleAPI role) {
        RoleAPI byNom = roleAPIRepo.findByNom(role.getNom());
        if(byNom == null){
            log.info("Enregistrement d'un nouveau role API {} dans la base", role.getNom());
            return roleAPIRepo.save(role);
        }
        log.info("Role API {} est déjà dans la base", role.getNom());
        return byNom;
    }

    @Override
    public RoleAPI updateRole(RoleAPI role) throws Exception {
        if(role.getId()  != null){
            log.info("Modification du role API {} dans la base", role.getNom());
            try{
                RoleAPI roleToUpdate = roleAPIRepo.findById(role.getId()).get();
                if(role.getDescription() !=null) roleToUpdate.setDescription(role.getDescription());
                if(role.getNom() !=null) roleToUpdate.setNom(role.getNom());
                if(role.getAbreviation() !=null) roleToUpdate.setAbreviation(role.getAbreviation());
                return roleToUpdate;
            }catch (Exception e){
                log.error("Erreur lors de la modification du role API dans la base");
                e.printStackTrace();
                throw new Exception("Le rôle API à modifier est introuvable");
            }
        }
        log.info("L'id du rôle API à modifier n'est pas spécifié!");
        throw new Exception("L'id du rôle API à modifier n'est pas spécifié!");
    }

    @Override
    public void deleteRole(RoleAPI role) {
        log.info("Suppression du rôle API id = {}", role.getId());
        if(role.getId() != null){
            roleAPIRepo.deleteById(role.getId());
        }
    }

    @Override
    public PermissionAPI savePermission(PermissionAPI permission) {
        PermissionAPI byNom = permissionAPIRepo.findByNom(permission.getNom());
        if(byNom == null){
            log.info("Enregistrement d'une nouvelle permission API {} dans la base", permission.getNom());
            return permissionAPIRepo.save(permission);
        }
        log.info("Permission API {} est déjà dans la base", permission.getNom());
        return byNom;
    }

    @Override
    public PermissionAPI updatePermission(PermissionAPI permission) throws Exception {
        if(permission.getId() != null){
            try{
                log.info("Modification d'une permission API id = {}", permission.getId());
                PermissionAPI permissionToUpdate = permissionAPIRepo.findById(permission.getId()).get();

                permissionToUpdate.setDescription(permission.getDescription());
                permissionToUpdate.setGroupe(permission.getGroupe());
                permissionToUpdate.setNom(permission.getNom());

                return permissionToUpdate;
            }catch (Exception e){
                e.printStackTrace();
                log.error("Permission API à modifier est introuvable");
                throw new Exception("Permission API à modifier est introuvable");
            }
        }
        throw new Exception("L'id de la permission API à modifier n'est pas spécifié");
    }

    @Override
    public void deletePermission(PermissionAPI permission) {
        if(permission.getId() != null){
            log.info("Suppression d'une permission API id = {}", permission.getId());
            permissionAPIRepo.deleteById(permission.getId());
        }
    }

    @Override
    public void addPermissionToRole(String roleNom, String[] permissionNom) {
        log.info("Ajout d'une permission API {} pour le role API {}", roleNom, permissionNom );
        RoleAPI role = roleAPIRepo.findByNom(roleNom);
        Set<PermissionAPI> permissions = new HashSet<>();
        for (String s : permissionNom) {
            PermissionAPI permissionToAdd = permissionAPIRepo.findByNom(s);
            permissions.add(permissionToAdd);
        }
        role.setPermissions(permissions);
    }

    @Override
    public UtilisateurAPI addRoleToUtilisateur(String username, String roleNom) {
        log.info("Ajout d'un role API {} pour l'utilisateur {}", roleNom, username );
        UtilisateurAPI utilisateur = this.findUtilisateurByUsername(username);
        RoleAPI role = roleAPIRepo.findByNom(roleNom);
        utilisateur.setRole(role);
        return utilisateur;
    }
}
