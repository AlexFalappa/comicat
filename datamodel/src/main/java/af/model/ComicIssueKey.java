package af.model;

import java.io.Serializable;

/**
 * Key for ComicIssue.
 * Created by sasha on 06/06/15.
 */
public class ComicIssueKey implements Serializable {
    private Comic comic;
    private int number;

    public ComicIssueKey() {
    }

    public ComicIssueKey(Comic comic, int number) {
        this.comic = comic;
        this.number = number;
    }

    public Comic getComic() {
        return comic;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComicIssueKey that = (ComicIssueKey) o;
        if (number != that.number) return false;
        return comic.getTitle().equals(that.comic.getTitle());
    }

    @Override
    public int hashCode() {
        int result = comic.hashCode();
        result = 31 * result + number;
        return result;
    }
}
