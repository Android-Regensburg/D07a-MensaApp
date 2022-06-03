package de.ur.mi.android.demos.mensa.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    private Places currentPlace;

    private ScheduledExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPlace = Places.UNI_REGENSBURG;
        initUI();
        initData();
        setNavigationViewListener();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);

        setSubtitle(currentPlace);

        // Der MensaDataAdapter wird an den RecylerView angeschlossen.
        RecyclerView viewForCurrentMenu = findViewById(R.id.view_current_menu);
        adapter = new MensaDataAdapter(getBaseContext());
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


    private void setSubtitle(Places place) {
        TextView subtitle = findViewById(R.id.text_content_description);
        subtitle.setText(subtitle.getText().toString().replace("$MENSA", place.getLabel()));
    }

    // Die Activity registriert sich als Listener auf Klicks im NavigationDrawer
    private void setNavigationViewListener() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);
    }

    // Der DataProvider wird initialisiert und die Daten von der API angefragt.
    private void initData() {
        provider = new MensaDataProvider(getApplicationContext(), this);
        provider.initialRequest();
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
        Weekday currentDay = Weekday.currentOrNearest();
        daySelector.selectTab(daySelector.getTabAt(currentDay.ordinal()));
        setSubtitle(currentPlace);

        // Zu Demonstrationszwecken wird die Anzeige der geladenen Informationen im UI um 2 Sekunden verzögert.
        // Damit dieser Delay unser UI nicht blockiert, verwenden wir einen Executor.
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                removeLoadingAndUpdate();
            }
        }, 2, 2, TimeUnit.SECONDS);
    }

    /*
        Diese Methode wird (auf einem anderen Thread) aufgerufen, wenn Daten geladen wurden
        und der Delay abgelaufen ist. Dann wird das angezeigte Menü (auf dem UI-Thread) aktualisiert.
     */
    private void removeLoadingAndUpdate() {
        Weekday currentDay = Weekday.currentOrNearest();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showMenuForDay(currentDay);
            }
        });
        executor.shutdownNow();
    }

    // Diese Methode wird aufgerufen wenn eine andere Mensa gewählt wird.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Das gewählte Element wird markiert.
        item.setChecked(true);

        // Von der API werden Daten für diesen Ort angefragt.
        currentPlace = Places.fromItemId(id);
        provider.getMenuForPlace(currentPlace);

        // Der Drawer wird wieder geschlossen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}