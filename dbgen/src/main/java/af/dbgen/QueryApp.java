package af.dbgen;

import af.model.Author;
import af.model.Comic;
import af.model.ComicIssue;
import af.model.ComicIssue_;
import af.model.Comic_;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query test application. Created by sasha on 07/06/15.
 */
public class QueryApp {

    public static final Logger log = LoggerFactory.getLogger(QueryApp.class);

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu.comic");
        EntityManager em = emf.createEntityManager();
        try {
            // query db
            log.info("------ JPQL Queries -------");

            TypedQuery<Author> q1 = em.createNamedQuery("Author.findByName", Author.class);
            q1.setParameter("nm", "Alessio");
            Author result = q1.getSingleResult();
            log.info("Author of name Alessio: {} {}", result.getName(), result.getSurname());
            log.info("Drawn comics: {}", result.getDrawnComics());

            TypedQuery<Comic> q2 = em.createNamedQuery("Comic.findByTitle", Comic.class);
            q2.setParameter("title", "Orfani: Ringo");
            Comic c = q2.getSingleResult();
            log.info("Issues of comic titled 'Orfani: Ringo' and their authors ({} items)", c.getIssues().size());
            if (!c.getIssues().isEmpty()) {
                for (ComicIssue ci : c.getIssues()) {
                    log.info("issue {}:", ci.getNumber());
                    for (Author a : ci.getArtBy()) {
                        log.info("   drawn by: {} {}", a.getName(), a.getSurname());
                    }
                    for (Author a : ci.getTextBy()) {
                        log.info("   written by: {} {}", a.getName(), a.getSurname());
                    }
                    for (Author a : ci.getInkBy()) {
                        log.info("   inked by: {} {}", a.getName(), a.getSurname());
                    }
                    for (Author a : ci.getColoursBy()) {
                        log.info("   coloured by: {} {}", a.getName(), a.getSurname());
                    }
                    for (Author a : ci.getCoverBy()) {
                        log.info("   cover by: {} {}", a.getName(), a.getSurname());
                    }
                }
                log.info(StringUtils.join(c.getIssues(), ", "));
            }
            q2 = em.createQuery("select c from Comic c where c.id between 1 and 10", Comic.class);
            log.info("Issues of comics of id 1..10:");
            for (Comic cmc : q2.getResultList()) {
                List<Integer> numbers = cmc.getIssues().stream().map(ComicIssue::getNumber).collect(Collectors.toList());
                log.info("  {}: {}", cmc.getTitle(), numbers);
            }

            Query q3 = em.createQuery("select min(publishDate),max(publishDate) from ComicIssue");
            Object[] res = (Object[]) q3.getSingleResult();
            log.info("Publication date range of all comic issues: {} -> {}", res[0], res[1]);

            log.info("------ Criteria API Queries -------");
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Comic> q4 = cb.createQuery(Comic.class);
            Root<Comic> rq = q4.from(Comic.class);
            Predicate cl1 = rq.get(Comic_.series).isNotNull();
            Predicate cl2 = rq.get(Comic_.lang).isNotNull();
            q4.select(rq).where(cb.and(cl1, cl2));
            TypedQuery<Comic> q = em.createQuery(q4);
            log.info("Comics with a language and a series: {}", q.getResultList());

            CriteriaQuery q5 = cb.createQuery();
            Root<Comic> rq2 = q5.from(Comic.class);
            ListJoin<Comic, ComicIssue> join = rq2.join(Comic_.issues);
            Predicate ge = cb.ge(join.get(ComicIssue_.number), 10);
            q5.select(rq).where(ge);
            TypedQuery tq = em.createQuery(q5);
            log.info("Comics with more than 10 number: {}", tq.getResultList());

        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.exit(1);
        } finally {
            em.close();
            emf.close();
        }

    }
}
