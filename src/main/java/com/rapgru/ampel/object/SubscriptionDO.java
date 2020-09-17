package com.rapgru.ampel.object;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_subscription")
public class SubscriptionDO {

    private long id;

    private String date;

    private String username;

    private int gkz;

    public SubscriptionDO() {}

    public SubscriptionDO(String date, String username, int gkz) {
        this.date = date;
        this.username = username;
        this.gkz = gkz;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGkz() {
        return gkz;
    }

    public void setGkz(int gkz) {
        this.gkz = gkz;
    }
}
