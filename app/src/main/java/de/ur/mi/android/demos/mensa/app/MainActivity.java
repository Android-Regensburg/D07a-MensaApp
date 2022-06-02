package de.ur.mi.android.demos.mensa.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;
import de.ur.mi.android.demos.mensa.app.ui.MensaDataAdapter;

public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener {

    // Mit dieser URL können Anfragen an die API gestellt werden, dazu müssen $PLACE und $DAY durch passende
    // Texte ausgetauscht werden.
    // Sie dürfen/sollen diese URL an eine passendere Stelle verschieben!
    private static final String API_URL = "https://mensa.software-engineering.education/mensa/$PLACE/$DAY";

    // Der MensaAdapter verbindet das UI mit der internen Repräsentation des Menüs
    private MensaDataAdapter adapter;
    // Das Tablayout bietet die Auswahl der Wochentage an.
    private TabLayout daySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setNavigationViewListener();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        // IHR CODE

        daySelector = findViewById(R.id.tab_layout_weekdays);
        daySelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Die onTabSelected-Methode wird aufgerufen, wenn ein Wochentag im TabLayout geklickt wird.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    // Diese Methode wird aufgerufen wenn eine andere Mensa gewählt wird.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Das gewählte Element wird markiert.
        item.setChecked(true);

        // Von der API sollen Daten für den angeklickten Ort abgefragt werden.
        // IHR CODE

        // Der Drawer wird wieder geschlossen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}