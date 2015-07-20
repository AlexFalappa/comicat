package af.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * A comic author. Created by sasha on 23/05/15.
 */
@Entity
@Table(indexes = {
    @Index(columnList = "name,surname", unique = true),})
@NamedQueries({
    @NamedQuery(name = "Author.findAll", query = "select a from Author a"),
    @NamedQuery(name = "Author.findByName", query = "select a from Author a where a.name=:nm"),
    @NamedQuery(name = "Author.findBySurname", query = "select a from Author a where a.surname=:surnm"),
    @NamedQuery(name = "Author.findByNameSurname", query = "select a from Author a where a.name=:nm and a.surname=:surnm")
})
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String surname;
    @ManyToMany(mappedBy = "artBy")
    private Collection<ComicIssue> drawnComics;
    @ManyToMany(mappedBy = "textBy")
    private Collection<ComicIssue> writtenComics;
    @ManyToMany(mappedBy = "coverBy")
    private Collection<ComicIssue> coverDrawnComics;
    @ManyToMany(mappedBy = "coloursBy")
    private Collection<ComicIssue> colouredComics;
    @ManyToMany(mappedBy = "inkBy")
    private Collection<ComicIssue> inkedComics;

    // needed by JPA
    protected Author() {
    }

    public Author(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Author name can't be null");
        }
        this.name = name;
    }

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public String getNameSurname() {
        StringBuilder sb = new StringBuilder(name);
        if (surname != null) {
            sb.append(' ').append(surname);
        }
        return sb.toString();
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

    public Collection<ComicIssue> getDrawnComics() {
        return drawnComics;
    }

    public void setDrawnComics(Collection<ComicIssue> drawnComics) {
        this.drawnComics = drawnComics;
    }

    public Collection<ComicIssue> getWrittenComics() {
        return writtenComics;
    }

    public void setWrittenComics(Collection<ComicIssue> writtenComics) {
        this.writtenComics = writtenComics;
    }

    public Collection<ComicIssue> getCoverDrawnComics() {
        return coverDrawnComics;
    }

    public void setCoverDrawnComics(Collection<ComicIssue> coverDrawnComics) {
        this.coverDrawnComics = coverDrawnComics;
    }

    public Collection<ComicIssue> getColouredComics() {
        return colouredComics;
    }

    public void setColouredComics(Collection<ComicIssue> colouredComics) {
        this.colouredComics = colouredComics;
    }

    public Collection<ComicIssue> getInkedComics() {
        return inkedComics;
    }

    public void setInkedComics(Collection<ComicIssue> inkedComics) {
        this.inkedComics = inkedComics;
    }

    @Override
    public String toString() {
        return getNameSurname();
    }
}
