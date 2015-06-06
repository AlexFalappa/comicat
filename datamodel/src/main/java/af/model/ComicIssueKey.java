package af.model;

import java.io.Serializable;

/**
 * Key for ComicIssue.
 * Created by sasha on 06/06/15.
 */
public class ComicIssueKey implements Serializable{
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
}
