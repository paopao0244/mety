package mg.finance.apiv;

import lombok.RequiredArgsConstructor;
import mg.finance.apiv.security.configuration.utils.JwtGenerator;
import mg.finance.apiv.security.utilisateur.UtilisateurAPIService;
import mg.finance.apiv.security.utilisateur.entity.RoleAPI;
import mg.finance.apiv.security.utilisateur.entity.UtilisateurAPI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;


@SpringBootApplication
@RequiredArgsConstructor
public class ApiVApplication {
	private final Environment environment;
	@Bean
	public JwtGenerator jwtGenerator(){
		return new JwtGenerator(environment.getProperty("jwt.secret"), Long.valueOf(Objects.requireNonNull(environment.getProperty("jwt.duration"))));
	}
	@Bean
	BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(ApiVApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UtilisateurAPIService utilisateurAPIService) {
		return args -> {
//			RoleAPI role = new RoleAPI("ROLE_USER","Rôle par défaut","Simple utilisateur");
//			RoleAPI role1 = new RoleAPI("ROLE_ADMIN","Rôle pour Back-Office","ADMIN");
//			utilisateurAPIService.saveRole(role);
//			utilisateurAPIService.saveRole(role1);
//	TEST
//			PermissionAPI permissionAPI = new PermissionAPI("GET_AGENT_BY_MATRICULE","Informations agent","Récupérer les informations d'un agent à partir d'un matricule");
//			PermissionAPI permissionAPI1 = new PermissionAPI("GET_POSTE_BY_MATRICULE","Informations agent","Récupérer les postes agents à partir d'un matricule");
//			PermissionAPI permissionAPI2 = new PermissionAPI("GET_AVANCE_BY_MATRICULE","Informations agent","Récupérer les avances d'un agent à partir d'un matricule");
//			PermissionAPI permissionAPI3 = new PermissionAPI("GET_AFFECTATION_BY_POSTE","Informations agent","Récupérer les affectations d'un poste agent");
//			PermissionAPI permissionAPI4 = new PermissionAPI("GET_ENTETE_BY_POSTE","Informations agent","Récupérer les mouvements d'un poste agent");
//			PermissionAPI permissionAPI5 = new PermissionAPI("GET_FIC_RECAP_BY_POSTE_EXERCICE_MOIS","Solde agent","Récupérer les fiches recap d'un poste agent pour une année donnée");
//			PermissionAPI permissionAPI6 = new PermissionAPI("GET_MISSION_ALL_BY_POSTE_EXERCICE_MOIS","Solde agent","Récupérer les details de solde d'un poste agent pour une période donnée");
//			utilisateurAPIService.savePermission(permissionAPI);
//			utilisateurAPIService.savePermission(permissionAPI1);
//			utilisateurAPIService.savePermission(permissionAPI2);
//			utilisateurAPIService.savePermission(permissionAPI3);
//			utilisateurAPIService.savePermission(permissionAPI4);
//			utilisateurAPIService.savePermission(permissionAPI5);
//			utilisateurAPIService.savePermission(permissionAPI6);
//
//
//			utilisateurAPIService.addPermissionToRole("ROLE_DRH",new String[]{"GET_AGENT_BY_MATRICULE","GET_POSTE_BY_MATRICULE","GET_AVANCE_BY_MATRICULE","GET_AFFECTATION_BY_POSTE",
//			"GET_ENTETE_BY_POSTE","GET_FIC_RECAP_BY_POSTE_EXERCICE_MOIS","GET_MISSION_ALL_BY_POSTE_EXERCICE_MOIS"});
//
//			UtilisateurAPI user = new UtilisateurAPI("admin",null, "ROLE_ADMIN");
//			utilisateurAPIService.saveUtilisateur(user);
		};
	}

}
