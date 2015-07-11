/**
 * FitbitAndroidSample: https://github.com/pugwonk/FitbitAndroidSample
 * Getting Started With Scribe: https://github.com/fernandezpablo85/scribe-java/wiki/getting-started
 *
 */

package charistas.actibit.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import charistas.actibit.R;

public class LoginActivity extends ActionBarActivity {
    static OAuthService service;
    static Token requestToken;
    static final int GET_PIN_REQUEST = 101;

    //String value = null;
    //String [] parameters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toast.makeText(this, "LoginActivity", Toast.LENGTH_SHORT).show();

        startActivityForResult(new Intent(this, AuthenticationActivity.class), GET_PIN_REQUEST);
    }

    public void btnGetAccessCode(final View view) {
        EditText PIN = (EditText) findViewById(R.id.PIN);
        String gotPIN = PIN.getText().toString();
        final Verifier verifier = new Verifier(gotPIN);

        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                final Token accessToken = service.getAccessToken(requestToken, verifier);
                SharedPreferences.Editor editor = getSharedPreferences("charistas.actibit", MODE_PRIVATE).edit();
                editor.putString("ACCESS_TOKEN", accessToken.getToken());
                editor.putString("ACCESS_SECRET", accessToken.getSecret());
                editor.putString("ACCESS_RAW_RESPONSE", accessToken.getRawResponse());
                editor.commit();

                final TextView output = (TextView)findViewById(R.id.output);
                // Visual output should run on main thread
                output.post(new Runnable() {
                    @Override
                    public void run() {
                        output.setText("Access Token: " +accessToken.getToken());
                    }
                });
                finish();
            }
        }).start();
    }
/*
    public void btnPostData(View view) {
        EditText PIN = (EditText) findViewById(R.id.PIN);
        String gotPIN = PIN.getText().toString();
        final Verifier verifier = new Verifier(gotPIN);

        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                Token accessToken = service.getAccessToken(requestToken, verifier);

                OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.fitbit.com/1/user/-/activities.json");
                if (value.equals("Tennis")) {
                    request.addBodyParameter("activityId", LogFitBitActivity.TENNIS_ID);
                    for (int i = 0; i < parameters.length; i++) {
                        request.addBodyParameter(LogFitBitActivity.TENNIS_PARAMETERS[i], parameters[i]);
                    }
                }
                else if (value.equals("Cycling")) {
                    request.addBodyParameter("activityId", LogFitBitActivity.CYCLING_ID);
                    for (int i = 0; i < parameters.length; i++) {
                        request.addBodyParameter(LogFitBitActivity.CYCLING_PARAMETERS[i], parameters[i]);
                    }
                }

                 // The following is to POST activity:https://wiki.fitbit.com/display/API/API-Log-Activity
                //AuthRequest request = new OAuthRequest(Verb.POST, "https://api.fitbit.com/1/user/-/activities.json");
                //request.addBodyParameter("activityId", "15675");
                //request.addBodyParameter("startTime", "7:30");
                //request.addBodyParameter("durationMillis", "1800000");
                //request.addBodyParameter("date", "2015-07-06");

                 // The following is to GET example data:
                 // OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.fitbit.com/1/user/-/profile.json");

                service.signRequest(accessToken, request);

                final Response response = request.send();
                final TextView output = (TextView) findViewById(R.id.output);

                // Visual output should run on main thread
                output.post(new Runnable() {
                    @Override
                    public void run() {
                        output.setText(response.getBody());
                    }
                });
            }
        }).start();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if ( (requestCode == GET_PIN_REQUEST) && (resultCode == RESULT_OK)) {
                Bundle extras = intent.getExtras();
                if(extras != null){
                    final String pin = extras.getString("PIN");
                    final EditText PIN = (EditText) findViewById(R.id.PIN);

                    PIN.post(new Runnable() {
                        @Override
                        public void run() {
                            PIN.setText(pin);
                        }
                    });
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
