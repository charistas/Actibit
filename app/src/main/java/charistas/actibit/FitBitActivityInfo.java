package charistas.actibit;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class FitBitActivityInfo {
    protected String name;

    public static Map getActivityIDs() {
        Map ids = new HashMap();

        ids.put("Tennis", "15675");
        ids.put("Cycling", "90001");
        ids.put("Cleaning", "5020");
        ids.put("Cooking", "5052");
        ids.put("Basketball", "15040");
        ids.put("Billiard", "15080");
        ids.put("Soccer", "15605");
        ids.put("Yoga", "52001");
        ids.put("Volleyball", "15711");
        ids.put("Swimming", "18310");

        return ids;
    }

    public static Map getDrawables(Resources resources) {
        Map drawables = new HashMap();

        drawables.put("Tennis", resources.getIdentifier("tennis", "drawable", "charistas.actibit"));
        drawables.put("Cycling", resources.getIdentifier("cycling", "drawable", "charistas.actibit"));
        drawables.put("Cleaning", resources.getIdentifier("cleaning", "drawable", "charistas.actibit"));
        drawables.put("Cooking", resources.getIdentifier("cooking", "drawable", "charistas.actibit"));
        drawables.put("Basketball", resources.getIdentifier("basketball", "drawable", "charistas.actibit"));
        drawables.put("Billiard", resources.getIdentifier("billiard", "drawable", "charistas.actibit"));
        drawables.put("Soccer", resources.getIdentifier("soccer", "drawable", "charistas.actibit"));
        drawables.put("Yoga", resources.getIdentifier("yoga", "drawable", "charistas.actibit"));
        drawables.put("Volleyball", resources.getIdentifier("volleyball", "drawable", "charistas.actibit"));
        drawables.put("Swimming", resources.getIdentifier("swimming", "drawable", "charistas.actibit"));

        return drawables;
    }

    public static Map getActivityParameters() {
        Map parameters = new HashMap();

        parameters.put("Tennis", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Cycling", new String[]{"date", "startTime", "durationMillis", "distance"});
        parameters.put("Cleaning", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Cooking", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Basketball", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Billiard", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Soccer", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Yoga", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Volleyball", new String[]{"date", "startTime", "durationMillis"});
        parameters.put("Swimming", new String[]{"date", "startTime", "durationMillis"});

        return parameters;
    }
}
