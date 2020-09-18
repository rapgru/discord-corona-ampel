package com.rapgru.ampel.model;

import java.awt.*;

public enum WarningColor {
    GREEN(Color.green, "gr\u00fcn", "green_circle"),
    YELLOW(Color.yellow, "gelb", "yellow_circle"),
    ORANGE(Color.orange, "orange", "orange_circle"),
    RED(Color.red, "rot", "red_circle");

    private final Color color;
    private final String text;
    private final String emoji;

    WarningColor(Color color, String text, String emoji) {
        this.color = color;
        this.text = text;
        this.emoji = emoji;
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public String getEmoji() {
        return emoji;
    }
}
