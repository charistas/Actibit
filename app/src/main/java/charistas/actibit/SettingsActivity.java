package charistas.actibit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import java.util.Arrays;

public class SettingsActivity extends PreferenceActivity {
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Toolbar
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_settings, null);
        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);
        Toolbar mToolBar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        mToolBar.setTitle(getTitle());
        mToolBar.setTitleTextColor(0xFFFFFFFF);
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                for (String activityName : FitbitActivityInfo.activityNames) {
                    if (key.equals(activityName)) {
                        // Save preference for future use
                        CheckBoxPreference cbp = (CheckBoxPreference)findPreference(key);
                        SharedPreferences.Editor editor = getSharedPreferences("charistas.actibit", MODE_PRIVATE).edit();
                        editor.putBoolean(key, cbp.isEnabled());
                        editor.commit();

                        // Remove activity from Adapter if it's disabled - Add activity otherwise
                        if (!cbp.isEnabled()) {
                            PickActivity.ca.removeItem(Arrays.asList(FitbitActivityInfo.activityNames).indexOf(activityName));
                        }
                        else {
                            PickActivity.ca.addItem(activityName);
                        }
                    }
                }
            }
        };

        SharedPreferences prefs = getSharedPreferences("charistas.actibit", MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    private void setupSimplePreferencesScreen() {
        // Add 'general' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_general);
        addPreferencesFromResource(R.xml.preferances_general);

        SharedPreferences prefs = getSharedPreferences("charistas.actibit", MODE_PRIVATE);
        PreferenceScreen mPreferenceScreen = getPreferenceScreen();
        CheckBoxPreference [] checkboxes = new CheckBoxPreference[FitbitActivityInfo.activityNames.length];
        for (int i = 0; i < FitbitActivityInfo.activityNames.length; i++) {
            checkboxes[i] = new CheckBoxPreference(this);
            checkboxes[i].setKey(FitbitActivityInfo.activityNames[i]);
            checkboxes[i].setTitle(FitbitActivityInfo.activityNames[i]);
            checkboxes[i].setChecked(prefs.getBoolean(FitbitActivityInfo.activityNames[i], true));
            mPreferenceScreen.addPreference(checkboxes[i]);
        }
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferances_general);
        }
    }
}
