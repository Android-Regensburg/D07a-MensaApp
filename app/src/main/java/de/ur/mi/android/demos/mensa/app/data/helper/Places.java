package de.ur.mi.android.demos.mensa.app.data.helper;

import de.ur.mi.android.demos.mensa.app.R;

/**
 * Diese Enum repräsentiert die verfügbaren Mensen.
 * Über die fromLongName-Methode kann eine Instanz zurückgegeben werden, die einem Namen einer
 * Hochschule entspricht.
 * Die fromItemId Methode erlaubt die Rückgabe eines Orts, der mit einem Element des Drawer Menüs
 * korrespondiert.
 * Die Places Objekte haben die code Eigenschaft, diese muss in die URL der API eingefügt werden,
 * um Daten der entsprechenden Mensa zu erhalten.
 */
public enum Places {

    UNI_REGENSBURG_MENSA("Regensburg/Mensa", "Mensa der Uni Regensburg"),
    OTH_REGENSBURG("Regensburg/Seybothstrasse-mittags", "Mensa der OTH Regensburg"),
    UNI_PASSAU("Passau/Mensa", "Mensa der Uni Passau"),
    PT_CAFETE("Regensburg/Cafeteria-PT", "PT Cafeteria an der Uni Regensburg");

    public final String code;
    public final String label;

    Places(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static Places fromLongName(String name) {
        if(name.equals("Uni Regensburg")) {
            return Places.UNI_REGENSBURG_MENSA;
        } else if (name.equals("OTH Regensburg")) {
            return Places.OTH_REGENSBURG;
        } else if (name.equals("Uni Passau")) {
            return Places.UNI_PASSAU;
        } else if (name.equals("PT Cafete")) {
            return Places.PT_CAFETE;
        }

        return Places.UNI_REGENSBURG_MENSA;
    }

    public static Places fromItemId(int id) {
        if (id == R.id.regensburg_menu) {
            return Places.UNI_REGENSBURG_MENSA;
        } else if (id == R.id.oth_menu) {
            return Places.OTH_REGENSBURG;
        } else if(id == R.id.passau_menu) {
            return Places.UNI_PASSAU;
        } else if(id == R.id.pt_menu) {
            return Places.PT_CAFETE;
        }

        return Places.UNI_REGENSBURG_MENSA;
    }

}
