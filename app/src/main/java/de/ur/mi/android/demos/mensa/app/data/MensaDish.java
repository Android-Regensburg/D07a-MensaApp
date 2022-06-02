package de.ur.mi.android.demos.mensa.app.data;

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

    public MensaDish(String name, Weekday onDay, Category category) {
        this.name = name;
        this.onDay = onDay;
        this.category = category;
    }

    /**
     * Versucht, aus dem übergebenen JSONObject die notwendigen Werte zum Erstellen einer MensaDish-
     * Instanz auszulesen und ein entsprechendes Objekt zurückzugeben.
     */
    public static MensaDish fromJSONObject(JSONObject object) throws JSONException {
        // Implementieren Sie hier das Auslesen der Daten aus dem JSON Objekt.
        // Erzeugen Sie mit den ausgelesenen Daten ein neues Objekt der Klasse MensaDish und geben Sie dieses zurück.
        return null;
    }
}
