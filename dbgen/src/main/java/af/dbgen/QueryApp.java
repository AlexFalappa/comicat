package af.dbgen;

import af.model.Author;
import af.model.Comic;
import af.model.ComicIssue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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
            log.info("Author of name Antonio: {} {}", result.getName(), result.getSurname());
            log.info("Drawn comics: {}", result.getDrawnComics());

            TypedQuery<Comic> q2 = em.createNamedQuery("Comic.findByTitle", Comic.class);
            q2.setParameter("title", "Ringo");
            Comic c = q2.getSingleResult();
            log.info("Issues of comic titled 'Ringo': {}", c.getIssues().size());
            if (!c.getIssues().isEmpty()) {
                log.info(StringUtils.join(c.getIssues(), ", "));
            }

            Query q3 = em.createQuery("select min(publishDate),max(publishDate) from ComicIssue");
            Object[] res = (Object[]) q3.getSingleResult();
            log.info("Publication date range of all comic issues: {} -> {}", res[0], res[1]);

            q2 = em.createQuery("select c from Comic c where c.id between 1 and 10", Comic.class);
            log.info("Issues of comics of id 1..10:");
            for (Comic cmc : q2.getResultList()) {
                List<Integer> numbers = cmc.getIssues().stream().map(ComicIssue::getNumber).collect(Collectors.toList());
                log.info("  {}: {}", cmc.getTitle(), numbers);
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
