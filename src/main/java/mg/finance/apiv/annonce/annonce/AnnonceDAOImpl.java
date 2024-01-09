package mg.finance.apiv.annonce.annonce;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnnonceDAOImpl implements AnnonceDAO {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Annonce> getAll() {
        String query = "Select a.* " +
                "from Annonce a " ;
        return entityManager.createNativeQuery(query, Annonce.class).getResultList();
    }

}
