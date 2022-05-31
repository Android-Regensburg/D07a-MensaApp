package de.ur.mi.android.demos.mensa.app.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.ur.mi.android.demos.mensa.app.R;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;

/**
 * ViewHolder zur Darstellung eines einzelnen MensaDish-Objektes im UI
 */
public class MensaDataViewHolder extends RecyclerView.ViewHolder {

    private final TextView dishCategoryText;
    private final TextView dishNameText;

    public MensaDataViewHolder(@NonNull View itemView) {
        super(itemView);
        dishCategoryText = itemView.findViewById(R.id.text_dish_category);
        dishNameText = itemView.findViewById(R.id.text_dish_name);
    }

    /*
     *   Wird vom Adapter aufgerufen, um das übergeben MensaDish in diesem ViewHolder anzuzeigen.
     *   Dabei werden die Views des Layouts eines Eintrags mit den entsprechenden Texten (Kategorie
     *   und Name des Gerichts) befüllt.
     */
    public void bindView(MensaDish dish) {
        dishCategoryText.setText(dish.category.label);
        dishNameText.setText(dish.name);
    }

}
