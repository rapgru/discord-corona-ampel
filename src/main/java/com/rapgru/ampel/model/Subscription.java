package com.rapgru.ampel.model;

import java.time.Instant;

public class Subscription {

    private long id;

    private final Instant date;

    private final String userId;

    private final int gkz;

    public Subscription(long id, Instant date, String userId, int gkz) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.gkz = gkz;
    }

    public Instant getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public int getGkz() {
        return gkz;
    }


    public long getId() {
        return id;
    }
}
