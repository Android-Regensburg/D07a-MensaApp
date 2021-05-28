package de.ur.mi.android.demos.mensa.app.data;

public class MensaDish {

    public final String name;
    public final Category category;
    public final Weekday onDay;

    public MensaDish(String name, Category category, Weekday onDay) {
        this.name = name;
        this.category = category;
        this.onDay = onDay;
    }

}
