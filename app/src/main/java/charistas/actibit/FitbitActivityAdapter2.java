package charistas.actibit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class FitbitActivityAdapter2 extends RecyclerView.Adapter<FitbitActivityAdapter2.FitbitActivityViewHolder> {
    private static MyClickListener myClickListener;
    private List<FitbitActivityInfo2> fitbitActivityList;
    private Context context;

    public FitbitActivityAdapter2(Context context, List<FitbitActivityInfo2> fitBitActivityList) {
        this.fitbitActivityList = fitBitActivityList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return fitbitActivityList.size();
    }

    @Override
    public void onBindViewHolder(FitbitActivityViewHolder contactViewHolder, int i) {
        FitbitActivityInfo2 fitbitActivity = fitbitActivityList.get(i);
        contactViewHolder.name.setText(fitbitActivity.name);

        Map<String, Integer> drawables = FitbitActivityInfo2.getDrawables(context.getResources());
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
            //Toast.makeText(v.getContext(), name.getText() +" clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), LogFitbitActivity2.class);
            intent.putExtra("FitbitActivityName", name.getText());
            v.getContext().startActivity(intent);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
