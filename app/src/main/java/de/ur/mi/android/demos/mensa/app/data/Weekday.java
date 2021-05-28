package de.ur.mi.android.demos.mensa.app.data;

public enum Weekday {
    MONDAY("mo"),
    TUESDAY("di"),
    WEDNESDAY("mi"),
    THURSDAY("do"),
    FRIDAY("fr");

    public final String shortName;

    Weekday(String shortName) {
        this.shortName = shortName;
    }
}
