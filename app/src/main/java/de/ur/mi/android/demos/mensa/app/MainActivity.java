package de.ur.mi.android.demos.mensa.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.ur.mi.android.demos.mensa.app.data.MensaDataListener;
import de.ur.mi.android.demos.mensa.app.data.MensaDataProvider;
import de.ur.mi.android.demos.mensa.app.data.MensaDish;
import de.ur.mi.android.demos.mensa.app.data.helper.Places;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;
import de.ur.mi.android.demos.mensa.app.ui.MensaDataAdapter;

public class MainActivity extends Activity implements MensaDataListener, NavigationView.OnNavigationItemSelectedListener {

    private MensaDataProvider provider;
    private MensaDataAdapter adapter;
    private TabLayout daySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
        setNavigationViewListener();
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

    private void setNavigationViewListener() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        item.setChecked(true);

        if(id == R.id.regensburg_menu) {
            Log.d("Menu", "Uni Regensburg Clicked");
        } else if(id == R.id.oth_menu) {
            Log.d("Menu", "OTH Regensburg Clicked");
        }

        provider.getMenuForPlace(Places.fromItemId(id));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}