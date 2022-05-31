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

    // Der MensaDataProvider erlaubt es Menüs aus dem Internet abzufragen und in das UI einzubinden.
    private MensaDataProvider provider;
    // Der MensaAdapter verbindet das UI mit der internen Repräsentation des Menüs
    private MensaDataAdapter adapter;
    // Das Tablayout bietet die Auswahl der Wochentage an.
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
        // Der MensaDataAdapter wird an den RecylerView angeschlossen.
        RecyclerView viewForCurrentMenu = findViewById(R.id.view_current_menu);
        adapter = new MensaDataAdapter();
        viewForCurrentMenu.setAdapter(adapter);
        // Auf dem Tablayout wird ein EventListener registriert.
        daySelector = findViewById(R.id.tab_layout_weekdays);
        daySelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /*
             *  Wird ein Wochentag im Tablayout ausgewählt, so wird ein entsprechendes Element des
             *  Weekday Enums erzuegt und das Menü für diesen Tag abgefragt.
             */
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

    // Die Activity registriert sich als Listener auf Klicks im NavigationDrawer
    private void setNavigationViewListener() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
    }

    // Der DataProvider wird initialisiert und die Daten von der API angefragt.
    private void initData() {
        provider = new MensaDataProvider(getApplicationContext(), this);
        provider.update();
    }

    /*
     *   Bei Aufruf dieser Methode werden die Menüelemente, die mit dem übergebenen Wochentag
     *   korrespondieren an den Adapter übergeben und angezeigt.
     */
    private void showMenuForDay(Weekday day) {
        if (day == null) {
            return;
        }
        ArrayList<MensaDish> menuForSelectedDay = provider.getMenuForDay(day);
        adapter.setMenu(menuForSelectedDay);
    }

    // Wenn neue Mensadaten verfügbar sind wird der aktuelle Wochentag ausgewählt.
    @Override
    public void onMensaDataUpdated() {
        daySelector.selectTab(daySelector.getTabAt(Weekday.currentOrNearest().ordinal()));
    }

    // Diese Methode wird aufgerufen wenn eine andere Mensa gewählt wird.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Das gewählte Element wird markiert.
        item.setChecked(true);

        // Von der API werden Daten für diesen Ort angefragt.
        provider.getMenuForPlace(Places.fromItemId(id));

        // Der Drawer wird wieder geschlossen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}