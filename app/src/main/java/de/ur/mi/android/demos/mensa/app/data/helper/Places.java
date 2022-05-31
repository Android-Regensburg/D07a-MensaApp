package de.ur.mi.android.demos.mensa.app.data.helper;

import de.ur.mi.android.demos.mensa.app.R;

public enum Places {

    UNI_REGENSBURG("uni"),
    OTH_REGENSBURG("oth");

    public final String code;

    Places(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Places fromLongName(String name) {
        if(name.equals("Uni Regensburg")) {
            return Places.UNI_REGENSBURG;
        } else if (name.equals("OTH Regensburg")) {
            return Places.OTH_REGENSBURG;
        }

        return Places.UNI_REGENSBURG;
    }

    public static Places fromItemId(int id) {
        if (id == R.id.regensburg_menu) {
            return Places.UNI_REGENSBURG;
        } else if (id == R.id.oth_menu) {
            return Places.OTH_REGENSBURG;
        }

        return Places.UNI_REGENSBURG;
    }

}
