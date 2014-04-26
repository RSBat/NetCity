package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.netcity.ScheduleScreen.ChangeWeek;
import com.netcity.ScheduleScreen.GetSchedule;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @author Сергей
 *
 */
public class ScheduleScreen_xLarge extends Fragment {

	LinearLayout llSchedule;
	
	TextView tvDayOfWeek1, tvDayOfWeek2;
	TextView tvDate1, tvDate2;
	
	String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
	
	JSONArray json;
	JSONArray jsonWeeks;
	
	Button btnMondayTuesday, btnWednesdayThursday, btnFridaySaturday;
	Button btnWeekNext, btnWeekPrev;
	
	int day;
	int weekNum = 0;
	String week = "";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen_xlarge, null);
		
		GetSchedule getSchedule = new GetSchedule();
		getSchedule.execute();
		
		btnMondayTuesday = (Button) v.findViewById(R.id.btn_monday_tuesday);
		btnWednesdayThursday = (Button) v.findViewById(R.id.btn_wednesday_thursday);
		btnFridaySaturday = (Button) v.findViewById(R.id.btn_friday_saturday);
		
		tvDayOfWeek1 = (TextView) v.findViewById(R.id.tv_dayOfWeek1);
		tvDayOfWeek2 = (TextView) v.findViewById(R.id.tv_dayOfWeek2);
		
		tvDate1 = (TextView) v.findViewById(R.id.tv_date1);
		tvDate2 = (TextView) v.findViewById(R.id.tv_date2);
		
		llSchedule = (LinearLayout) v.findViewById(R.id.ll_schedule);
		
		btnWeekNext = (Button) v.findViewById(R.id.btn_nextWeek);
		btnWeekPrev = (Button) v.findViewById(R.id.btn_prevWeek);
		
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
		
		btnWeekNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangeWeek gCh = new ChangeWeek();
				gCh.execute(1);
			}		
		});
		
		btnWeekPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangeWeek gCh = new ChangeWeek();
				gCh.execute(0);
			}
			
		});
		
		
		day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
		
		if (day == -1) {
			day = 4;
		}
		else if (day % 2 == 1) {
			day -= 1;
		}
		
		switch (day)
		{
		case 0:
			setBtnEnabled(btnMondayTuesday);
			break;
			
		case 2:
			setBtnEnabled(btnWednesdayThursday);
			break;
			
		case 4:
			setBtnEnabled(btnFridaySaturday);
			break;
		}
		
		return v;
	}

	private void showSchedule() {
		llSchedule.removeAllViews();
		
		// Берем дату понедельника этой недели переводим в Calendar изменяем на нужный день недели и выводим строкой
		SimpleDateFormat  dateFormat = new SimpleDateFormat("dd.MM.yy");    
				
		try {
			Date dateMonday = (Date) dateFormat.parse(week);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateMonday);
			cal.add(Calendar.DAY_OF_WEEK, day);
			tvDate1.setText(dateFormat.format(cal.getTime()));
			cal.add(Calendar.DAY_OF_WEEK, 1);
			tvDate2.setText(dateFormat.format(cal.getTime()));
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			
			JSONObject jsonDay;
			try {
				LayoutParams llPar = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
				LinearLayout llSchedule1 = new LinearLayout(getActivity());
				LinearLayout llSchedule2 = new LinearLayout(getActivity());
				llSchedule1.setOrientation(1);
				llSchedule2.setOrientation(1);
				llSchedule1.setLayoutParams(llPar);
				llSchedule2.setLayoutParams(llPar);
				
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
			
				tvDayOfWeek1.setText(days[day]);
			
				for (int i = 0; i < jsonLessons.length(); i += 1) {
					
					JSONObject jsonLes;
					
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity());
						tvLesson.setText(jsonLes.optInt("num", i) + ". " + jsonLes.optString("name", "----------"));
						tvLesson.setTextSize(20);
						llSchedule1.addView(tvLesson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			
				jsonDay = json.getJSONObject(day+1);
				jsonLessons = jsonDay.getJSONArray("lessons");
				
				tvDayOfWeek2.setText(days[day + 1]);
			
				for (int i = 0; i < 10; i += 1) {
					
					JSONObject jsonLes;
					
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity());
						tvLesson.setText(jsonLes.optInt("num", i) + ". " + jsonLes.optString("name", "----------"));
						tvLesson.setTextSize(20);
						llSchedule1.addView(tvLesson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				
				llSchedule.addView(llSchedule1);
				llSchedule.addView(llSchedule2);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			JSONObject jsonDay;
			try {
				LayoutParams llPar = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
				LinearLayout llSchedule1 = new LinearLayout(getActivity());
				LinearLayout llSchedule2 = new LinearLayout(getActivity());
				llSchedule1.setOrientation(1);
				llSchedule2.setOrientation(1);
				llSchedule1.setLayoutParams(llPar);
				llSchedule2.setLayoutParams(llPar);
				
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
				
				tvDayOfWeek1.setText(days[day]);
			
				for (int i = 0; i < jsonLessons.length(); i += 1) {
					JSONObject jsonLes;
					
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity());
						tvLesson.setText(jsonLes.optInt("num", i) + ". " + jsonLes.optString("name", "----------"));
						tvLesson.setTextSize(20);
						llSchedule1.addView(tvLesson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				
				jsonDay = json.getJSONObject(day+1);
				jsonLessons = jsonDay.getJSONArray("lessons");
			
				tvDayOfWeek2.setText(days[day + 1]);
			
				for (int i = 0; i < jsonLessons.length(); i += 1) {
					JSONObject jsonLes;
					
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity());
						tvLesson.setText(jsonLes.optInt("num", i) + ". " + jsonLes.optString("name", "----------"));
						tvLesson.setTextSize(20);
						llSchedule2.addView(tvLesson);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				
				llSchedule.addView(llSchedule1);
				llSchedule.addView(llSchedule2);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	private void setBtnEnabled(Button btn) {
		btnMondayTuesday.setEnabled(true);
		btnWednesdayThursday.setEnabled(true);
		btnFridaySaturday.setEnabled(true);
		btn.setEnabled(false);
	}
	
class GetSchedule extends AsyncTask<Void,Void,String> {
		ServerRequest sReqSchedule = new ServerRequest();
		ServerRequest sReqWeeks = new ServerRequest();
		SharedPreferences sPref = getActivity().getSharedPreferences("NetCity", getActivity().MODE_PRIVATE);
		SharedPreferences sPrefSched = getActivity().getSharedPreferences("NetCitySchedule", getActivity().MODE_PRIVATE);
		
		protected void onPreExecute() {
			
			
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			Log.w("MYLOG", "Started");
			
			try {	
				if (sPrefSched.getString("weeks", "None").equals("None")) {
					jsonWeeks = new JSONArray(sReqWeeks.connect("http://195.88.220.90/v1/schedule/week_list", "", true, sPref.getString("token", "None")));
				}
				else {
					jsonWeeks = new JSONArray(sPrefSched.getString("weeks", "[{\"error\":\"error\"}]"));
				}
				
				Log.w("MYLOG", "Req successful");
				
				for (int i = 0; i < jsonWeeks.length(); i++) {
					if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2 == -1) {
						Calendar tomorrow = Calendar.getInstance();
						tomorrow.add(Calendar.DAY_OF_WEEK, 1);
						
						Date dateTomorrow = tomorrow.getTime();
						
						SimpleDateFormat  dateFormat = new SimpleDateFormat("dd.MM.yy");    
						
						String strTomorrow = dateFormat.format(dateTomorrow);
						
						
						if (((String) (jsonWeeks.getJSONObject(i).keys().next())).equals(strTomorrow)) {
							weekNum = i+1;
							week = (String) jsonWeeks.getJSONObject(i).keys().next();
							Log.w("MYLOG", week);
						}
					} else {
						Calendar monday = Calendar.getInstance();
						monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
						
						Date dateMonday = monday.getTime();
						
						SimpleDateFormat  dateFormat = new SimpleDateFormat("dd.MM.yy");    
						
						String strMonday = dateFormat.format(dateMonday);
						StringBuilder sbMonday = new StringBuilder(strMonday);
						if (sbMonday.charAt(0) == '0') {
							sbMonday.deleteCharAt(0);
						}
						strMonday = sbMonday.toString();
						
						Log.w("MYLOG", "----------");
						Log.w("MYLOG", i + "");
						Log.w("MYLOG", strMonday);
						Log.w("MYLOG", (String) (jsonWeeks.getJSONObject(i).keys().next()));
						
						if (((String) (jsonWeeks.getJSONObject(i).keys().next())).equals(strMonday)) {
							weekNum = i+1;
							week = (String) jsonWeeks.getJSONObject(i).keys().next();
							Log.w("MYLOG", "week" + week);
							break;
						}
					}
				}
				
				
				json = new JSONArray(sReqSchedule.connect("http://195.88.220.90/v1/schedule/week", "/?date=" + week, true, sPref.getString("token", "None")));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "OK";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			try {
				if (json.getJSONObject(0).optString("error", "None").equals("None")) {
					showSchedule();
					
					Editor ed = sPrefSched.edit();
					ed.putString("week", week);
					ed.putInt("weekNum", weekNum);
					ed.putString("schedule", json.toString());
					ed.putString("weeks", jsonWeeks.toString());
					ed.commit();
				} else {
					Toast.makeText(getActivity(), "Не удается установить соединение с сервером. Пожайлуста проверьте интернет-соединение", Toast.LENGTH_LONG).show();
					week = sPrefSched.getString("week", "None");
					weekNum = sPrefSched.getInt("weekNum", 0);
					json = new JSONArray(sPrefSched.getString("schedule", "[{\"error\":\"error\"}]"));
					jsonWeeks = new JSONArray(sPrefSched.getString("weeks", "[{\"error\":\"error\"}]"));
					
					Log.w("MYLOG", week + weekNum + json.toString());
					
					showSchedule();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	class ChangeWeek extends AsyncTask<Integer, Void, String> {

		SharedPreferences sPref = getActivity().getSharedPreferences("NetCity", getActivity().MODE_PRIVATE);
		ServerRequest sReq = new ServerRequest();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			llSchedule.removeAllViews();
			
			ProgressBar prbar = new ProgressBar(getActivity());
			
			llSchedule.addView(prbar);
		}
		
		@Override
		protected String doInBackground(Integer... params) { //0 - пред неделя 1 - след неделя
			if (params[0] == 0) {
				weekNum -=1;
			} else if (params[0] == 1) {
				weekNum += 1;
			}
			
			try {
				week = (String) jsonWeeks.getJSONObject(weekNum-1).keys().next();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String result = null;
			
			result = sReq.connect("http://195.88.220.90/v1/schedule/week", "/?date=" + week, true, sPref.getString("token", "None"));
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			try {
				json = new JSONArray(result);
				showSchedule();
				Toast.makeText(getActivity(), week, Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
