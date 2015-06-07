package af.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
})
@NamedQueries({
        @NamedQuery(name = "Comic.findByTitle", query = "select c from Comic c where c.title=:title"),
        @NamedQuery(name = "Comic.findBySeries", query = "select c from Comic c where c.series.name=:snm"),
})
@XmlType(propOrder = {"title", "subTitle", "series", "seriesIssue", "frequency", "publisher", "genre", "country", "language", "notes", "issues"})
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private ComicType type;
    private String publisher;
    @OneToOne()
    private Series series;
    private Integer seriesIssue;
    private String title;
    private String subTitle;
    private Frequency frequency;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String country;
    private String language;
    @Lob
    private String notes;
    @OneToMany(mappedBy = "comic")
    private List<ComicIssue> issues = new ArrayList<>();
    @Version
    private Date lastUpdate;

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

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
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

    public Integer getSeriesIssue() {
        return seriesIssue;
    }

    public void setSeriesIssue(Integer seriesIssue) {
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
