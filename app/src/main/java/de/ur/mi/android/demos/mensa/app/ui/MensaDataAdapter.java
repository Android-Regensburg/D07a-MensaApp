package de.ur.mi.android.demos.mensa.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.R;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;

/**
 * Adapter zur Anbindung einer Liste von MensaDish-Objekten an einen RecyclerView
 */
public class MensaDataAdapter extends RecyclerView.Adapter<MensaDataViewHolder> {

    private ArrayList<MensaDish> currentMenu;

    public MensaDataAdapter() {
        currentMenu = new ArrayList<>();
    }

    public void setMenu(ArrayList<MensaDish> menu) {
        this.currentMenu = new ArrayList<>(menu);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MensaDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dish_item, parent, false);
        return new MensaDataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MensaDataViewHolder holder, int position) {
        MensaDish dish = currentMenu.get(position);
        holder.bindView(dish);
    }

    @Override
    public int getItemCount() {
        return currentMenu.size();
    }
}
