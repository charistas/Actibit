package charistas.actibit;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import charistas.actibit.auth.AuthenticationActivity;
import charistas.actibit.auth.FitbitApi;

public class LogFitbitActivity extends ActionBarActivity implements View.OnClickListener, SetDurationDialogFragment.OnDurationCompleteListener, SetDistanceDialogFragment.onDistanceDialogComplete {
    TextView [] myTextViews = null;
    EditText[] myEditTexts = null;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    SimpleDateFormat dateFormatter;
    SimpleDateFormat timeFormatter;
    //SimpleDateFormat durationFormatter;

    EditText dateEditText;
    EditText timeEditText;
    EditText durationEditText;
    EditText distanceEditText;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Map<String, String> labels = new HashMap();
        labels.put("date", "Date");
        labels.put("startTime", "Start time");
        labels.put("durationMillis", "Duration");
        labels.put("distance", "Distance");

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        timeFormatter = new SimpleDateFormat("kk:mm", Locale.US);
        //durationFormatter = new SimpleDateFormat("kk 'hours' 'and' mm 'minutes'", Locale.US);

        context = this;
        //Toast.makeText(this, "LogFitbitActivity", Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        String activityName = null;
        if (extras != null) {
            activityName = extras.getString("FitbitActivityName");
            //Toast.makeText(this, "Received: " + activityName, Toast.LENGTH_SHORT).show();
        }

        Map<String, String> ids = FitbitActivityInfo.getActivityIDs();
        Map<String, String []> parameters = FitbitActivityInfo.getActivityParameters();

        String [] curParameters = parameters.get(activityName);
        int curLen = curParameters.length;
        myTextViews = new TextView[curLen];
        myEditTexts = new EditText[curLen];

        LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.myLinearLayout);
        // Add TextViews and EditTexts
        for (int i = 0; i < myTextViews.length; i++) {
            TextView rowTextView = new TextView(this);
            final EditText rowEditText = new EditText(this);

            rowTextView.setText(labels.get(curParameters[i]));
            if (curParameters[i].equals("date")) {
                rowEditText.setOnClickListener(this);
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        rowEditText.setText(dateFormatter.format(newDate.getTime()));
                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                rowEditText.setInputType(InputType.TYPE_NULL);
                dateEditText = rowEditText;
            }
            else if (curParameters[i].equals("startTime")) {
                rowEditText.setOnClickListener(this);
                Calendar newCalendar = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);
                        rowEditText.setText(timeFormatter.format(newTime.getTime()));
                    }
                },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), true);
                rowEditText.setInputType(InputType.TYPE_NULL);
                timeEditText = rowEditText;
            }
            else if (curParameters[i].equals("durationMillis")) {
                rowEditText.setOnClickListener(this);
                rowEditText.setInputType(InputType.TYPE_NULL);
                durationEditText = rowEditText;
            }
            else if (curParameters[i].equals("distance")) {
                rowEditText.setOnClickListener(this);
                rowEditText.setInputType(InputType.TYPE_NULL);
                distanceEditText = rowEditText;
            }

            myLinearLayout.addView(rowTextView);
            myLinearLayout.addView(rowEditText);

            myTextViews[i] = rowTextView;
            myEditTexts[i] = rowEditText;
        }

        final String chosenActivity = activityName;
        final String [] chosenActivityParameters = new String[myEditTexts.length];
        for (int i = 0; i < myEditTexts.length; i++) {
            chosenActivityParameters[i] = myEditTexts[i].getText().toString();
        }

        Button myButton = new Button(this);
        myButton.setText("Done");
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (EditText et : myEditTexts) {
                    if (et.getText().toString().equals("")) {
                        Toast.makeText(context,"Missing parameters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
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

        final Map<String, String> ids = FitbitActivityInfo.getActivityIDs();
        final Map<String, String[]> parameters = FitbitActivityInfo.getActivityParameters();

        final Handler handler = new Handler();
        // Network operation shouldn't run on main thread
        new Thread(new Runnable() {
            public void run() {
                OAuthService service = new ServiceBuilder().provider(FitbitApi.class).apiKey(AuthenticationActivity.CONSUMER_KEY).apiSecret(AuthenticationActivity.CONSUMER_SECRET).build();
                OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.fitbit.com/1/user/-/activities.json");

                request.addBodyParameter("activityId", ids.get(activityName));

                String [] curParameters = parameters.get(activityName);

                for (int i = 0; i < curParameters.length; i++) {
                    if (curParameters[i].equals("durationMillis")) {
                        String durationText = myEditTexts[i].getText().toString();
                        String [] result = durationText.split(" ");
                        int hours = Integer.parseInt(result[0]);
                        int minutes = Integer.parseInt(result[3]);
                        int milliseconds = ((hours * 60) + minutes) * 60000;
                        request.addBodyParameter(curParameters[i], Integer.toString(milliseconds));
                    }
                    else if (curParameters[i].equals("distance")) {
                        String distanceText = myEditTexts[i].getText().toString();
                        String [] result = distanceText.split(" ");
                        request.addBodyParameter(curParameters[i], result[0]);
                    }
                    else {
                        request.addBodyParameter(curParameters[i], myEditTexts[i].getText().toString());
                    }
                }

                service.signRequest(accessToken, request);
                final Response response = request.send();

                // Visual output should run on main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ( (response.getCode() == 201) || (response.getCode() == 200) ) {
                            Toast.makeText(context, "Sent", Toast.LENGTH_LONG).show();
                        }
                        else if (response.getCode() == 401) {
                            Toast.makeText(context, "Authentication needed", Toast.LENGTH_LONG).show();
                        }
                        else if (response.getCode() == 500) {
                            Toast.makeText(context, "Something is wrong at Fitbit, try your request later", Toast.LENGTH_LONG).show();
                        }
                        else if (response.getCode()  == 502) {
                            Toast.makeText(context, "Fitbit will be back soon, maintenance", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(context, "Unknown error: " +response.getMessage() +" | Code: " +response.getCode(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                finish();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view == dateEditText) {
            datePickerDialog.show();
        }
        else if (view == timeEditText) {
            timePickerDialog.show();
        }
        else if (view == durationEditText) {
            showDurationDialogFragment();
        }
        else if (view == distanceEditText) {
            showDistanceDialogFragment();
        }
    }

    void showDurationDialogFragment() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = SetDurationDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    void showDistanceDialogFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment newFragment = SetDistanceDialogFragment.newInstance();
        newFragment.show(ft, "dialog");
    }

    public void onComplete(String hours, String minutes) {
        if (hours == "1") {
            durationEditText.setText(hours +" hour and " +minutes +" minutes");
        }
        else {
            durationEditText.setText(hours +" hours and " +minutes +" minutes");
        }
    }

    public void onDistanceDialogComplete(String distance) {
        distanceEditText.setText(distance +" km");
    }
}
