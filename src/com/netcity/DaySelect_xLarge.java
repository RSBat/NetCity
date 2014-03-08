package com.netcity;

import com.netcity.DaySelect.scheduleShow;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DaySelect_xLarge extends Fragment {

	public interface scheduleShowL {
		public void showSchedule(int mode, int day);
	}
	
	scheduleShow scSh;
	Button btnMondayTuesday, btnWednesdayThursday, btnFridaySaturday;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			scSh = (scheduleShow) activity;
		} catch (ClassCastException e) {
			
		}
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.day_select_xlarge, null);
		
		btnMondayTuesday = (Button) v.findViewById(R.id.btn_monday_tuesday);
		btnWednesdayThursday = (Button) v.findViewById(R.id.btn_wednesday_thursday);
		btnFridaySaturday = (Button) v.findViewById(R.id.btn_friday_saturday);
		
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
				case R.id.btn_monday_tuesday:
					if (portr) {
						scSh.showSchedule(2, 0);
					} else {
						scSh.showSchedule(3, 0);
					}
					setBtnEnabled((Button) btn);
					break;
				
				case R.id.btn_wednesday_thursday:
					if (portr) {
						scSh.showSchedule(2, 2);
					} else {
						scSh.showSchedule(3, 2);
					}
					setBtnEnabled((Button) btn);
					break;
					
				case R.id.btn_friday_saturday:
					if (portr) {
						scSh.showSchedule(2, 4);
					} else {
						scSh.showSchedule(3, 4);
					}
					setBtnEnabled((Button) btn);
					break;
				}
			}
			
		};
		
		btnMondayTuesday.setOnClickListener(onCl);
		btnWednesdayThursday.setOnClickListener(onCl);
		btnFridaySaturday.setOnClickListener(onCl);
		
		return v;
	}
	
	public void setBtnEnabled(Button btn) {
		btnMondayTuesday.setEnabled(true);
		btnWednesdayThursday.setEnabled(true);
		btnFridaySaturday.setEnabled(true);
		
		btn.setEnabled(false);
	}
}
