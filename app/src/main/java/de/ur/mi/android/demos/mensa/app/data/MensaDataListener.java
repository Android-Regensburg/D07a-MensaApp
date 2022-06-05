package de.ur.mi.android.demos.mensa.app.data;

/**
 * Interface, für Listener, die darüber informiert werden sollen, wenn die Daten des DataProvider aktualisiert wurden
 */
public interface MensaDataListener {

    void onMensaDataUpdated();
    void onListEmptied();

}
