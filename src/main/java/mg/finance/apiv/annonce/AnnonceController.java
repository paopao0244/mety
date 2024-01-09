package mg.finance.apiv.annonce;

import lombok.RequiredArgsConstructor;
import mg.finance.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/annonce")
@RequiredArgsConstructor
public class AnnonceController {
    private final AnnonceService annonceService;
    @GetMapping("/get-all")
    //@PreAuthorize("hasAuthority('GET_AGENT_BY_MATRICULE')")
    public ResponseEntity<?> getAll(){
        try{
            return ResponseEntity.ok().body(annonceService.getAll());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
        }
    }

}
