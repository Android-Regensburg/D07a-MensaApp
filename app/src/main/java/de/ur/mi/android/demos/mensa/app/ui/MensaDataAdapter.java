package de.ur.mi.android.demos.mensa.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.R;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;

/**
 * Adapter zur Anbindung einer Liste von MensaDish-Objekten an einen RecyclerView.
 */
public class MensaDataAdapter extends RecyclerView.Adapter<MensaDataViewHolder> {

    // In dieser ArrayList werden die aktuell anzuzeigenden Gerichte gespeichert.
    private ArrayList<MensaDish> currentMenu;

    private Context context;

    public MensaDataAdapter(Context context) {
        currentMenu = new ArrayList<>();
        this.context = context;
    }

    // Mit dieser Methode kann die Liste der aktuellen Gerichte ausgetauscht werden.
    public void setMenu(ArrayList<MensaDish> menu) {
        this.currentMenu = new ArrayList<>(menu);
        this.notifyDataSetChanged();
    }

    // Erzuegt einen ViewHolder, der das Layout eines Eintrags befüllt.
    @NonNull
    @Override
    public MensaDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dish_item, parent, false);
        return new MensaDataViewHolder(v);
    }

    // Diese Methode wird automatisch aufgerufen und fügt ein Gericht in die Liste ein.
    @Override
    public void onBindViewHolder(@NonNull MensaDataViewHolder holder, int position) {
        if (currentMenu.size() > 0) {
            MensaDish dish = currentMenu.get(position);
            holder.bindView(dish, context);
            return;
        }

        // Anstatt im Falle einer leeren Liste nichts anzuzeigen, zeigen wir einen Ladebalken an.
        holder.bindView(null, context);
    }

    // Informiert Android wie viele Karten anzuzeigen sind.
    @Override
    public int getItemCount() {
        if (currentMenu.size() == 0) return 1;
        return currentMenu.size();
    }
}
