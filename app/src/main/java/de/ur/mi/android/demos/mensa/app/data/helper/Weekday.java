package de.ur.mi.android.demos.mensa.app.data.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Dieses Enum repräsentiert die in der App relevanten Wochentage. Statt der Verwendung von z.B.
 * DayOfWeek aus dem java.time-Paket wird ein eigenes Enum verwendet, um den Wochentagen das jeweilige
 * Kürzel ("shortName") zuzuordnen, das in dem vom API-Server erhaltenen JSON-String zur Kennzeichnung
 * der Wochentage verwendet wird.
 */
public enum Weekday {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday");

    public final String name;

    Weekday(String name) {
        this.name = name;
    }

    // Gibt den Wochentag zurück, der durch das übergebene Kürzel repräsentiert wird
    public static Weekday fromShortName(String shortName) {
        for (Weekday day : Weekday.values()) {
            if (day.name.equals(shortName)) {
                return day;
            }
        }
        return null;
    }

    // Gibt den Wochentag zurück, der den aktuellen Tag repräsentiert. Wird die Methode am Wochenende
    // aufgerufen, wird der letzte Werktag der laufenden Woche (Freitag) zurückgegeben
    public static Weekday currentOrNearest() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (today.ordinal() < 5) {
            return Weekday.values()[today.ordinal()];
        }
        return Weekday.FRIDAY;
    }
}
