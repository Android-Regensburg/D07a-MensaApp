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
    PT_CAFETE("Regensburg/Cafeteria-PT", "PT Cafeteria an der Uni Regensburg"),
    CHEMIE_CAFETE("Regensburg/Cafeteria-Chemie", "Chemie Cafeteria an der Uni Regensburg"),
    HS_LANDSHUT_MENSA("Landshut/Mensa", "Mensa der Hochschule Landshut"),
    THD_MENSA("Deggendorf/Mensa", "Mensateria an der Technischen Hochschule Deggendorf"),
    STRAUBING_CAFE("Straubing/Mensa", "Cafe an der Donau am TUM Campus Straubing"),
    PFARRKIRCHEN_MENSA("Pfarrkirchen/Mensa", "Mensa an der THD in Pfarrkirchen");

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
        switch (name) {
            case "OTH Regensburg":
                return Places.OTH_REGENSBURG;
            case "Uni Passau":
                return Places.UNI_PASSAU;
            case "PT Cafete":
                return Places.PT_CAFETE;
            case "Chemie Cafete":
                return Places.CHEMIE_CAFETE;
            case "Hochschule Landshut":
                return Places.HS_LANDSHUT_MENSA;
            case "Technische Hochschule Deggendorf":
                return Places.THD_MENSA;
            case "TUM Campus Straubing":
                return Places.STRAUBING_CAFE;
            case "THD Pfarrkirchen":
                return Places.PFARRKIRCHEN_MENSA;
            default:
                return Places.UNI_REGENSBURG_MENSA;
        }
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
        } else if(id == R.id.menu_chemie) {
            return Places.CHEMIE_CAFETE;
        } else if(id == R.id.menu_landshut) {
            return Places.HS_LANDSHUT_MENSA;
        } else if(id == R.id.menu_deggendorf) {
            return Places.THD_MENSA;
        } else if(id == R.id.menu_straubing) {
            return Places.STRAUBING_CAFE;
        } else if(id == R.id.menu_pfarrkirchen) {
            return Places.PFARRKIRCHEN_MENSA;
        }

        return Places.UNI_REGENSBURG_MENSA;
    }

}
