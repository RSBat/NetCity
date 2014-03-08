package com.netcity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DaySelect extends Fragment {

	public interface scheduleShow {
		public void showSchedule(int mode, int day);
	}
	
	scheduleShow scSh;
	Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			scSh = (scheduleShow) activity;
		} catch (ClassCastException e) {
			
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.day_select, null);
		btnMonday = (Button) v.findViewById(R.id.btn_monday);
		btnTuesday = (Button) v.findViewById(R.id.btn_tuesday);
		btnWednesday = (Button) v.findViewById(R.id.btn_wednesday);
		btnThursday = (Button) v.findViewById(R.id.btn_thursday);
		btnFriday = (Button) v.findViewById(R.id.btn_friday);
		btnSaturday = (Button) v.findViewById(R.id.btn_saturday);
		
		OnClickListener onCl = new OnClickListener() {

			@Override
			public void onClick(View btn) {
				// TODO Auto-generated method stub
				boolean portr = true;
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					portr = false;
				}
				switch (btn.getId())
				{
				case R.id.btn_monday:
					if (portr) {
						scSh.showSchedule(0, 0);
					} else {
						scSh.showSchedule(1, 0);
					}
					break;
					
				case R.id.btn_tuesday:
					if (portr) {
						scSh.showSchedule(0, 1);
					} else {
						scSh.showSchedule(1, 1);
					}
					break;
					
				case R.id.btn_wednesday:
					if (portr) {
						scSh.showSchedule(0, 2);
					} else {
						scSh.showSchedule(1, 2);
					}
					break;
					
				case R.id.btn_thursday:
					if (portr) {
						scSh.showSchedule(0, 3);
					} else {
						scSh.showSchedule(1, 3);
					}
					break;
				
				case R.id.btn_friday:
					if (portr) {
						scSh.showSchedule(0, 4);
					} else {
						scSh.showSchedule(1, 4);
					}
					break;
				
				case R.id.btn_saturday:
					if (portr) {
						scSh.showSchedule(0, 5);
					} else {
						scSh.showSchedule(1, 5);
					}
					break;
				}
				setBtnEnabled((Button) btn);
			}
		};
		
		btnMonday.setOnClickListener(onCl);
		btnTuesday.setOnClickListener(onCl);
		btnWednesday.setOnClickListener(onCl);
		btnThursday.setOnClickListener(onCl);
		btnFriday.setOnClickListener(onCl);
		btnSaturday.setOnClickListener(onCl);
		
		return v;
	}
	
	private void setBtnEnabled(Button btn) {
		btnMonday.setEnabled(true);
		btnTuesday.setEnabled(true);
		btnWednesday.setEnabled(true);
		btnThursday.setEnabled(true);
		btnFriday.setEnabled(true);
		btnSaturday.setEnabled(true);
		
		btn.setEnabled(false);
	}
}
