package charistas.actibit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * This class is responsible for initializing and configuring the hour and minute picker.
 */
public class SetDurationDialogFragment extends DialogFragment {

    /**
     * Create a new instance of this fragment.
     * @return A new instance of this fragment
     */
    static SetDurationDialogFragment createFragment() {
        return new SetDurationDialogFragment();
    }

    /**
     * Responsible for initializing and configuring the dialog that lets the user set the total
     * hours and minutes spent on the chosen Fitbit activity.
     * @param savedInstanceState The state generated by a previous instance of this activity
     * @return The newly created dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_duration_dialog, null);

        // Configure the picker that lets the user set the hours
        final NumberPicker hours = (NumberPicker) view.findViewById(R.id.hourPicker);
        final String [] hourPossibleValues = new String[24];
        hourPossibleValues[0] = "0";
        for (int i = 1; i < hourPossibleValues.length; i++) {
            hourPossibleValues[i] = Integer.toString(i);
        }
        hours.setMaxValue(23);
        hours.setMinValue(0);
        hours.setWrapSelectorWheel(false);

        // Configure the picker that lets the user set the minutes
        final NumberPicker minutes = (NumberPicker) view.findViewById(R.id.minutePicker);
        final String [] minutePossibleValues = new String[13];
        minutePossibleValues[0] = "0";
        for (int i = 1; i < minutePossibleValues.length; i++) {
            minutePossibleValues[i] = Integer.toString(5 * i);
        }
        minutes.setMinValue(0);
        minutes.setMaxValue(11);
        minutes.setDisplayedValues(minutePossibleValues);
        minutes.setWrapSelectorWheel(false);

        builder.setTitle(getString(R.string.set_duration));

        // TODO: Ask on StackOverflow why the following TextViews stay invisible
        TextView htv = (TextView)view.findViewById(R.id.hoursTextView);
        htv.setEnabled(true);
        TextView mtv = (TextView)view.findViewById(R.id.minutesTextView);
        mtv.setEnabled(true);

        builder.setView(view);

        // Configure the OK and Cancel buttons
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                durationListener.onDone(hourPossibleValues[hours.getValue()], minutePossibleValues[minutes.getValue()]);
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog - do nothing
            }
        });

        return builder.create();
    }

    /**
     * Captures the Interface implementation
     * @param context The active context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.durationListener = (OnDurationInputDoneListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDurationInputDoneListener");
        }
    }

    /**
     * This listener is used to communicate with the LogFitbitActivity whenever the user chooses OK
     * in the dialog.
     */
    public  interface OnDurationInputDoneListener {
        void onDone(String hours, String minutes);
    }

    private OnDurationInputDoneListener durationListener;
}