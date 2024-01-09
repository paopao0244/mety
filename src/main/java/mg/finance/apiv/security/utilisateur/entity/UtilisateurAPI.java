package mg.finance.apiv.security.utilisateur.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="UTILISATEUR_API", uniqueConstraints = {@UniqueConstraint(name="username_api_unique",columnNames = {"username"})})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UtilisateurAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String nom;
    private String mail;
    private String telephone;
    @JsonIgnore
    private String password;
    @Transient
    private String passwordFront;
    @JsonIgnore
    private String generatedPassword;

    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private RoleAPI role;

    private Boolean isActive;

    @Column(name = "date_creation", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime dateCreation;
    private Integer tentativeConnexion = 0;
    @Transient
    private String roleFront;

    public List<String> getListPermissions(){
        List<String> permissions = new ArrayList<>();
        if(this.getRole()!=null){
            permissions.add(this.getRole().getNom());
            this.getRole().getPermissions().forEach(permission -> permissions.add(permission.getNom()));
        }
        return permissions;
    }

    public UtilisateurAPI(String username, String passwordFront, String roleFront){
        this.username = username;
        this.passwordFront = passwordFront;
        this.roleFront = roleFront;
    }
}
