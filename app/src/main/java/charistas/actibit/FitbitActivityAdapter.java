package charistas.actibit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class FitbitActivityAdapter extends RecyclerView.Adapter<FitbitActivityAdapter.FitbitActivityViewHolder> {
    private List<FitbitActivityInfo> fitbitActivityList;
    private Context context;

    public FitbitActivityAdapter(Context context, List<FitbitActivityInfo> fitBitActivityList) {
        this.fitbitActivityList = fitBitActivityList;
        this.context = context;
    }

    public void removeItem(int position) {
        //fitbitActivityList.remove(position);
        //notifyDataSetChanged();
    }

    public void addItem(String activityName) {
        //fitbitActivityList.add(new FitbitActivityInfo(activityName));
        //notifyDataSetChanged();
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
        public void onClick(View v) {

            SharedPreferences prefs = v.getContext().getSharedPreferences("charistas.actibit", v.getContext().MODE_PRIVATE);
            String access_token = prefs.getString("ACCESS_TOKEN", null);
            if (access_token == null) {
                Toast.makeText(v.getContext(), "You have to sign in first.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(v.getContext(), LogFitbitActivity.class);
            intent.putExtra("FitbitActivityName", name.getText());
            v.getContext().startActivity(intent);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
