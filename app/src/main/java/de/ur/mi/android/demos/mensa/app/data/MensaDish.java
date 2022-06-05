package de.ur.mi.android.demos.mensa.app.data;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import de.ur.mi.android.demos.mensa.app.data.helper.Category;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

/**
 * Repräsentiert einen einzelnen Eintrag des (wöchentlichen) Speiseplans
 */
public class MensaDish {
    // Name des Gerichts
    public final String name;
    // Wochentag, an dem die Speise angeboten wird
    public final Weekday onDay;
    // Kategorie (Hauptspeise, Beilage oder Dessert)
    public final Category category;

    private MensaDish(String name, Weekday onDay, Category category) {
        this.name = name;
        this.onDay = onDay;
        this.category = category;
    }

    /**
     * Versucht, aus dem übergebenen JSONObject die notwendigen Werte zum Erstellen einer MensaDish-
     * Instanz auszulesen und ein entsprechendes Objekt zurückzugeben.
     *
     * @param object Das JSONObject, in dem die Informationen für das neu zu erstellende MensaDish-Objekt enthalten sind
     * @return Das MensaDish-Objekt, das die Informationen aus dem übergebenen JSON-Objekt repräsentiert
     * @throws JSONException
     */
    public static MensaDish fromJSONObject(JSONObject object) throws JSONException {
        String name = object.getString("name");
        String dayFromJSON = object.getString("day");
        Weekday onDay = Weekday.fromShortName(dayFromJSON);
        String categoryFromJSON = object.getString("category");
        Log.d("MensaCategory", "Category: " + categoryFromJSON);
        Category category = Category.fromValue(categoryFromJSON);
        Log.d("MensaCategory", "Category name: " + category.label);
        return new MensaDish(name, onDay, category);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name of Dish: " +
                name +
                "\n" +
                "Day of Dish: " +
                onDay.name +
                "\n" +
                "Category of Dish: " +
                category.label;
    }
}
