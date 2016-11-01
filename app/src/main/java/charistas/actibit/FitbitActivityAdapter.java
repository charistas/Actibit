package charistas.actibit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
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

import charistas.actibit.api.QuickPreferences;

/**
 * This class is responsible for handling and displaying the Fitbit activities.
 */
class FitbitActivityAdapter extends RecyclerView.Adapter<FitbitActivityAdapter.FitbitActivityViewHolder> {
    private List<FitbitActivity> fitbitActivityList;

    /**
     * A simple constructor for this class.
     * @param fitBitActivityList The Fitbit activity list that will be displayed inside this adapter
     */
    FitbitActivityAdapter(List<FitbitActivity> fitBitActivityList) {
        this.fitbitActivityList = fitBitActivityList;
    }

    /**
     * Returns the total number of the FItbit activities that will be displayed.
     * @return Sum
     */
    @Override
    public int getItemCount() {
        return fitbitActivityList.size();
    }

    /**
     * Responsible for displaying the appropriate image for each Fitbit activity.
     * @param contactViewHolder This is where the activities exist
     * @param i The position of the current Fitbit activity
     */
    @Override
    public void onBindViewHolder(FitbitActivityViewHolder contactViewHolder, int i) {
        FitbitActivity fitbitActivity = fitbitActivityList.get(i);
        contactViewHolder.name.setText(fitbitActivity.getName());

        Map<String, Integer> drawables = FitbitActivity.getDrawables(contactViewHolder.itemView.getContext().getResources());
        contactViewHolder.image.setImageResource(drawables.get(fitbitActivity.getName()));
    }

    /**
     * Creates a new ViewHolder that will contain all the Fitbit activities.
     * @param viewGroup The ViewGroup into which the new view will be added
     * @param i The view type of the new view
     * @return A new ViewHolder that holds the view of the given type
     */
    @Override
    public FitbitActivityViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);
        return new FitbitActivityViewHolder(itemView);
    }

    /**
     * This is where all of the enabled Fitbit activities will be kept.
     */
    static class FitbitActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        ImageView image;

        /**
         * A simple constructor for this class.
         * @param view The current view
         */
        FitbitActivityViewHolder(View view) {
            super(view);

            name =  (TextView)view.findViewById(R.id.txtName);
            image = (ImageView)view.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        /**
         * Handles the actions needed to be done after the user clicks on an image that represents
         * an activity. After we confirm that there is an active Internet connection available, we
         * proceed with launching the appropriate activity, LogFitbitActivity in that case.
         * @param view The View that the user clicked on
         */
        @Override
        public void onClick(final View view) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
            boolean haveToken = prefs.getBoolean(QuickPreferences.HAVE_AUTHORIZATION, false);

            // If the user hasn't signed-in, prompt him to do so.
            if (!haveToken) {
                Toast.makeText(view.getContext(), view.getContext().getString(R.string.you_have_to_sign_in), Toast.LENGTH_SHORT).show();
                return;
            }

            // We need to check if there's an active Internet connection available. In order to do
            // so, we have to use a new Thread, as we cannot perform network operations inside the
            // UI thread.
            final Handler handler = new Handler();
            new Thread(
                    new Runnable() {
                        public void run() {
                            boolean internetStatus = hasActiveInternetConnection(view.getContext());
                            if (!internetStatus) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(view.getContext(), view.getContext().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Intent intent = new Intent(view.getContext(), LogFitbitActivity.class);
                                intent.putExtra("FitbitActivityName", name.getText());
                                view.getContext().startActivity(intent);
                            }
                        }


            }).start();
        }

        /**
         * Using a random website, www.google.com in this case, to test the device's Internet connection
         * @return True if a working Internet connection exists, false otherwise
         */
        private boolean hasActiveInternetConnection(Context context) {
            if (isNetworkAvailable(context)) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                    connection.setRequestProperty("User-Agent", "Test");
                    connection.setRequestProperty("Connection", "close");
                    connection.setConnectTimeout(1500);
                    connection.connect();
                    return (connection.getResponseCode() == 200);
                } catch (IOException e) {
                    Toast.makeText(context, context.getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        /**
         * Checks whether there is an active data network available
         * @param context The context to use
         * @return True if there's an active data network available, false otherwise
         */
        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }
    }
}
