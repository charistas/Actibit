package charistas.actibit;

import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FitbitActivityInfo {
    protected String name;

    public static String [] activityNames = new String[] {
            "Basketball",
            "Billiard",
            "Cleaning",
            "Cooking",
            "Cycling",
            "Soccer",
            "Swimming",
            "Tennis",
            "Volleyball",
            "Yoga"
    };

    public static String [] activityIDs = new String[] {
            "15040",
            "15080",
            "5020",
            "5052",
            "90001",
            "15605",
            "18310",
            "15675",
            "15711",
            "52001"
    };

    public static Map getActivityIDs(SharedPreferences prefs) {
        int [] timesUsed = new int[activityNames.length];
        List<Info> activities = new ArrayList<>();

        // Create list
        for (int i = 0; i < activityNames.length; i++) {
            timesUsed[i] = prefs.getInt(activityNames[i] +"_timesUsed", 0);
            activities.add(new Info(activityNames[i], activityIDs[i], timesUsed[i]));
        }

        // Sort list
        Collections.sort(activities, new Comparator<Info>() {
            @Override
            public int compare(Info i1, Info i2) {
                return i2.timesUsed - i1.timesUsed; // Descending
            }
        });

        // Create map
        Map ids = new LinkedHashMap();
        for (Info activity : activities) {
            ids.put(activity.name, activity.id);
        }

        /*ids.put("Basketball", "15040");
        ids.put("Billiard", "15080");
        ids.put("Cleaning", "5020");
        ids.put("Cooking", "5052");
        ids.put("Cycling", "90001");
        ids.put("Soccer", "15605");
        ids.put("Swimming", "18310");
        ids.put("Tennis", "15675");
        ids.put("Volleyball", "15711");
        ids.put("Yoga", "52001");*/

        return ids;
    }

    public static Map getDrawables(Resources resources) {
        Map drawables = new LinkedHashMap();

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
        Map parameters = new LinkedHashMap();

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
