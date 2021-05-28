package de.ur.mi.android.demos.mensa.app.data.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

/**
 * Diese Klasse repräsentiert einen einmalig verwendbaren Request an die HTTP-API um den vollständigen
 * Speiseplan der laufenden Woche herunterzuladen und als Array von JSON-Objekten zurückzugeben. Hier
 * wird das Volley-Framework verwendet um die fünf notwendigen HTTP-Anfragen an den Server zu stellen.
 * Die einzelnen Anworten des Servers werden in einem JSONArray zwischengespeichert. Durch das Zählen der
 * positiven und negativen Antworten des Servers kann festgestellt werden, wenn die letzte notwendige
 * Antwort des Server eingegangen ist. Achtung: Das funktioniert nur deshalb sicher, da die onReponse-Methode
 * der Volley-Listener automatisch im Main Thread (UI Thread) der Anwendung ausgeführt wird!
 */
public class WeeklyMenuRequest implements Response.Listener<String>, Response.ErrorListener {

    private static final String API_URL = "https://mensa.software-engineering.education/mensa/uni/$DAY";

    // Kontext der App, der zur Verwendung des Volley-Frameworks benötigt wird
    private final Context context;
    // Listener, der nach Abschluss aller Anfragen über das Ergebnis (Liste der erhaltenen Speisen) informiert wird
    private final WeeklyMenuRequestListener listener;
    // JSONArray zum Speichern der Inhalte der einzelnen Server-Antworten
    private final JSONArray results;
    // Gibt an, ob dieser Request bereits gestartet wurde
    private boolean wasStarted;
    // Zählt, wie viele der Anfragen an den API-Server bereits beantwortet wurden
    private int responseCounter;

    public WeeklyMenuRequest(Context context, WeeklyMenuRequestListener listener) {
        this.context = context;
        this.listener = listener;
        this.results = new JSONArray();
        this.wasStarted = false;
        this.responseCounter = 0;
    }

    public void start() throws RequestAlreadyStartedException {
        if (wasStarted) {
            throw new RequestAlreadyStartedException();
        }
        // Hier erstellen wir die Queue, in der wir die fünf notwendigen Anfragen (Montag bis Freitag) sammeln
        RequestQueue queue = Volley.newRequestQueue(context);
        for (Weekday day : Weekday.values()) {
            StringRequest request = createVolleyRequestForWeekday(day, this, this);
            queue.add(request);
        }
        // Hier wird die Queue gestartet: Volley beginnt mit der Ausführung der vorbereiteten Anfragen
        queue.start();
        // Ab diesem Zeitpunkt gilt der WeeklyMenuRequest als "vebraucht" und kann nicht noch einmal gestartet werden
        wasStarted = true;
    }

    /**
     * Erstellt einen StringeRequest (Volley) für das Erfragen der Speiseplandaten für den übergebenen Wochentag
     *
     * @param weekday         Wochentag, dessen Daten erfragt werden sollen
     * @param successListener Listener für den erfolgreichen Abschluss der Anfrage
     * @param errorListener   Listener für Fehler während der Anfrage
     * @return Der vorbereitete Request (noch nicht gestartet)
     */
    private StringRequest createVolleyRequestForWeekday(Weekday weekday, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        String url = API_URL.replace("$DAY", weekday.shortName);
        return new StringRequest(Request.Method.GET, url, successListener, errorListener);
    }

    private void notifyListenerIfReady() {
        if (responseCounter == Weekday.values().length) {
            listener.onDataRequestFinished(results);
        }
    }

    @Override
    public void onResponse(String response) {
        // Wenn eine der Serveranfragen beantwortet wurde ...
        try {
            // .. versuchen wir die JSON-formatierten Speisen aus dem erhaltenen Array auszulesen
            JSONArray resultsForOneDay = new JSONArray(response);
            for (int i = 0; i < resultsForOneDay.length(); i++) {
                // ... und als einzelne JSONObjekte in dem zentralen JSONArray zu speichern
                results.put(resultsForOneDay.getJSONObject(i));
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
        // Wir zählen auch die fehlerhaften Rückgaben, da unsere App sonst "ewig" auf den Abschluss ausstehender Request warten würde
        responseCounter++;
        notifyListenerIfReady();
    }

}
