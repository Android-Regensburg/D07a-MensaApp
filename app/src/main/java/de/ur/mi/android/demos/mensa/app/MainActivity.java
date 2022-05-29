package de.ur.mi.android.demos.mensa.app;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.data.MensaDataListener;
import de.ur.mi.android.demos.mensa.app.data.MensaDataProvider;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;
import de.ur.mi.android.demos.mensa.app.ui.MensaDataAdapter;

public class MainActivity extends Activity implements MensaDataListener {

    private MensaDataProvider provider;
    private MensaDataAdapter adapter;
    private TabLayout daySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        RecyclerView viewForCurrentMenu = findViewById(R.id.view_current_menu);
        adapter = new MensaDataAdapter();
        viewForCurrentMenu.setAdapter(adapter);
        daySelector = findViewById(R.id.tab_layout_weekdays);
        daySelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Weekday selectedDay = Weekday.values()[tab.getPosition()];
                showMenuForDay(selectedDay);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initData() {
        provider = new MensaDataProvider(getApplicationContext(), this);
        provider.update();
    }

    private void showMenuForDay(Weekday day) {
        if (day == null) {
            return;
        }
        ArrayList<MensaDish> menuForSelectedDay = provider.getMenuForDay(day);
        adapter.setMenu(menuForSelectedDay);
    }

    @Override
    public void onMensaDataUpdated() {
        daySelector.selectTab(daySelector.getTabAt(Weekday.currentOrNearest().ordinal()));
    }
}