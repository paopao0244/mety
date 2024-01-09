package mg.finance.apiv.annonce.annonce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ANNONCE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Annonce {
    @Id
    private String id;
    private Double prix;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateAnnonce;
    private Long idVoiture;
    private Long idUser;
    private String etatValidation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateValidation;
    private String etatVendu;
}
