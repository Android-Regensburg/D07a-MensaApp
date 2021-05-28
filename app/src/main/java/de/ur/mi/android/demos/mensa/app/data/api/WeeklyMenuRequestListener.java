package de.ur.mi.android.demos.mensa.app.data.api;

import org.json.JSONArray;

/**
 * Interface für Listener, die über den Abschluss eines WeeklyMenuRequest informiert werden sollen
 */
public interface WeeklyMenuRequestListener {
    /**
     * Wird aufgerufen, wenn der Request vollständig abgeschlossen wurde
     *
     * @param results Alle vom Server erhaltenen Daten als Array von JSON-Objekten
     */
    void onDataRequestFinished(JSONArray results);
}
