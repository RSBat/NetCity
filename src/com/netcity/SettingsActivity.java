package com.netcity;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class SettingsActivity extends PreferenceActivity {
	
	Preference prefTime;
	OnPreferenceClickListener onCl;
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        prefTime = (Preference) getPreferenceManager().findPreference("pref_notif_time");
        
        onCl = new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference pref) {
				if (pref.getKey().equals("pref_notif_time")) {
					showTimePicker();
				}
				return false;
			}
        };
    }
	
	public void showTimePicker() {
		DialogFragment newFragment = new TimePickerFragment();
	    //newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
		}
	}
}
