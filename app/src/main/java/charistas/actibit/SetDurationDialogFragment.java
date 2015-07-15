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

        final NumberPicker hourPicker = (NumberPicker) v.findViewById(R.id.hourPicker);
        final String [] hourPickerValues = new String[24];
        hourPickerValues[0] = "0";
        for (int i = 1; i < hourPickerValues.length; i++) {
            hourPickerValues[i] = Integer.toString(i);
        }
        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);
        hourPicker.setWrapSelectorWheel(false);

        final NumberPicker minutePicker = (NumberPicker) v.findViewById(R.id.minutePicker);
        final String [] minutePickerValues = new String[13];
        minutePickerValues[0] = "0";
        for (int i = 1; i < minutePickerValues.length; i++) {
            minutePickerValues[i] = Integer.toString(5 * i);
        }
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(11);
        minutePicker.setDisplayedValues(minutePickerValues);
        minutePicker.setWrapSelectorWheel(false);

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
                String hours = hourPickerValues[hourPicker.getValue()];
                String minutes = minutePickerValues[minutePicker.getValue()];
                durationListener.onComplete(hours, minutes);
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
            this.durationListener = (OnDurationCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDurationCompleteListener");
        }
    }

    public  interface OnDurationCompleteListener {
        void onComplete(String hours, String minutes);
    }

    private OnDurationCompleteListener durationListener;
}