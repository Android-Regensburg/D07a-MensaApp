package de.ur.mi.android.demos.mensa.app.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.ur.mi.android.demos.mensa.app.R;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;


public class MensaDataViewHolder extends RecyclerView.ViewHolder {

    private final TextView dishCategoryText;
    private final TextView dishNameText;

    public MensaDataViewHolder(@NonNull View itemView) {
        super(itemView);
        dishCategoryText = itemView.findViewById(R.id.text_dish_category);
        dishNameText = itemView.findViewById(R.id.text_dish_name);
    }

    public void bindView(MensaDish dish) {
        dishCategoryText.setText(dish.category.label);
        dishNameText.setText(dish.name);
    }

}
