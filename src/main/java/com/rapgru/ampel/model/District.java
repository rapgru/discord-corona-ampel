package com.rapgru.ampel.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        District district = (District) o;
        return gkz == district.gkz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gkz);
    }
}
