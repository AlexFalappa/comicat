package af.xmlgen;

import af.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import java.util.Date;

/**
 * XML generator.
 * Created by sasha on 04/06/15.
 */
public class App {
    public static void main(String[] args) throws JAXBException {
        Series s = new Series();
        s.setName("BonelliFantascienza");

        Author a1 = new Author("Antonio", "Serra");
        Author a2 = new Author("Gianni", "Cavazzano");
        Author a3 = new Author("Paolo", "Vigna");

        Comic c1 = new Comic();
        c1.setTitle("Ringo");
        c1.setType(ComicType.PERIODICAL);
        c1.setFrequency(Frequency.MONTHLY);
        c1.setGenre(Genre.SCIENCE_FICTION);
        c1.setPublisher("Bonelli");
        c1.setSeries(s);
        ComicIssue issue = new ComicIssue(9);
        issue.setComic(c1);
        issue.setPublishDate(new Date());
        issue.getArtBy().add(a1);
        issue.getTextBy().add(a2);
        issue.setPages(38);
        issue.setPrice(BigDecimal.valueOf(4.5));
        c1.getIssues().add(issue);

        Comic c2 = new Comic();
        c2.setTitle("Nathan Never");
        c2.setSeries(s);
        ComicIssue issue2 = new ComicIssue(123);
        issue2.setPublishDate(new Date());
        issue2.getArtBy().add(a1);
        issue2.getTextBy().add(a3);
        c2.getIssues().add(issue2);

        ComicCollection cl = new ComicCollection();
        cl.setName("scifi");
        cl.getComics().add(c1);
        cl.getComics().add(c2);

        xmlMarshal(cl);
    }

    private static void xmlMarshal(ComicCollection cl) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ComicCollection.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(cl, System.out);
    }
}
