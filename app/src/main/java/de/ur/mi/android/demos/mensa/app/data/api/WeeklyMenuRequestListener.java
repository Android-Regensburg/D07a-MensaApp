package de.ur.mi.android.demos.mensa.app.data.api;

import org.json.JSONArray;

public interface WeeklyMenuRequestListener {
    void onDataRequestFinished(JSONArray results);
}
