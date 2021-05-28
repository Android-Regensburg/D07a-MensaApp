package de.ur.mi.android.demos.mensa.app.data;

public enum Category {

    MAIN("Hauptgericht"),
    SIDE("Beilage"),
    DESSERT("Nachtisch");

    public final String label;

    Category(String label) {
        this.label = label;
    }

}
