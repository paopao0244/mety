package mg.finance.apiv.annonce;

import mg.finance.apiv.annonce.annonce.Annonce;

import java.util.List;

public interface AnnonceService {
    List<Annonce> getAll() throws Exception;
}
