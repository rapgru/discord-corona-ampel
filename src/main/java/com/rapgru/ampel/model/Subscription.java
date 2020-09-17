package com.rapgru.ampel.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

public class Subscription {

    private final Instant date;

    private final String username;

    private final int gkz;

    public Subscription(Instant date, String username, int gkz) {
        this.date = date;
        this.username = username;
        this.gkz = gkz;
    }

    public Instant getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public int getGkz() {
        return gkz;
    }
}
