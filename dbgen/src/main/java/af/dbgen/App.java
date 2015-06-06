package af.dbgen;

import af.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Generator of dummy comics.
 * <p>
 * Created by sasha on 01/06/15.
 */
public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);
    private static Random rgen = new Random();
    private static List<Author> authCache = new ArrayList<>();
    private static List<Series> seriesCache = new ArrayList<>();

    public static void main(String[] args) {
        CmdLineArgs cla = new CmdLineArgs();
        CmdLineParser clp = new CmdLineParser(cla);
        try {
            clp.parseArgument(args);
            if (cla.recNum <= 0) {
                System.err.println("Record number must be greater than zero");
                clp.printSingleLineUsage(System.err);
                clp.printUsage(System.err);
                System.exit(1);
            }
            log.info("dbgen started");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu.comic", emfPropsFrom(cla));
            EntityManager em = emf.createEntityManager();
            try {
                // populate db
                em.getTransaction().begin();
                generateComics(cla, em);
                persistComics(em);
                em.getTransaction().commit();
                // query db
                Query q = em.createNamedQuery("Comic.findByTitle", Comic.class);
                q.setParameter("title", "Ringo");
                Object res = q.getSingleResult();
            } finally {
                em.close();
                emf.close();
            }
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            clp.printSingleLineUsage(System.err);
            clp.printUsage(System.err);
            System.exit(1);
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            System.exit(1);
        }
    }

    private static void persistComics(EntityManager em) {
        Author a1 = new Author("Antonio", "Serra");
        Author a2 = new Author("Gianni", "Cavazzano");

        em.persist(a1);
        em.persist(a2);

        Comic c1 = new Comic();
        c1.setTitle("Ringo");
        c1.setType(ComicType.PERIODICAL);
        c1.setFrequency(Frequency.MONTHLY);
        c1.setGenre(Genre.SCIENCE_FICTION);
        c1.setPublisher("Bonelli");
        ComicIssue issue = new ComicIssue();
        issue.setNumber(9);
        issue.setPublishDate(new Date());
        issue.getArtBy().add(a1);
        issue.getTextBy().add(a2);
        issue.setPages(38);
        issue.setPrice(BigDecimal.valueOf(4.5));
        em.persist(issue);
        c1.getIssues().add(issue);

        Comic c2 = new Comic();
        c2.setTitle("Nathan Never");
        ComicIssue issue2 = new ComicIssue();
        issue2.getArtBy().add(a1);
        issue2.getTextBy().add(a2);
        issue2.setNumber(123);
        em.persist(issue2);
        c2.getIssues().add(issue2);

        em.persist(c1);
        em.persist(c2);
    }

    private static void generateComics(CmdLineArgs cla, EntityManager em) {
        log.info("Generating {} comics records", cla.recNum);
        for (int i = 0; i < cla.recNum; i++) {
            //TODO mostrare un progresso ogni tot record generati
            // basic attributes
            Comic c = new Comic(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(15)), genComicTpe());
            c.setPublisher(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            // authors
            ComicIssue issue = new ComicIssue(rgen.nextInt(1000));
            issue.getArtBy().add(genAuthor(em));
            if (rgen.nextDouble() < 0.4) {
                issue.getTextBy().add(genAuthor(em));
            }
            if (rgen.nextDouble() < 0.2) {
                issue.getColoursBy().add(genAuthor(em));
            }
            if (rgen.nextDouble() < 0.2) {
                issue.getInkBy().add(genAuthor(em));
            }
            if (rgen.nextDouble() < 0.1) {
                issue.getCoverBy().add(genAuthor(em));
            }
            issue.setPrice(BigDecimal.valueOf(20.0 * rgen.nextDouble()));
            em.persist(issue);
            c.getIssues().add(issue);
            // series
            if (rgen.nextDouble() < 0.7) {
                c.setSeries(genSeries());
            }
            // other
            c.setFrequency(genFreq());
            //TODO aggiungere altri attributi
            em.persist(c);
        }
    }

    private static Series genSeries() {
        if (rgen.nextDouble() < 0.8 && !seriesCache.isEmpty()) {
            return seriesCache.get(rgen.nextInt(seriesCache.size()));
        } else {
            Series s = new Series();
            s.setSeriesName(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            seriesCache.add(s);
            return s;
        }
    }

    private static Frequency genFreq() {
        return Frequency.values()[rgen.nextInt(Frequency.values().length)];
    }

    private static Author genAuthor(EntityManager em) {
        if (rgen.nextDouble() < 0.7 && !authCache.isEmpty()) {
            return authCache.get(rgen.nextInt(authCache.size()));
        } else {
            Author a = new Author(RandomStringUtils.randomAlphabetic(3 + rgen.nextInt(7)), RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            em.persist(a);
            authCache.add(a);
            return a;
        }
    }

    private static ComicType genComicTpe() {
        return rgen.nextDouble() < 0.2 ? ComicType.MONOGRAPH : ComicType.PERIODICAL;
    }

    private static Map<String, String> emfPropsFrom(CmdLineArgs cla) {
        Map<String, String> props = new HashMap<>();
        if (cla.jdbcDriver != null) {
            props.put("javax.persistence.jdbc.driver", cla.jdbcDriver);
        }
        if (cla.jdbcUrl != null) {
            props.put("javax.persistence.jdbc.url", cla.jdbcUrl);
        }
        if (cla.jdbcUser != null) {
            props.put("javax.persistence.jdbc.user", cla.jdbcUser);
        }
        if (cla.jdbcPassw != null) {
            props.put("javax.persistence.jdbc.password", cla.jdbcPassw);
        }
        return props;
    }
}
