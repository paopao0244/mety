package mg.finance.apiv.security.utilisateur.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ROLE_API")
public class RoleAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String description;
    private String abreviation;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions_api",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permissions_id"),
            uniqueConstraints = {@UniqueConstraint(name="unique_role_permission_api",columnNames = {"role_id","permissions_id"})}
    )
    @JsonIgnore
    private Collection<PermissionAPI> permissions =new ArrayList<>();

    public RoleAPI(String nom, String description, String abreviation){
        this.nom = nom;
        this.description = description;
        this.abreviation = abreviation;
    }

}
