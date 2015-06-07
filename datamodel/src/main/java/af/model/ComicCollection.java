package af.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A collection of comics.
 * This object is mainly used as a container for grouping <tt>Comic</tt> objects in XML binding.
 * Created by sasha on 29/05/15.
 */
@XmlRootElement(name = "collection")
@XmlType(propOrder = {"created", "comics"})
public class ComicCollection {
    private String name;
    private final Date created = new Date();
    private List<Comic> comics = new ArrayList<>();

    static ComicCollection fromXml(File xmlFile) {
        return null;
    }

    public static ComicCollection fromQuery(String q) {
        return null;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public Date getCreated() {
        return created;
    }

    @XmlElement(name = "comic")
    public List<Comic> getComics() {
        return comics;
    }
}
