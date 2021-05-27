package de.ur.mi.android.demos.mensa.app.data.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;

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

    public static Weekday fromShortName(String shortName) {
        for (Weekday day : Weekday.values()) {
            if (day.shortName.equals(shortName.toLowerCase())) {
                return day;
            }
        }
        return null;
    }

    public static Weekday currentOrNearest() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (today.ordinal() < 6) {
            return Weekday.values()[today.ordinal()];
        }
        return Weekday.FRIDAY;
    }
}
