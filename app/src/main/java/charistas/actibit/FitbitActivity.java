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

/**
 * Responsible for keeping the name, the id and the total times used so far for each of the
 * supported Fitbit activities.
 */
class FitbitActivity {
    private String name;
    private String id;
    private int timesUsed;

    /**
     * Gets the current activity's name.
     * @return Name of the current activity
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the current activity's name.
     * @param name Name of the current activity
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An empty constructor.
     */
    FitbitActivity () {}

    /**
     * A constructor that creates a new instance of this class with the given parameters.
     * @param name The name of the current activity
     * @param id The unique ID of the current activity
     * @param timesUsed The total number of times that the current activity has been chosen by the
     *                  user
     */
    private FitbitActivity (String name, String id, int timesUsed) {
        this.timesUsed = timesUsed;
        this.id = id;
        this.name = name;
    }

    /**
     * Contains the names and the ids of all the available Fitbit activities.
     */
    private static final Map<String, String> activities = new HashMap<>();
    static {
        activities.put("Basketball", "15040");
        activities.put("Billiard", "15080");
        activities.put("Cleaning", "5020");
        activities.put("Cooking", "5052");
        activities.put("Cycling", "90001");
        activities.put("Soccer", "15605");
        activities.put("Swimming", "18310");
        activities.put("Tennis", "15675");
        activities.put("Volleyball", "15711");
        activities.put("Yoga", "52001");
    }

    /**
     * Calculates the total number of Fitbit activities that are supported.
     * @return The total number of Fitbit activities supported
     */
    static int getTotalActivities() {
        return activities.size();
    }

    static String [] getActivityNames() {
        String [] activityNames = new String[activities.size()];

        int counter = 0;
        for (Map.Entry<String, String> entry : activities.entrySet()) {
            activityNames[counter] = entry.getKey();
            counter++;
        }

        return activityNames;
    }

    /**
     * Gets the Fitbit activities that are supported sorted in a descending order based on which
     * of them have been used the most by the user.
     * @param prefs The current SharedPreferences
     * @return Sorted list of Fitbit activities
     */
    static Map<String, String> getActivities(SharedPreferences prefs) {
        List<FitbitActivity> unsortedActivities = new ArrayList<>();

        // Create the unsorted list
        for (Map.Entry<String, String> entry : activities.entrySet()) {
            unsortedActivities.add(new FitbitActivity(entry.getKey(), entry.getValue(), prefs.getInt(entry.getKey() +"_timesUsed", 0)));
        }

        // Sort the aforementioned list
        Collections.sort(unsortedActivities, new Comparator<FitbitActivity>() {
            @Override
            public int compare(FitbitActivity activityOne, FitbitActivity activityTwo) {
                return activityTwo.timesUsed - activityOne.timesUsed;
            }
        });

        // Create the sorted map
        Map<String, String> sortedActivities = new LinkedHashMap<>();
        for (FitbitActivity activity : unsortedActivities) {
            sortedActivities.put(activity.name, activity.id);
        }

        return sortedActivities;
    }

    /**
     * Gets the resource identifier of the images that accompany Fitbit activities
     * @param resources Resources
     * @return A Map that contains info on the images that accompany Fitbit activities
     */
    static Map<String, Integer> getDrawables(Resources resources) {
        Map<String, Integer> drawables = new LinkedHashMap<>();

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

    /**
     * Gets the parameters required to log each Fitbit activity
     * @return A map with the parameters required to log each Fitbit activity
     */
    static Map<String, String []> getAllParameters() {
        Map<String, String []> parameters = new LinkedHashMap<>();

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
