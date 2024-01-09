package mg.finance.apiv.security.utilisateur.viewObject;

import lombok.Data;

@Data
public class UtilisateurAPIPasswordVO {
    private Long id;
    private String username;
    private String oldPassword;
    private String newPassword;
}
