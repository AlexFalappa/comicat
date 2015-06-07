package af.dbgen;

import af.model.Author;
import af.model.Comic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Query test application.
 * Created by sasha on 07/06/15.
 */
public class QueryApp {
    public static final Logger log = LoggerFactory.getLogger(QueryApp.class);

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu.comic");
        EntityManager em = emf.createEntityManager();
        try {
            // query db
            log.info("------ Query -------");
            TypedQuery<Author> q = em.createNamedQuery("Author.findByName", Author.class);
            q.setParameter("nm", "Antonio");
            Author result = q.getSingleResult();
            log.info("Author: {} {}", result.getName(), result.getSurname());
            log.info("Drawn comics: {}", result.getDrawnComics());
            TypedQuery<Comic> q2 = em.createNamedQuery("Comic.findByTitle", Comic.class);
            q2.setParameter("title", "Ringo");
            Comic c = q2.getSingleResult();
            log.info("Issues: {}", c.getIssues().size());
            if (!c.getIssues().isEmpty()) {
                log.info(StringUtils.join(c.getIssues(), ", "));
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.exit(1);
        } finally {
            em.close();
            emf.close();
        }

    }
}
