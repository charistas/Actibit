package charistas.actibit;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FitBitActivityAdapter extends RecyclerView.Adapter<FitBitActivityAdapter.FitBitActivityViewHolder> {
    private static MyClickListener myClickListener;
    private List<FitBitActivityInfo> fitBitActivityList;

    public FitBitActivityAdapter(List<FitBitActivityInfo> fitBitActivityList) {
        this.fitBitActivityList = fitBitActivityList;
    }

    @Override
    public int getItemCount() {
        return fitBitActivityList.size();
    }

    @Override
    public void onBindViewHolder(FitBitActivityViewHolder contactViewHolder, int i) {
        FitBitActivityInfo fitBitActivity = fitBitActivityList.get(i);
        contactViewHolder.name.setText(fitBitActivity.name);

        if (fitBitActivity.name.equals("Tennis")) {
            contactViewHolder.image.setImageResource(R.drawable.tennis);
        }
        else if (fitBitActivity.name.equals("Cycling")) {
            contactViewHolder.image.setImageResource(R.drawable.cycling);
        }
    }

    @Override
    public FitBitActivityViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new FitBitActivityViewHolder(itemView);
    }

    public static class FitBitActivityViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        protected TextView name;
        protected ImageView image;

        public FitBitActivityViewHolder(View v) {
            super(v);

            name =  (TextView)v.findViewById(R.id.txtName);
            image = (ImageView)v.findViewById(R.id.image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), name.getText() +" clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), LogFitBitActivity.class);
            intent.putExtra("FitBitActivityName", name.getText());
            v.getContext().startActivity(intent);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
