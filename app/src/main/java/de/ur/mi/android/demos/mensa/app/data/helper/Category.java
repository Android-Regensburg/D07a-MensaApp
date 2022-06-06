package de.ur.mi.android.demos.mensa.app.data.helper;

import java.util.Arrays;

/**
 * Dieses Enum bildet die drei aktuell relevanten Speisekategorien der Uni-Mensa an. Im JSON-String
 * des API-Servers werden die einzelnen Speisen über verschiedene Werte des "category"-Felds einer
 * der Kategorien zugeordnet. Die zugehörigen Werte bzw. Labels werden für jede Kategorie über die
 * Einträge des "matchingValues"-Array definiert. Zusätzlich wird jeder Kategorie ein allgemeines
 * Label zugeordnet, das zu textuellen Repräsentation der Kategorie im UI verwendet werden kann.
 */
public enum Category {
    // Hauptgericht
    MAIN(new String[]{"HG1", "HG2", "HG3", "HG4"}, "Hauptgericht"),
    // Beilage
    SIDE(new String[]{"B1", "B2", "B3", "B4", "B5", "Suppe"}, "Beilage"),
    // Dessert/Nachtisch
    DESSERT(new String[]{"N1", "N2", "N3"}, "Dessert");

    private final String[] matchingValues;
    public final String label;

    Category(String[] matchingValues, String label) {
        this.matchingValues = matchingValues;
        this.label = label;
        Arrays.sort(this.matchingValues);
    }

    // Gibt die Kategorie zurück, deren "matchingValues"-Array den übergebenen Wert beinhaltet
    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            int index = Arrays.binarySearch(category.matchingValues, value);
            if (index > -1) {
                return category;
            }
        }
        return SIDE;
    }
}

