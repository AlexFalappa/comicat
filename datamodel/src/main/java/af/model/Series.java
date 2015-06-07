package af.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A comic series.
 * <p>
 * Class annotated for use with JAXB Object-XML mapping and JPA Object-Relational mapping.
 * Created by sasha on 23/05/15.
 */
@Entity
public class Series {
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
}
