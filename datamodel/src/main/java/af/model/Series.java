package af.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * A comic series.
 * <p>
 * Class annotated for use with JAXB Object-XML mapping and JPA Object-Relational mapping. Created by sasha on 23/05/15.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Series.findAll", query = "select s from Series s"),
    @NamedQuery(name = "Series.findByName", query = "select s from Series s where s.name=:name"),
    @NamedQuery(name = "Series.findByNameLike", query = "select s from Series s where s.name like concat('%',:name,'%')")
})
public class Series implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    public Series() {
    }

    public Series(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Series{%d: %s}", id, name);
    }
}
