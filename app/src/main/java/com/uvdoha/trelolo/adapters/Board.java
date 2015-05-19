package com.uvdoha.trelolo.adapters;

/**
 * Created by dmitry on 19.05.15.
 */
public class Board {

    private String title;
    private String id;

    public Board(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
