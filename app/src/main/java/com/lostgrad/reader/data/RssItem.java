package com.lostgrad.reader.data;

/**
 * Created by graeme on 02/04/14.
 */
public class RssItem {

    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return title;
    }
}
