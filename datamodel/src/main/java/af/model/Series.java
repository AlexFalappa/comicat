package af.model;

import javax.persistence.Embeddable;

/**
 * A comic series.
 * Created by sasha on 23/05/15.
 */
@Embeddable
public class Series {
    String seriesName;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String name) {
        this.seriesName = name;
    }
}
