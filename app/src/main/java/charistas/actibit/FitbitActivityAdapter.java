package charistas.actibit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FitbitActivityAdapter extends RecyclerView.Adapter<FitbitActivityAdapter.FitbitActivityViewHolder> {
    private List<FitbitActivityInfo> fitbitActivityList;
    private static Context context;

    public FitbitActivityAdapter(Context context, List<FitbitActivityInfo> fitBitActivityList) {
        this.fitbitActivityList = fitBitActivityList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return fitbitActivityList.size();
    }

    @Override
    public void onBindViewHolder(FitbitActivityViewHolder contactViewHolder, int i) {
        FitbitActivityInfo fitbitActivity = fitbitActivityList.get(i);
        contactViewHolder.name.setText(fitbitActivity.name);

        Map<String, Integer> drawables = FitbitActivityInfo.getDrawables(context.getResources());
        contactViewHolder.image.setImageResource(drawables.get(fitbitActivity.name));
    }

    @Override
    public FitbitActivityViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new FitbitActivityViewHolder(itemView);
    }

    public static class FitbitActivityViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        protected TextView name;
        protected ImageView image;

        public FitbitActivityViewHolder(View v) {
            super(v);

            name =  (TextView)v.findViewById(R.id.txtName);
            image = (ImageView)v.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {

            SharedPreferences prefs = v.getContext().getSharedPreferences("charistas.actibit", v.getContext().MODE_PRIVATE);
            String access_token = prefs.getString("ACCESS_TOKEN", null);
            if (access_token == null) {
                Toast.makeText(v.getContext(), "You have to sign in first.", Toast.LENGTH_SHORT).show();
                return;
            }

            final Handler handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    boolean internetStatus = FitbitActivityAdapter.hasActiveInternetConnection();
                    if (!internetStatus) {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(v.getContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    else {
                        Intent intent = new Intent(v.getContext(), LogFitbitActivity.class);
                        intent.putExtra("FitbitActivityName", name.getText());
                        v.getContext().startActivity(intent);
                    }
                }
            }).start();
        }
    }

    public static boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("PickActivity", "Error checking internet connection", e);
            }
        } else {
            Log.d("PickActivity", "No network available!");
        }
        return false;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) PickActivity.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
