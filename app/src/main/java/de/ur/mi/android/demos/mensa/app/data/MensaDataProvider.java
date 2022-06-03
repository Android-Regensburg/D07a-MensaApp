package de.ur.mi.android.demos.mensa.app.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.data.api.RequestAlreadyStartedException;
import de.ur.mi.android.demos.mensa.app.data.api.WeeklyMenuRequest;
import de.ur.mi.android.demos.mensa.app.data.api.WeeklyMenuRequestListener;
import de.ur.mi.android.demos.mensa.app.data.helper.Places;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

/**
 * Diese Klasse kapselt die Zugriffe auf die API und stellt die Daten der restlichen Anwendung
 * bereit.
 */
public class MensaDataProvider implements WeeklyMenuRequestListener {

    private final Context context;
    // Wird informiert, wenn neue Daten geladen wurden.
    private final MensaDataListener listener;
    // Beinhaltet alle Gerichte die gerade angeboten werden.
    private ArrayList<MensaDish> currentMenu;

    public MensaDataProvider(Context context, MensaDataListener listener) {
        this.context = context;
        this.listener = listener;
        this.currentMenu = new ArrayList<>();
    }

    // Wird aufgerufen, um die neuesten Daten aus der API zu laden.
    public void initialRequest() {
        getMenuForPlace(Places.UNI_REGENSBURG);
    }

    // Wird aufgerufen, um neue Daten für einen neuen Ort aus der API zu laden.
    public void getMenuForPlace(Places place) {
        WeeklyMenuRequest task = new WeeklyMenuRequest(context, this, place);
        try {
            task.start();
        } catch (RequestAlreadyStartedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt alle Gerichte zurück, deren Datum dem übergebenen Tag entsprechen.
     *
     * @param day der Wochentag für den Gerichte zurückgegeben werden sollen
     * @return ArrayList mit allen Gerichten, die am übergebenen Tag angeboten werden.
      */
    public ArrayList<MensaDish> getMenuForDay(Weekday day) {
        ArrayList<MensaDish> menu = new ArrayList<>();
        for (MensaDish dish : currentMenu) {
            if (dish.onDay == day) {
                menu.add(dish);
            }
        }
        return menu;
    }

    /*
        Wenn Daten von der API geladen wurden wird die ArrayList der aktuell gespeicherten Gerichte
        ersetzt. Dazu werden aus den geladenen JSONs Objekte vom Typ Dish erzeugt. Nach Abschluss
        aller Umwandlungen wird der Listener informiert, dass neue Gerichte angezeigt werden können.
     */
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
