package de.ur.mi.android.demos.mensa.app.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.ur.mi.android.demos.mensa.app.data.helper.Places;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

/**
 * Diese Klasse repräsentiert einen Request an die HTTP-API um den vollständigen Speiseplan
 * der laufenden Woche herunterzuladen und als Array von JSON-Objekten zurückzugeben. Hier
 * wird das Volley-Framework verwendet um die fünf notwendigen HTTP-Anfragen an den Server zu stellen.
 * Die einzelnen Anworten des Servers werden in einem JSONArray zwischengespeichert. Durch das Zählen der
 * positiven und negativen Antworten des Servers kann festgestellt werden, wenn die letzte notwendige
 * Antwort des Server eingegangen ist. Achtung: Das funktioniert nur deshalb sicher, da die onReponse-Methode
 * der Volley-Listener automatisch im Main Thread (UI Thread) der Anwendung ausgeführt wird!
 */
public class WeeklyMenuRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    // private static final String API_URL = "https://mensa.software-engineering.education/mensa/$PLACE/$DAY";
    private static final String API_URL = "https://mensa.v2.software-engineering.education/$PLACE/$DAY";

    // Kontext der App, der zur Verwendung des Volley-Frameworks benötigt wird
    private final Context context;
    // Listener, der nach Abschluss aller Anfragen über das Ergebnis (Liste der erhaltenen Speisen) informiert wird
    private final WeeklyMenuRequestListener listener;
    // JSONArray zum Speichern der Inhalte der einzelnen Server-Antworten
    private JSONArray results;
    // Gibt an, ob dieser Request bereits gestartet wurde
    private boolean wasStarted;
    // Zählt, wie viele der Anfragen an den API-Server bereits beantwortet wurden
    private int responseCounter;
    // Speichert für welchen Ort die Anfrage gestellt werden soll.
    private final Places currentPlace;

    public WeeklyMenuRequest(Context context, WeeklyMenuRequestListener listener, Places place) {
        this.context = context;
        this.listener = listener;
        this.results = new JSONArray();
        this.wasStarted = false;
        this.responseCounter = 0;
        this.currentPlace = place;
    }

    public void start() throws RequestAlreadyStartedException {
        if (wasStarted) {
            throw new RequestAlreadyStartedException();
        }
        // Hier erstellen wir die Queue, in der wir die fünf notwendigen Anfragen (Montag bis Freitag) sammeln
        RequestQueue queue = Volley.newRequestQueue(context);
        for (Weekday day : Weekday.values()) {
            JsonObjectRequest request = createVolleyRequestForWeekdayAndPlace(day, currentPlace, this, this);
            queue.add(request);
        }
        // Hier wird die Queue gestartet: Volley beginnt mit der Ausführung der vorbereiteten Anfragen
        queue.start();
        // Ab diesem Zeitpunkt gilt der WeeklyMenuRequest als "vebraucht" und kann nicht noch einmal gestartet werden
        wasStarted = true;
    }

    /**
     * Erstellt einen StringeRequest (Volley) für das Erfragen der Speiseplandaten für den übergebenen Wochentag
     * und den übergebenen Ort
     *
     * @param weekday         Wochentag, dessen Daten erfragt werden sollen
     * @param place           Ort für dessen Mensa Daten abgefragt werden sollen.
     * @param successListener Listener für den erfolgreichen Abschluss der Anfrage
     * @param errorListener   Listener für Fehler während der Anfrage
     * @return Der vorbereitete Request (noch nicht gestartet)
     */
    private JsonObjectRequest createVolleyRequestForWeekdayAndPlace(Weekday weekday, Places place, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        String url = API_URL.replace("$DAY", weekday.name);
        url = url.replace("$PLACE", place.code);
        return new JsonObjectRequest(url, successListener, errorListener);
    }


    private void notifyListenerIfReady() {
        if (responseCounter == Weekday.values().length) {
            responseCounter = 0;
            listener.onDataRequestFinished(results);
        }
    }

    /*
        Wenn eine Anfrage an die API beantwortet wurde wird versucht den String in ein JSON-Array umzuwandeln.
        Sobald für alle Wochenenden ein Array erzeugt werden konnte wird er Listener informiert, dass
        neue Daten verfügbar sind.
     */
    @Override
    public void onResponse(JSONObject response) {
        if (responseCounter == 0) {
            this.results = new JSONArray();
        }

        // Wenn eine der Serveranfragen beantwortet wurde ...
        try {
            // .. versuchen wir die JSON-formatierten Speisen aus dem erhaltenen Objekt auszulesen
            JSONArray dataArray = response.getJSONArray("data");
            if (dataArray.length() != 0) {
                for (int i = 0; i < dataArray.length(); i++) {
                    // ... und als einzelne JSONObjekte in dem zentralen JSONArray zu speichern
                    results.put(dataArray.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // Wir zählen diese Antwort ...
            responseCounter++;
            // ... und prüfen, ob nun alle Anfragen beantwortet wurden und wir den Listener über das Gesamtergebnis informieren können
            notifyListenerIfReady();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (responseCounter == 0) {
            this.results = new JSONArray();
        }
        // Wir zählen auch die fehlerhaften Rückgaben, da unsere App sonst "ewig" auf den Abschluss ausstehender Request warten würde
        responseCounter++;
        notifyListenerIfReady();
    }

}
