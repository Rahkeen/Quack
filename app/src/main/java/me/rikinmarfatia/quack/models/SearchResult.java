package me.rikinmarfatia.quack.models;

/**
 * Holds search result data.
 *
 * @author Rikin (rikinm10@gmail.com)
 */
public class SearchResult {
    private String text;
    private String url;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
