package mg.finance.apiv.security.utilisateur.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="PERMISSION_API")
public class PermissionAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String groupe;
    private String description;

    public PermissionAPI(String nom, String groupe, String description){
        this.nom = nom;
        this.groupe = groupe;
        this.description = description;
    }
}
