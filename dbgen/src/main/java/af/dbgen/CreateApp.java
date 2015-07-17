package af.dbgen;

import af.model.Author;
import af.model.Comic;
import af.model.ComicCollection;
import af.model.ComicIssue;
import af.model.ComicType;
import af.model.Frequency;
import af.model.Genre;
import af.model.Series;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang3.RandomStringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generator of dummy comics.
 * <p>
 * Created by sasha on 01/06/15.
 */
public class CreateApp {

    public static final Logger log = LoggerFactory.getLogger(CreateApp.class);
    private static final Genre[] GENRES = Genre.class.getEnumConstants();
    private static final String CHARS = "abcdefgh ijklmnop qrstuvxw yz ABCDEFGH IJKLMNOP QRSTUVWX YZ";
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
            randomComics(cla, em);
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
        Calendar cal = new GregorianCalendar();

        Comic c1 = new Comic();
        c1.setTitle("Orfani: Ringo");
        c1.setType(ComicType.PERIODICAL);
        c1.setFrequency(Frequency.MONTHLY);
        c1.setGenre(Genre.SCIENCE_FICTION);
        c1.setPublisher("Sergio Bonelli Editore");

        ComicIssue issue = new ComicIssue(6);
        cal.set(2015, Calendar.MARCH, 1);
        issue.setPages(98);
        issue.setPrice(BigDecimal.valueOf(4.5));
        issue.setPublishDate(cal.getTime());
        issue.getArtBy().add(new Author("Alessio", "Avallone"));
        Author mu = new Author("Mauro", "Uzzeo");
        issue.getTextBy().add(mu);
        Author ema = new Author("Emiliano", "Mammuccari");
        issue.getCoverBy().add(ema);
        issue.getColoursBy().add(new Author("Nicola", "Righi"));
        issue.setComic(c1);

        ComicIssue issue2 = new ComicIssue(9);
        cal.set(2015, Calendar.JUNE, 1);
        issue2.setPublishDate(cal.getTime());
        issue2.setPages(98);
        issue2.setPrice(BigDecimal.valueOf(4.5));
        issue2.getArtBy().add(new Author("Matteo", "Cremona"));
        issue2.getTextBy().add(mu);
        issue2.getCoverBy().add(ema);
        issue2.getColoursBy().add(new Author("Giovanna", "Niro"));
        issue2.getColoursBy().add(new Author("Fabiola", "Ienne"));
        issue2.setComic(c1);

        c1.getIssues().add(issue);
        c1.getIssues().add(issue2);
        em.persist(c1);
    }

    private static void randomComics(CmdLineArgs cla, EntityManager em) {
        log.info("Generating {} comics records", cla.recNum);
        boolean authorsOnIssues;
        for (int i = 1; i <= cla.recNum; i++) {
            // show progress if needed
            if (cla.recNum > 500) {
                if (i % 100 == 0) {
                    log.info(" ... {} ...", i);
                }
            }
            // basic attributes
            Comic c = new Comic(RandomStringUtils.random(5 + rgen.nextInt(15), CHARS), genComicTpe());
            c.setPublisher(RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10)));
            c.setType(rgen.nextDouble() < 0.8 ? ComicType.PERIODICAL : ComicType.MONOGRAPH);
            // genre for 1/3 of comics
            if (rgen.nextDouble() < 0.33) {
                c.setGenre(GENRES[rgen.nextInt(GENRES.length)]);
            }
            // authors linked to issues instead of comic only for 20% of comics
            authorsOnIssues = rgen.nextDouble() < 0.2;
            if (!authorsOnIssues) {
                c.getArtBy().add(genAuthor(em));
                if (rgen.nextDouble() < 0.3) {
                    c.getTextBy().add(genAuthor(em));
                }
                if (rgen.nextDouble() < 0.1) {
                    c.getColoursBy().add(genAuthor(em));
                }
                if (rgen.nextDouble() < 0.05) {
                    c.getInkBy().add(genAuthor(em));
                }
            }
            // series for 70% of comics
            if (rgen.nextDouble() < 0.7) {
                c.setSeries(genSeries(em));
            }
            // issues
            for (int j = 1; j <= rgen.nextInt(100); j++) {
                ComicIssue issue = new ComicIssue(j);
                issue.setComic(c);
                if (authorsOnIssues) {
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
                }
                issue.setPrice(BigDecimal.valueOf(20.0 * rgen.nextDouble()));
                issue.setPages(20 + rgen.nextInt(80));
                issue.setPublishDate(new Date(System.currentTimeMillis() + rgen.nextInt()));
                // number in series only for 20%
                if (c.getSeries() != null && rgen.nextDouble() < 0.2) {
                    issue.setSeriesNumber(rgen.nextInt(999));
                    // subtitle only for 10%
                    if (rgen.nextDouble() < 0.1) {
                        issue.setSubTitle(RandomStringUtils.random(5 + rgen.nextInt(15), CHARS));
                    }
                }
                c.getIssues().add(issue);
            }
            // other
            c.setFrequency(genFreq());
            if (rgen.nextDouble() < 0.4) {
                // language for 40% of comics
                String lang = RandomStringUtils.randomAlphabetic(5 + rgen.nextInt(10));
                c.setLang(lang);
                c.setCountry(lang);
            }
            if (rgen.nextDouble() < 0.1) {
                // notes for 10%
                c.setNotes(RandomStringUtils.random(15 + rgen.nextInt(20), CHARS));
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
            s.setName(RandomStringUtils.random(5 + rgen.nextInt(10), CHARS));
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
            Author a = new Author(RandomStringUtils.randomAlphabetic(3 + rgen.nextInt(7)), RandomStringUtils.randomAlphabetic(5 + rgen
                    .nextInt(10)));
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
