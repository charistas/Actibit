package charistas.actibit;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends ActionBarActivity {
    static Context context;
    static SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        context = this;

        // Display the fragment as the main content
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_general);

            listener = this;

            // Add activity checkboxes
            SharedPreferences prefs = context.getSharedPreferences("charistas.actibit", MODE_PRIVATE);
            PreferenceScreen mPreferenceScreen = getPreferenceScreen();
            CheckBoxPreference[] checkboxes = new CheckBoxPreference[FitbitActivityInfo.activityNames.length];
            for (int i = 0; i < FitbitActivityInfo.activityNames.length; i++) {
                checkboxes[i] = new CheckBoxPreference(context);
                checkboxes[i].setKey(FitbitActivityInfo.activityNames[i]);
                checkboxes[i].setTitle(FitbitActivityInfo.activityNames[i]);
                checkboxes[i].setChecked(prefs.getBoolean(FitbitActivityInfo.activityNames[i], true));
                mPreferenceScreen.addPreference(checkboxes[i]);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            for (String activityName : FitbitActivityInfo.activityNames) {
                if (key.equals(activityName)) {
                    // Save preference for future use
                    CheckBoxPreference cbp = (CheckBoxPreference)findPreference(key);
                    SharedPreferences.Editor editor = context.getSharedPreferences("charistas.actibit", MODE_PRIVATE).edit();
                    editor.putBoolean(key, cbp.isChecked());
                    editor.commit();
                    //Toast.makeText(context,activityName +" is " +String.valueOf(cbp.isChecked()), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
