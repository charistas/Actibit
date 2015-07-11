package charistas.actibit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.util.Map;

import charistas.actibit.auth.AuthenticationActivity;
import charistas.actibit.auth.FitbitApi;

public class LogFitBitActivity extends ActionBarActivity {
    TextView [] myTextViews = null;
    EditText[] myEditTexts = null;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        context = this;
        //Toast.makeText(this, "LogFitBitActivity", Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        String value = null;
        if (extras != null) {
            value = extras.getString("FitBitActivityName");
            //Toast.makeText(this, "Received: " + value, Toast.LENGTH_SHORT).show();
        }

        Map<String, String> ids = FitBitActivityInfo.getActivityIDs();
        Map<String, String []> parameters = FitBitActivityInfo.getActivityParameters();

        String [] curParameters = parameters.get(value);
        int curLen = curParameters.length;
        myTextViews = new TextView[curLen];
        myEditTexts = new EditText[curLen];

        LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.myLinearLayout);
        // Add TextViews and EditTexts
        for (int i = 0; i < myTextViews.length; i++) {
            TextView rowTextView = new TextView(this);
            EditText rowEditText = new EditText(this);

            rowTextView.setText(curParameters[i]);

            myLinearLayout.addView(rowTextView);
            myLinearLayout.addView(rowEditText);

            myTextViews[i] = rowTextView;
            myEditTexts[i] = rowEditText;
        }

        final String chosenActivity = value;
        final String [] chosenActivityParameters = new String[myEditTexts.length];
        for (int i = 0; i < myEditTexts.length; i++) {
            chosenActivityParameters[i] = myEditTexts[i].getText().toString();
        }

        Button myButton = new Button(this);
        myButton.setText("Ok");
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postData(chosenActivity);
            }
        });
        myLinearLayout.addView(myButton);
    }

    public void postData(final String activityName) {
        SharedPreferences prefs = getSharedPreferences("charistas.actibit", MODE_PRIVATE);
        String ACCESS_TOKEN = prefs.getString("ACCESS_TOKEN", null);
        String ACCESS_SECRET = prefs.getString("ACCESS_SECRET", null);
        String ACCESS_RAW_RESPONSE = prefs.getString("ACCESS_RAW_RESPONSE", null);
        final Token accessToken = new Token(ACCESS_TOKEN, ACCESS_SECRET, ACCESS_RAW_RESPONSE);

        final Map<String, String> ids = FitBitActivityInfo.getActivityIDs();
        final Map<String, String[]> parameters = FitBitActivityInfo.getActivityParameters();

        final Handler handler = new Handler();
        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                OAuthService service = new ServiceBuilder().provider(FitbitApi.class).apiKey(AuthenticationActivity.CONSUMER_KEY).apiSecret(AuthenticationActivity.CONSUMER_SECRET).build();
                OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.fitbit.com/1/user/-/activities.json");

                request.addBodyParameter("activityId", ids.get(activityName));

                String [] curParameters = parameters.get(activityName);

                for (int i = 0; i < curParameters.length; i++) {
                    request.addBodyParameter(curParameters[i], myEditTexts[i].getText().toString());
                }

                service.signRequest(accessToken, request);
                request.send();

                // Visual output should run on main thread
                /*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Sent!", Toast.LENGTH_SHORT).show();
                    }
                });*/
                finish();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log, menu);
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
