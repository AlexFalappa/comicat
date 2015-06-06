package af.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A comic issue.
 * <p>
 * Class annotated for use with JAXB Object-XML mapping and JPA Object-Relational mapping.
 * <p>
 * Created by sasha on 23/05/15.
 */
@Entity
@Table(indexes = {
        @Index(columnList = "title", unique = true),
        @Index(columnList = "publishdate")
})
@NamedQueries({
        @NamedQuery(name = "Comic.findByTitle", query = "select c from Comic c where c.title=:title"),
        @NamedQuery(name = "Comic.findBySeries", query = "select c from Comic c where c.series.seriesName=:snm"),
        @NamedQuery(name = "Comic.findByPubDate", query = "select c from Comic c where c.publishDate=:pubdate"),
})
@XmlType(propOrder = {"title", "issue", "subTitle", "series", "seriesIssue", "frequency", "publishDate", "publisher", "genre", "pages", "price", "artBy","textBy","coverBy","inkBy","coloursBy", "country", "language", "notes"})
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private ComicType type;
    private String publisher;
    private Series series;
    private int seriesIssue;
    private String title;
    private String subTitle;
    private int issue;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    private Frequency frequency;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_art_author")
    private Set<Author> artBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_text_author")
    private Set<Author> textBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_cover_author")
    private Set<Author> coverBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_colours_author")
    private Set<Author> coloursBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "comic_ink_author")
    private Set<Author> inkBy = new HashSet<>(4);
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String country;
    private String language;
    private int pages;
    private BigDecimal price;
    @Lob
    private String notes;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] cover;
    @Version
    private Date lastUpdate;

    public Comic() {
    }

    public Comic(String title, ComicType type, int issue) {
        this.title = title;
        this.type = type;
        this.issue = issue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    @XmlElementWrapper
    @XmlElement(name = "author")
    public Set<Author> getInkBy() {
        return inkBy;
    }

    public void setInkBy(Set<Author> inkBy) {
        this.inkBy = inkBy;
    }

    @XmlElementWrapper
    @XmlElement(name = "author")
    public Set<Author> getColoursBy() {
        return coloursBy;
    }

    public void setColoursBy(Set<Author> coloursBy) {
        this.coloursBy = coloursBy;
    }

    @XmlElementWrapper
    @XmlElement(name = "author")
    public Set<Author> getCoverBy() {
        return coverBy;
    }

    public void setCoverBy(Set<Author> coverBy) {
        this.coverBy = coverBy;
    }

    @XmlElementWrapper
    @XmlElement(name = "author")
    public Set<Author> getTextBy() {
        return textBy;
    }

    public void setTextBy(Set<Author> textBy) {
        this.textBy = textBy;
    }

    @XmlElementWrapper
    @XmlElement(name = "author")
    public Set<Author> getArtBy() {
        return artBy;
    }

    public void setArtBy(Set<Author> artBy) {
        this.artBy = artBy;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(int issue) {
        this.issue = issue;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeriesIssue() {
        return seriesIssue;
    }

    public void setSeriesIssue(int seriesIssue) {
        this.seriesIssue = seriesIssue;
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

}
