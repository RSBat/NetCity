package com.netcity;

import java.util.Calendar;

import org.json.JSONObject;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleScreen_xLarge extends Fragment {

	Button btnMondayTuesday, btnWednesdayThursday, btnFridaySaturday;
	
	LinearLayout llSchedule1, llSchedule2;
	
	JSONObject jsonMain = null;
	
	int day;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen_xlarge, null);
		
		btnMondayTuesday = (Button) v.findViewById(R.id.btn_monday_tuesday);
		btnWednesdayThursday = (Button) v.findViewById(R.id.btn_wednesday_thursday);
		btnFridaySaturday = (Button) v.findViewById(R.id.btn_friday_saturday);
		
		llSchedule1 = (LinearLayout) v.findViewById(R.id.ll_schedule_xlarge_1);
		llSchedule2 = (LinearLayout) v.findViewById(R.id.ll_schedule_xlarge_2);
		
		OnClickListener onCl = new OnClickListener() {

			@Override
			public void onClick(View btn) {
				// TODO Auto-generated method stub
				switch (btn.getId())
				{
				case R.id.btn_monday_tuesday:
					day = 0;
					break;
				
				case R.id.btn_wednesday_thursday:
					day = 2;
					break;
					
				case R.id.btn_friday_saturday:
					day = 4;
					break;
				}
				setBtnEnabled((Button) btn);
				showSchedule();
			}
			
		};
		
		btnMondayTuesday.setOnClickListener(onCl);
		btnWednesdayThursday.setOnClickListener(onCl);
		btnFridaySaturday.setOnClickListener(onCl);
		
		day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
		
		if (day == -1) {
			day = 4;
			setBtnEnabled(btnFridaySaturday);
		}
		else if (day % 2 == 1) {
			day -= 1;
		}
		
		showSchedule();
		
		return v;
	}

	private void showSchedule() {
		llSchedule1.removeAllViews();
		llSchedule2.removeAllViews();
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			TextView tvDay1 = new TextView(getActivity());
			tvDay1.setText("Day" + day);
			llSchedule1.addView(tvDay1);
			
			for (int i = 0; i < 10; i += 1) {
				TextView tvLesson = new TextView(getActivity());
				tvLesson.setText(i + ". Lesson");
				tvLesson.setTextSize(20);
				llSchedule1.addView(tvLesson);
			}
			
			TextView tvDay2 = new TextView(getActivity());
			tvDay2.setText("Day " + (day + 1));
			llSchedule1.addView(tvDay2);
			
			for (int i = 0; i < 10; i += 1) {
				TextView tvLesson = new TextView(getActivity());
				tvLesson.setText(i + ". Lesson");
				tvLesson.setTextSize(20);
				llSchedule1.addView(tvLesson);
			}
		} else {
			TextView tvDay1 = new TextView(getActivity());
			tvDay1.setText("Day" + day);
			llSchedule1.addView(tvDay1);
			
			for (int i = 0; i < 10; i += 1) {
				TextView tvLesson = new TextView(getActivity());
				tvLesson.setText(i + ". Lesson");
				tvLesson.setTextSize(20);
				llSchedule1.addView(tvLesson);
			}
			
			TextView tvDay2 = new TextView(getActivity());
			tvDay2.setText("Day " + (day + 1));
			llSchedule2.addView(tvDay2);
			
			for (int i = 0; i < 10; i += 1) {
				TextView tvLesson = new TextView(getActivity());
				tvLesson.setText(i + ". Lesson");
				tvLesson.setTextSize(20);
				llSchedule2.addView(tvLesson);
			}
		}
	}
	
	private void setBtnEnabled(Button btn) {
		btnMondayTuesday.setEnabled(true);
		btnWednesdayThursday.setEnabled(true);
		btnFridaySaturday.setEnabled(true);
		btn.setEnabled(false);
	}
}
