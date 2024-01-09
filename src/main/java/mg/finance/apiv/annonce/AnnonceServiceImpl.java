package mg.finance.apiv.annonce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.finance.apiv.annonce.annonce.Annonce;
import mg.finance.apiv.annonce.annonce.AnnonceDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnonceServiceImpl implements AnnonceService{
    private final AnnonceDAO annonceDAO;

    @Override
    public List<Annonce> getAll() throws Exception {
        return annonceDAO.getAll();
    }

}
