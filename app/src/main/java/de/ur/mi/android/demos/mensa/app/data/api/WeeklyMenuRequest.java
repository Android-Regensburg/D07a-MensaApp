package de.ur.mi.android.demos.mensa.app.data.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

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
public class WeeklyMenuRequest implements Response.Listener<String>, Response.ErrorListener {

    private static final String API_URL = "https://mensa.software-engineering.education/mensa/$PLACE/$DAY";

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

    private boolean finished;

    private String currentPlace;

    public WeeklyMenuRequest(Context context, WeeklyMenuRequestListener listener) {
        this.context = context;
        this.listener = listener;
        this.results = new JSONArray();
        this.wasStarted = false;
        this.finished = false;
        this.responseCounter = 0;
        this.currentPlace = Places.UNI_REGENSBURG.getCode();
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

    /*
        Diese Methode kann verwendet werden, wenn aktuell keine Anfrage läuft, eine neue Anfrage für den
        übergebenen Ort zu starten.
        Die Methode wird daher aufgerufen, wenn im Drawer Menü ein Eintrag angeklickt wird.
        Nach Abschluss der Anfrage wird der Listener informiert.
     */
    public void restartForNewPlace(Places place) {
        if(wasStarted && finished) {
            finished = false;
            // Hier erstellen wir die Queue, in der wir die fünf notwendigen Anfragen (Montag bis Freitag) sammeln
            RequestQueue queue = Volley.newRequestQueue(context);
            for (Weekday day : Weekday.values()) {
                StringRequest request = createVolleyRequestForWeekdayAndPlace(day, place, this, this);
                queue.add(request);
            }
            // Hier wird die Queue gestartet: Volley beginnt mit der Ausführung der vorbereiteten Anfragen
            queue.start();
        }
    }

    /**
     * Erstellt einen StringeRequest (Volley) für das Erfragen der Speiseplandaten für den übergebenen Wochentag.
     * Standardmäßig wird die Uni Regensburg verwendet.
     *
     * @param weekday         Wochentag, dessen Daten erfragt werden sollen
     * @param successListener Listener für den erfolgreichen Abschluss der Anfrage
     * @param errorListener   Listener für Fehler während der Anfrage
     * @return Der vorbereitete Request (noch nicht gestartet)
     */
    private StringRequest createVolleyRequestForWeekday(Weekday weekday, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        String url = API_URL.replace("$DAY", weekday.shortName);
        url = url.replace("$PLACE", Places.UNI_REGENSBURG.code);
        return new StringRequest(Request.Method.GET, url, successListener, errorListener);
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
    private StringRequest createVolleyRequestForWeekdayAndPlace(Weekday weekday, Places place, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        String url = API_URL.replace("$DAY", weekday.shortName);
        url = url.replace("$PLACE", place.code);
        return new StringRequest(Request.Method.GET, url, successListener, errorListener);
    }


    private void notifyListenerIfReady() {
        if (responseCounter == Weekday.values().length) {
            finished = true;
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
    public void onResponse(String response) {
        if (responseCounter == 0) {
            this.results = new JSONArray();
        }

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
        if (responseCounter == 0) {
            this.results = new JSONArray();
        }
        // Wir zählen auch die fehlerhaften Rückgaben, da unsere App sonst "ewig" auf den Abschluss ausstehender Request warten würde
        responseCounter++;
        notifyListenerIfReady();
    }

}
