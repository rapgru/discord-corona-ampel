package com.rapgru.ampel.model;

public class District {

    private final int gkz;
    private final String name;

    public District(int gkz, String name) {
        this.gkz = gkz;
        this.name = name;
    }

    public int getGkz() {
        return gkz;
    }

    public String getName() {
        return name;
    }
}
