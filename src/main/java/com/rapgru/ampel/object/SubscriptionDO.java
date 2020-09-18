package com.rapgru.ampel.object;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="t_subscription")
public class SubscriptionDO {

    private long id;

    private String date;

    private String userId;

    private int gkz;

    public SubscriptionDO() {}

    public SubscriptionDO(String date, String userId, int gkz) {
        this.date = date;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGkz() {
        return gkz;
    }

    public void setGkz(int gkz) {
        this.gkz = gkz;
    }
}
