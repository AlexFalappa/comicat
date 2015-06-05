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
        Comic c1 = new Comic();
        c1.setTitle("Ringo");
        c1.setIssue(9);
        c1.setType(ComicType.PERIODICAL);
        c1.setFrequency(Frequency.MONTHLY);
        c1.setPublishDate(new Date());
        c1.setGenre(Genre.SCIENCE_FICTION);
        c1.setPublisher("Bonelli");
        Author a1 = new Author("Antonio", "Serra");
        c1.getArtBy().add(a1);
        Author a2 = new Author("Gianni", "Cavazzano");
        c1.getTextBy().add(a2);
        c1.setPages(38);
        c1.setPrice(BigDecimal.valueOf(4.5));

        Comic c2 = new Comic();
        c2.setTitle("Nathan Never");
        c2.getArtBy().add(a1);
        c2.getTextBy().add(a2);
        c2.setIssue(123);

        ComicCollection cl = new ComicCollection();
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
