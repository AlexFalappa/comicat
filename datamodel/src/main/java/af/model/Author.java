package af.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.Collection;

/**
 * A comic author.
 * Created by sasha on 23/05/15.
 */
@Entity
@Table(indexes = {
        @Index(columnList = "name,surname", unique = true),
})
@NamedQueries({
        @NamedQuery(name = "Author.findByName", query = "select a from Author a where a.name=:nm"),
        @NamedQuery(name = "Author.findBySurname", query = "select a from Author a where a.surname=:surnm"),
        @NamedQuery(name = "Author.findByNameSurname", query = "select a from Author a where a.name=:nm and a.surname=:surnm")
})
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String surname;
    @ManyToMany(mappedBy = "artBy")
    private Collection<Comic> drawnComics;
    @ManyToMany(mappedBy = "textBy")
    private Collection<Comic> writtenComics;
    @ManyToMany(mappedBy = "coverBy")
    private Collection<Comic> coverDrawnComics;
    @ManyToMany(mappedBy = "coloursBy")
    private Collection<Comic> colouredComics;
    @ManyToMany(mappedBy = "inkBy")
    private Collection<Comic> inkedComics;

    public Author() {
    }

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Collection<Comic> getDrawnComics() {
        return drawnComics;
    }

    public void setDrawnComics(Collection<Comic> drawnComics) {
        this.drawnComics = drawnComics;
    }

    public Collection<Comic> getWrittenComics() {
        return writtenComics;
    }

    public void setWrittenComics(Collection<Comic> writtenComics) {
        this.writtenComics = writtenComics;
    }

    public Collection<Comic> getCoverDrawnComics() {
        return coverDrawnComics;
    }

    public void setCoverDrawnComics(Collection<Comic> coverDrawnComics) {
        this.coverDrawnComics = coverDrawnComics;
    }

    public Collection<Comic> getColouredComics() {
        return colouredComics;
    }

    public void setColouredComics(Collection<Comic> colouredComics) {
        this.colouredComics = colouredComics;
    }

    public Collection<Comic> getInkedComics() {
        return inkedComics;
    }

    public void setInkedComics(Collection<Comic> inkedComics) {
        this.inkedComics = inkedComics;
    }
}
