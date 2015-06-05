package af.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of comics.
 * Created by sasha on 29/05/15.
 */
@XmlRootElement(name = "collection")
public class ComicCollection {
    @XmlElement(name = "comic")
    private List<Comic> comics = new ArrayList<>();

    static ComicCollection fromXml(File xmlFile) {
        return null;
    }

    public static ComicCollection fromQuery(String q) {
        ComicCollection ret = new ComicCollection();
        return ret;
    }

    public List<Comic> getComics() {
        return comics;
    }
}
