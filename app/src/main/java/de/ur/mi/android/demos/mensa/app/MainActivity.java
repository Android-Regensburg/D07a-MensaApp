package de.ur.mi.android.demos.mensa.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import de.ur.mi.android.demos.mensa.app.data.MensaDataProvider;

/**
 * Diese zentrale Activity der App verbindet die verschiedenen Bereiche der Anwendung. Hier werden
 * die Komponenten der Daten- und UI-Schicht initialisiert und gesteuert. Wir reagieren auf die
 * Eingaben der Nutzer*innen, fordern die entsprechenden Informationen aus der Datenschicht an und
 * übertragen diese in den sichtbaren Teil der Anwendung (UI).
 * <p>
 * Aufgaben
 * <p>
 * - Initialisieren des UI und Abfangen relevanter Eingaben, inkl. Vorbereitung des RecyclerView
 * - Initialisieren des DataProviders und Anstoßen des initialen Daten-Downloads
 * - Einholen der passenden Daten für den ausgewählten Wochentag und Weitergabe an den Adapter des RecyclerView
 * <p>
 * Unsere Aufgaben:
 * <p>
 * TODO Klassenstruktur zur Abbildung von einzelne Speisen schaffen
 * TODO JSON-Daten für die ganze Woche vom Server anfordern
 * TODO JSON-Daten in "richtige" Java-Objekte umwandeln
 * TODO Auswahl von Speisefolgen für einzelne Wochentage ermöglichen
 * TODO Speisefolge eines Tages im UI anzeigen (Adapter und ViewHolder)
 */

public class MainActivity extends AppCompatActivity {
    private TabLayout daySelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initUI();
    }

    private void initData() {
        MensaDataProvider provider = new MensaDataProvider(getApplicationContext());
        provider.update();
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        // Wir selektieren das TabLayout, mit dem die Nutzer*innen den anzuzeigenden Wochentag auswählen können ...
        daySelector = findViewById(R.id.tab_layout_weekdays);
        // .. und registrieren einen Listener, der uns über den Wechsel des ausgewählten Tabs informiert
        daySelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Diese Methode wird aufgerufen, wenn eines der Tab-Items (siehe Layout-Datei) ausgewählt wurde
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Über das übergebene Tab-Objekt können wir auslesen, welcher Tab selektiert wurde
                // z.B. dessen Position innerhalb des Elternelements (erster Tab, zweiter Tab, ...)
                // feststellen.
                Log.d("MENSA_APP", "in: onTabSelected (" + tab.getPosition() + ")");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}