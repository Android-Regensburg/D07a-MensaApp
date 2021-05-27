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

public class WeeklyMenuRequest implements Response.Listener<String>, Response.ErrorListener {

    private static final String API_URL = "https://mensa.software-engineering.education/mensa/uni/$DAY";

    private final Context context;
    private final WeeklyMenuRequestListener listener;
    private final JSONArray results;
    private boolean wasStarted;
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
        RequestQueue queue = Volley.newRequestQueue(context);
        for (Weekday day : Weekday.values()) {
            StringRequest request = createVolleyRequestForWeekday(day, this, this);
            queue.add(request);
        }
        queue.start();
        wasStarted = true;
    }

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
        try {
            JSONArray resultsForOneDay = new JSONArray(response);
            for (int i = 0; i < resultsForOneDay.length(); i++) {
                results.put(resultsForOneDay.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            responseCounter++;
            notifyListenerIfReady();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        responseCounter++;
        notifyListenerIfReady();
    }

}
