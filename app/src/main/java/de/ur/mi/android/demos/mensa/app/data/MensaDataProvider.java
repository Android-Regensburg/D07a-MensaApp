package de.ur.mi.android.demos.mensa.app.data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.data.api.RequestAlreadyStartedException;
import de.ur.mi.android.demos.mensa.app.data.api.WeeklyMenuRequest;
import de.ur.mi.android.demos.mensa.app.data.api.WeeklyMenuRequestListener;
import de.ur.mi.android.demos.mensa.app.data.helper.Places;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

public class MensaDataProvider implements WeeklyMenuRequestListener {

    private final Context context;
    private final MensaDataListener listener;
    private ArrayList<MensaDish> currentMenu;
    private WeeklyMenuRequest task;

    public MensaDataProvider(Context context, MensaDataListener listener) {
        this.context = context;
        this.listener = listener;
        this.currentMenu = new ArrayList<>();
    }

    public void update() {
        task = new WeeklyMenuRequest(context, this);
        try {
            task.start();
        } catch (RequestAlreadyStartedException e) {
            e.printStackTrace();
        }
    }

    public void getMenuForPlace(Places place) {
        // currentMenu = new ArrayList<>();
        task.restartForNewPlace(place);
    }

    public ArrayList<MensaDish> getMenuForDay(Weekday day) {
        ArrayList<MensaDish> menu = new ArrayList<>();
        for (MensaDish dish : currentMenu) {
            if (dish.onDay == day) {
                menu.add(dish);
            }
        }
        return menu;
    }

    @Override
    public void onDataRequestFinished(JSONArray results) {
        currentMenu = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            try {
                MensaDish dish = MensaDish.fromJSONObject(results.getJSONObject(i));
                currentMenu.add(dish);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listener.onMensaDataUpdated();
    }
}
