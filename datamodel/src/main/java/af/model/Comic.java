package af.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * A comic issue.
 * <p>
 * Class annotated for use with JAXB Object-XML mapping and JPA Object-Relational mapping.
 * <p>
 * Created by sasha on 23/05/15.
 */
@Entity
@Table(indexes = {
    @Index(columnList = "title", unique = true)
})
@NamedQueries({
    @NamedQuery(name = "Comic.findAll", query = "select c from Comic c"),
    @NamedQuery(name = "Comic.findByTitle", query = "select c from Comic c where c.title=:title"),
    @NamedQuery(name = "Comic.findByTitleLike", query = "select c from Comic c where c.title like concat('%',:title,'%')"),
    @NamedQuery(name = "Comic.findBySeries", query = "select c from Comic c where c.series.name=:serName"),
    @NamedQuery(name = "Comic.findBySeriesLike", query = "select c from Comic c where c.series.name like concat('%',:serName,'%')"),
    @NamedQuery(name = "Comic.findByGenre", query = "select c from Comic c where c.genre=:genre")
})
@XmlType(propOrder = {"title", "subTitle", "series", "seriesIssue", "frequency", "publisher", "genre", "country", "language", "notes", "issues"})
public class Comic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private ComicType type;
    private String publisher;
    @OneToOne()
    private Series series;
    private String title;
    private Frequency frequency;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String country;
    private String lang;
    @Lob
    private String notes;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_art_author")
    private Set<Author> artBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_text_author")
    private Set<Author> textBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_colours_author")
    private Set<Author> coloursBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_ink_author")
    private Set<Author> inkBy = new HashSet<>(4);
    @OneToMany(mappedBy = "comic", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<ComicIssue> issues = new ArrayList<>();
    @Version
    private Timestamp lastUpdate;

    public Comic() {
    }

    public Comic(String title, ComicType type) {
        this.title = title;
        this.type = type;
    }

    @XmlElementWrapper
    @XmlElement(name = "issue")
    public List<ComicIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ComicIssue> issues) {
        this.issues = issues;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @XmlAttribute
    public ComicType getType() {
        return type;
    }

    public void setType(ComicType type) {
        this.type = type;
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public Set<Author> getArtBy() {
        return artBy;
    }

    public void setArtBy(Set<Author> artBy) {
        this.artBy = artBy;
    }

    public Set<Author> getTextBy() {
        return textBy;
    }

    public void setTextBy(Set<Author> textBy) {
        this.textBy = textBy;
    }

    public Set<Author> getColoursBy() {
        return coloursBy;
    }

    public void setColoursBy(Set<Author> coloursBy) {
        this.coloursBy = coloursBy;
    }

    public Set<Author> getInkBy() {
        return inkBy;
    }

    public void setInkBy(Set<Author> inkBy) {
        this.inkBy = inkBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toString() {
        return String.format("Comic{%d: %s}", id, title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comic comic = (Comic) o;
        return !(title != null ? !title.equals(comic.title) : comic.title != null);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
