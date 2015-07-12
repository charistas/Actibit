package charistas.actibit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class SetDurationDialogFragment extends DialogFragment {

    static SetDurationDialogFragment newInstance() {
        SetDurationDialogFragment f = new SetDurationDialogFragment();
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_duration_dialog, null);

        final NumberPicker hp = (NumberPicker) v.findViewById(R.id.hourPicker);
        final String [] hourPickerValues = new String[13];
        hourPickerValues[0] = "0";
        for (int i = 1; i < hourPickerValues.length; i++) {
            hourPickerValues[i] = Integer.toString(i);
        }
        hp.setMaxValue(23);
        hp.setMinValue(0);
        hp.setWrapSelectorWheel(false);

        final NumberPicker mp = (NumberPicker) v.findViewById(R.id.minutePicker);
        final String [] minutePickerValues = new String[13];
        minutePickerValues[0] = "0";
        for (int i = 1; i < minutePickerValues.length; i++) {
            minutePickerValues[i] = Integer.toString(5 * i);
        }
        mp.setMinValue(0);
        mp.setMaxValue(11);
        mp.setDisplayedValues(minutePickerValues);
        mp.setWrapSelectorWheel(false);

        builder.setTitle("Set Duration");

        // TODO: Ask on StackOverflow why TextViews don't show
        TextView htv = (TextView)v.findViewById(R.id.hoursTextView);
        htv.setEnabled(true);

        TextView mtv = (TextView)v.findViewById(R.id.minutesTextView);
        mtv.setEnabled(true);

        // Set view
        builder.setView(v);

        // Set positive and negative buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String hours = hourPickerValues[hp.getValue()];
                String minutes = minutePickerValues[mp.getValue()];
                mListener.onComplete(hours, minutes);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public  interface OnCompleteListener {
        void onComplete(String hours, String minutes);
    }

    private OnCompleteListener mListener;
}