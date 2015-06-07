package af.model;

import javax.persistence.*;

/**
 * A comic series.
 * Created by sasha on 23/05/15.
 */
@Entity
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String seriesName;

    public Series() {
    }

    public Series(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String name) {
        this.seriesName = name;
    }
}
