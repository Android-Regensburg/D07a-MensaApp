package de.ur.mi.android.demos.mensa.app.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import de.ur.mi.android.demos.mensa.app.data.api.WeeklyMenuRequest;

public class MensaDataProvider implements WeeklyMenuRequest.WeeklyMenuRequestListener {

    private Context context;

    public MensaDataProvider(Context context) {
        this.context = context;
    }

    public void update() {
        WeeklyMenuRequest updateTask = new WeeklyMenuRequest(context, this::onDataRequestFinished);
        try {
            updateTask.start();
        } catch (WeeklyMenuRequest.RequestAlreadyStartedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRequestFinished(JSONArray results) {
        Log.d("MENSA_APP", "in: onDataRequestFinished");
        for (int i = 0; i < results.length(); i++) {
            try {
   
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
