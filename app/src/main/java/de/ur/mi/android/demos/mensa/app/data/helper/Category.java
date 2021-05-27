package de.ur.mi.android.demos.mensa.app.data.helper;

import java.util.Arrays;

public enum Category {
    MAIN(new String[]{"HG1", "HG2", "HG3"}, "Hauptgericht"),
    SIDE(new String[]{"B1", "B2", "B3", "B4"}, "Beilage"),
    DESSERT(new String[]{"N1", "N2"}, "Dessert");

    private final String[] matchingValues;
    public final String label;

    Category(String[] matchingValues, String label) {
        this.matchingValues = matchingValues;
        this.label = label;
        Arrays.sort(matchingValues);
    }

    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            int index = Arrays.binarySearch(category.matchingValues, value);
            if (index != -1) {
                return category;
            }
        }
        return null;
    }
}

