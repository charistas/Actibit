package charistas.actibit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charistas.actibit.auth.AuthenticationActivity;

public class PickActivity extends ActionBarActivity {
    public static TextView authStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        setTitle(R.string.title_activity_pick);

        //Toast.makeText(this, "PickActivity", Toast.LENGTH_SHORT).show();

        SharedPreferences prefs = getSharedPreferences("charistas.actibit", MODE_PRIVATE);
        String access_token = prefs.getString("ACCESS_TOKEN", null);
        String access_secret = prefs.getString("ACCESS_SECRET", null);
        String access_raw_response = prefs.getString("ACCESS_RAW_RESPONSE", null);

        authStatus = (TextView)findViewById(R.id.authStatus);
        if (access_token == null) {
            setAuthStatus(false);
            //Toast.makeText(this, "Sign in needed!", Toast.LENGTH_LONG).show();
        }
        else {
            setAuthStatus(true);
            //Toast.makeText(this, "Access Token: " +access_token, Toast.LENGTH_LONG).show();
        }

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        FitBitActivityAdapter ca = new FitBitActivityAdapter(this, createList());
        recList.setAdapter(ca);
    }

    public  static void setAuthStatus(boolean enabled) {
        if (enabled) {
            authStatus.setText("Auth status: OK");
        }
        else {
            authStatus.setText("Auth status: Not OK");
        }
    }

    public void buttonConnectClicked (View view) {
        Intent intent = new Intent(view.getContext(), AuthenticationActivity.class);
        view.getContext().startActivity(intent);
    }

    private List<FitBitActivityInfo> createList() {
        Map<String, String> activities = FitBitActivityInfo.getActivityIDs();
        List<FitBitActivityInfo> result = new ArrayList<>();

        for (String key : activities.keySet()) {
            FitBitActivityInfo ci = new FitBitActivityInfo();
            ci.name = key;
            result.add(ci);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pick, menu);
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
            Toast.makeText(this, "Not yet implemented.", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.action_feedback) {
            sendFeedback();
            return true;
        }
        else if (id == R.id.action_sign_out) {
            Toast.makeText(this, "Not yet implemented.", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendFeedback() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"haritasi@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback on ActiBit app");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
        }
    }
}
