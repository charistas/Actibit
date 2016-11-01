package charistas.actibit.api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import charistas.actibit.MainActivity;
import charistas.actibit.R;

/**
 * This class is responsible for receiving and saving the access token from the Fitbit API.
 */
public class AccessTokenReceiverActivity extends AppCompatActivity {
    String data;

    /**
     * Receives from Fitbit servers data like the access token and the user id and saves them.
     * @param savedInstanceState The state generated by a previous instance of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract the received data
        onNewIntent(getIntent());
        String accessToken = data.substring(data.indexOf("access_token") + 13, data.indexOf("&user_id"));
        String userId = data.substring(data.indexOf("user_id") + 8, data.indexOf("&scope"));
        String tokenType = data.substring(data.indexOf("token_type") + 11, data.indexOf("&expires_in"));

        // Save the received data
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(QuickPreferences.HAVE_AUTHORIZATION, true).apply();
        sharedPreferences.edit().putString(QuickPreferences.ACCESS_TOKEN, accessToken).apply();
        sharedPreferences.edit().putString(QuickPreferences.USER_ID, userId).apply();
        sharedPreferences.edit().putString(QuickPreferences.TOKEN_TYPE, tokenType).apply();
        sharedPreferences.edit().putString(QuickPreferences.FULL_AUTHORIZATION, tokenType +" " +accessToken).apply();

        // TODO: Add a 'Welcome <name>!' Toast message after each successful login. https://dev.fitbit.com/docs/user/
        Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_SHORT).show();

        // Return to initial screen
        Intent intent = new Intent(AccessTokenReceiverActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        data = intent.getDataString();
    }
}
