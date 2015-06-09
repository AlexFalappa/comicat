package af.dbgen;

import af.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import java.util.*;

/**
 * Generator of dummy comics.
 * <p>
 * Created by sasha on 01/06/15.
 */
public class CreateApp {
    public static final Logger log = LoggerFactory.getLogger(CreateApp.class);
    private static final Genre[] GENRES = Genre.class.getEnumConstants();
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
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            clp.printSingleLineUsage(System.err);
            clp.printUsage(System.err);
            System.exit(1);
        }
        log.info("dbgen started");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu.comic", emfPropsFrom(cla));
        EntityManager em = emf.createEntityManager();
        try {
            // populate db
            log.info("------ Population -------");
            em.getTransaction().begin();
            knownComics(em);
            randComics(cla, em);
            em.getTransaction().commit();
            if (cla.xmlExport) {
                // xml export
                log.info("------ XML export -------");
                log.info("All comics:");
                ComicCollection cc = ComicCollection.fromDbAll(em);
                xmlMarshal(cc);
                log.info("Ringo comics:");
                cc = ComicCollection.fromDbQuery("RingoComics", "select c from Comic c where c.title='Ringo'", em);
                xmlMarshal(cc);
            }
        } catch (PersistenceException | JAXBException pe) {
            pe.printStackTrace();
            System.exit(1);
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void knownComics(EntityManager em) {
        log.info("Persisting 'known' comics");
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

        ComicIssue issue = new ComicIssue(9);
        issue.setPublishDate(new Date());
        issue.getArtBy().add(a1);
        issue.getTextBy().add(a2);
        issue.setPages(38);
        issue.setPrice(BigDecimal.valueOf(4.5));
        issue.setComic(c1);

        ComicIssue issue2 = new ComicIssue(10);
        issue2.setPublishDate(new Date());
        issue2.setPages(54);
        issue2.getArtBy().add(a2);
        issue2.setComic(c1);

        c1.getIssues().add(issue);
        c1.getIssues().add(issue2);
        em.persist(c1);
    }

    private static void randComics(CmdLineArgs cla, EntityManager em) {
        log.info("Generating {} comics records", cla.recNum);
        for (int i = 1; i <= cla.recNum; i++) {
            // show progress if needed
            if (cla.recNum > 500) {
                if (i % 100 == 0) {
                    log.info(" ... {} ...", i);
                }
            }
            // basic attributes
            Comic c = new Comic(RandomStringUtils.randomAscii(5 + rgen.nextInt(15)), genComicTpe());
            c.setPublisher(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            c.setType(rgen.nextDouble() < 0.8 ? ComicType.PERIODICAL : ComicType.MONOGRAPH);
            if (rgen.nextDouble() < 0.33) {
                c.setGenre(GENRES[rgen.nextInt(GENRES.length)]);
            }
            // issues
            for (int j = 1; j <= rgen.nextInt(100); j++) {
                ComicIssue issue = new ComicIssue(j);
                issue.setComic(c);
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
                issue.setPages(20 + rgen.nextInt(80));
                issue.setPublishDate(new Date(System.currentTimeMillis() + rgen.nextInt()));
                c.getIssues().add(issue);
            }
            // series (and related attributes)
            if (rgen.nextDouble() < 0.7) {
                c.setSeries(genSeries(em));
                if (rgen.nextDouble() < 0.2) {
                    c.setSeriesIssue(rgen.nextInt(999));
                    if (rgen.nextDouble() < 0.1) {
                        c.setSubTitle(RandomStringUtils.randomAscii(5 + rgen.nextInt(15)));
                    }
                }
            }
            // other
            c.setFrequency(genFreq());
            if (rgen.nextDouble() < 0.4) {
                String lang = RandomStringUtils.randomAscii(5 + rgen.nextInt(10));
                c.setLanguage(lang);
                c.setCountry(lang);
            }
            if (rgen.nextDouble() < 0.1) {
                c.setNotes(RandomStringUtils.randomAscii(15 + rgen.nextInt(20)));
            }
            em.persist(c);
        }
        log.info(" ... done");
    }

    private static Series genSeries(EntityManager em) {
        if (rgen.nextDouble() < 0.8 && !seriesCache.isEmpty()) {
            return seriesCache.get(rgen.nextInt(seriesCache.size()));
        } else {
            Series s = new Series();
            s.setName(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            em.persist(s);
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
        props.put("javax.persistence.schema-generation.database.action", "drop-and-create");
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

    private static void xmlMarshal(ComicCollection cl) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ComicCollection.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(cl, System.out);
    }
}
