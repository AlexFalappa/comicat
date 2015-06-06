package af.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * An issue of a Comic.
 * Created by sasha on 06/06/15.
 */
@Entity
@Table(indexes = {
        @Index(columnList = "publishdate")
})
@XmlType(propOrder = {"number", "publishDate", "pages", "price", "artBy","textBy","coverBy","inkBy","coloursBy"})
public class ComicIssue {
    @Id
    private int number;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    @ManyToOne
    private Comic comic;
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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] cover;
    @Version
    private Date lastUpdate;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
