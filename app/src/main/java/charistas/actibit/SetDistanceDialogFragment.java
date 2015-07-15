package charistas.actibit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SetDistanceDialogFragment extends DialogFragment {

    static SetDistanceDialogFragment newInstance() {
        SetDistanceDialogFragment f = new SetDistanceDialogFragment();
        return f;
    }

    TextWatcher tt = null;
    static boolean flag = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_distance_dialog, null);

        builder.setTitle("Set Distance");

        final EditText distanceEditText = (EditText)v.findViewById(R.id.distanceEditText);

        // Set view
        builder.setView(v);

        // Set positive and negative buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String distance = distanceEditText.getText().toString();
                distanceListener.onDistanceDialogComplete(distance);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.distanceListener = (onDistanceDialogComplete)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDistanceCompleteListener");
        }
    }

    public  interface onDistanceDialogComplete {
        void onDistanceDialogComplete(String distance);
    }

    private onDistanceDialogComplete distanceListener;
}