package com.rapgru.ampel.model;

public class DistrictChange {

    private final WarningColor from;
    private final WarningColor to;

    private final DistrictChangeDirection direction;

    private final DistrictData dataPoint;

    public DistrictChange(WarningColor from, WarningColor to, DistrictChangeDirection direction, DistrictData dataPoint) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.dataPoint = dataPoint;
    }

    public WarningColor getFrom() {
        return from;
    }

    public WarningColor getTo() {
        return to;
    }

    public DistrictChangeDirection getDirection() {
        return direction;
    }

    public DistrictData getDataPoint() {
        return dataPoint;
    }

    @Override
    public String toString() {
        return "DistrictChange{" +
                "from=" + from +
                ", to=" + to +
                ", direction=" + direction +
                ", dataPoint=" + dataPoint +
                '}';
    }
}
