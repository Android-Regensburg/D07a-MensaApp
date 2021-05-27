package de.ur.mi.android.demos.mensa.app.data;

import org.json.JSONException;
import org.json.JSONObject;

import de.ur.mi.android.demos.mensa.app.data.helper.Category;
import de.ur.mi.android.demos.mensa.app.data.helper.Weekday;

public class MensaDish {

    public final String name;
    public final Weekday onDay;
    public final Category category;

    private MensaDish(String name, Weekday onDay, Category category) {
        this.name = name;
        this.onDay = onDay;
        this.category = category;
    }

    public static MensaDish fromJSONObject(JSONObject object) throws JSONException {
        String name = object.getString("name");
        String dayFromJSON = object.getString("day");
        Weekday onDay = Weekday.fromShortName(dayFromJSON);
        String categoryFromJSON = object.getString("category");
        Category category = Category.fromValue(categoryFromJSON);
        return new MensaDish(name, onDay, category);
    }
}
