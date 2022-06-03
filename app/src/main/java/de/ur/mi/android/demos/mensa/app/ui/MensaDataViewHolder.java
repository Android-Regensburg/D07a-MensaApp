package de.ur.mi.android.demos.mensa.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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
    private final LinearLayout emptyContainer;

    public MensaDataViewHolder(@NonNull View itemView) {
        super(itemView);
        dishCategoryText = itemView.findViewById(R.id.text_dish_category);
        dishNameText = itemView.findViewById(R.id.text_dish_name);
        emptyContainer = itemView.findViewById(R.id.empty);
    }

    /*
     *   Wird vom Adapter aufgerufen, um das übergeben MensaDish in diesem ViewHolder anzuzeigen.
     *   Dabei werden die Views des Layouts eines Eintrags mit den entsprechenden Texten (Kategorie
     *   und Name des Gerichts) befüllt.
     *   Falls aktuell kein Gericht in der Liste gespeichert ist zeigen wir stattdessen einen
     *   Ladebalken an, der dem leeren LinearLayout hinzugefügt wird.
     *   Das Linearlayout muss wieder geleert werden, wenn Informationen über ein Gericht verfügbar sind.
     */
    public void bindView(MensaDish dish, Context context) {
        if(dish == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View loadingView = inflater.inflate(R.layout.loading_card, emptyContainer, false);

            emptyContainer.addView(loadingView);

            dishCategoryText.setText("");
            dishNameText.setText("");

            return;
        }
        dishCategoryText.setText(dish.category.label);
        dishNameText.setText(dish.name);
        emptyContainer.removeAllViewsInLayout();
    }



}
