package charistas.actibit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import charistas.actibit.auth.AuthenticationActivity;

public class PickActivity extends ActionBarActivity {
    private static Menu menu = null;
    private static boolean signedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        setTitle(R.string.title_activity_pick);

        SharedPreferences prefs = getSharedPreferences("charistas.actibit", MODE_PRIVATE);
        String access_token = prefs.getString("ACCESS_TOKEN", null);
        if (access_token == null) {
            setSignedInStatus(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Let's get you sign in with your Fitbit account.")
                    .setCancelable(false)
                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Start authentication process
                            Intent intent = new Intent(PickActivity.this, AuthenticationActivity.class);
                            PickActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Let the user know how he can sign in later
                            Toast.makeText(PickActivity.this, "You may sign in later via the menu.", Toast.LENGTH_LONG).show();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            setSignedInStatus(true);
        }

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        FitbitActivityAdapter2 ca = new FitbitActivityAdapter2(this, createList());
        recList.setAdapter(ca);
    }

    private List<FitbitActivityInfo2> createList() {
        Map<String, String> activities = FitbitActivityInfo2.getActivityIDs();
        List<FitbitActivityInfo2> result = new ArrayList<>();

        for (String key : activities.keySet()) {
            FitbitActivityInfo2 ci = new FitbitActivityInfo2();
            ci.name = key;
            result.add(ci);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
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
        else if (id == R.id.action_sign_in) {
            if (signedIn) {
                // Sign Out
                setSignedInStatus(false);
                SharedPreferences.Editor editor = getSharedPreferences("charistas.actibit", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Toast.makeText(this, "You may sign in later via the menu.", Toast.LENGTH_LONG).show();
            }
            else {
                // Sign In
                Intent intent = new Intent(PickActivity.this, AuthenticationActivity.class);
                PickActivity.this.startActivity(intent);
            }
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

    public static void setSignedInStatus(boolean status) {
        PickActivity.signedIn = status;

        if (menu != null) {
            MenuItem item = menu.findItem(R.id.action_sign_in);
            if (status) {
                item.setTitle("Sign Out");
            } else {
                item.setTitle("Sign In");
            }
        }
    }
}
