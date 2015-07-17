package af.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * An issue of a Comic.
 *
 * <p>
 * Class annotated for use with JAXB Object-XML mapping and JPA Object-Relational mapping. ComicIssues are persisted and removed in cascade
 * with Comics.</p>
 *
 * <p>
 * Created by sasha on 06/06/15.</p>
 */
@Entity
@IdClass(ComicIssueKey.class)
@Table(indexes = {
    @Index(columnList = "publishdate")
})
@NamedQueries({
    @NamedQuery(name = "ComicIssue.findByComic", query = "select i from ComicIssue i where i.comic.id=:comicId"),
    @NamedQuery(name = "ComicIssue.findByComicAndNumber", query = "select i from ComicIssue i where i.comic.id=:comicId and i.number=:num"),
    @NamedQuery(name = "ComicIssue.findByDate", query = "select i from ComicIssue i where i.publishDate=:pubDate")
})
@XmlType(propOrder = {"number", "publishDate", "pages", "price", "artBy", "textBy", "coverBy", "inkBy", "coloursBy"})
public class ComicIssue {

    @Id
    @ManyToOne
    private Comic comic;
    @Id
    private int number;
    private Integer seriesNumber;
    private String subTitle;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "issue_art_author")
    private Set<Author> artBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "issue_text_author")
    private Set<Author> textBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "issue_cover_author")
    private Set<Author> coverBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "issue_colours_author")
    private Set<Author> coloursBy = new HashSet<>(4);
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "issue_ink_author")
    private Set<Author> inkBy = new HashSet<>(4);
    private Integer pages;
    private BigDecimal price;
    @Column(length = 500)
    private String location;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] coverFull;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] coverThumb;
    @Version
    private Timestamp lastUpdate;

    public ComicIssue() {
    }

    public ComicIssue(int number) {
        this.number = number;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
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

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @XmlAttribute
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(Integer seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @XmlTransient
    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getCoverFull() {
        return coverFull;
    }

    public void setCoverFull(byte[] cover) {
        this.coverFull = cover;
    }

    public byte[] getCoverThumb() {
        return coverThumb;
    }

    public void setCoverThumb(byte[] coverThumb) {
        this.coverThumb = coverThumb;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toString() {
        return String.format("ComicIssue{%d of %s}", number, comic);
    }
}
