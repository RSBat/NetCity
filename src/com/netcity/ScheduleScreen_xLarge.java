package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.netcity.ScheduleScreen.Connector;

import android.content.res.Configuration;
import android.os.AsyncTask;
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

	String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
	
	Button btnMondayTuesday, btnWednesdayThursday, btnFridaySaturday;
	
	LinearLayout llSchedule1, llSchedule2;
	
	TextView tvDay1, tvDay2;
	
	JSONArray json = null;
	
	int day;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen_xlarge, null);
		
		btnMondayTuesday = (Button) v.findViewById(R.id.btn_monday_tuesday);
		btnWednesdayThursday = (Button) v.findViewById(R.id.btn_wednesday_thursday);
		btnFridaySaturday = (Button) v.findViewById(R.id.btn_friday_saturday);
		
		tvDay1 = (TextView) v.findViewById(R.id.tv_dayOfWeek1);
		tvDay2 = (TextView) v.findViewById(R.id.tv_dayOfWeek2);
		
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
		
		getData();
		
		showSchedule();
		
		return v;
	}

	private void showSchedule() {
		llSchedule1.removeAllViews();
		llSchedule2.removeAllViews();
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			
			JSONObject jsonDay;
			try {
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
			
				tvDay1.setText(days[day]);
			
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
				
				tvDay2.setText(days[day + 1]);
			
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
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			

			JSONObject jsonDay;
			try {
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
				
				tvDay1.setText(days[day]);
			
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
			
				tvDay2.setText(days[day + 1]);
			
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
	
	public void getData(){
		Connector connection = new Connector();
		connection.execute("http://195.88.220.90/v1/login/srv_list"); //TODO
		try {
			json = new JSONArray("[{day = \"\u041f\u043d\";lessons = [{name = \"\u041c\u0430\u0442\u0435\u043c.\";num = 1},{name = \"\u0424\u0438\u0437-\u0440\u0430\";num = 2},{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 3},{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 4},{name = \"\u0411\u0438\u043e\u043b\u043e\u0433\u0438\u044f\";num = 5},{name = \"\u0413\u0435\u043e\u0433\u0440\u0430\u0444\u0438\u044f\";num = 6},{name = \"-\";num = 1},{name = \"-\";num = 2},{name = \"-\";num = 3},{name = \"-\";num = 4},{name = \"-\";num = 5},{name = \"-\";num = 6}]},{day = \"\u0412\u0442\";lessons = [{name = \"\u0418\u043d.\u044f\u0437./\u042f\u043f.\";num = 1},{name = \"\u0418\u043d.\u044f\u0437./\u042f\u043f.\";num = 2},{name = \"\u041c\u0430\u0442\u0435\u043c.\";num = 3},{name = \"\u041c\u0430\u0442\u0435\u043c.\";num = 4},{name = \"\u0420\u0443\u0441.\u044f\u0437.\";num = 5},{name = \"\u041b\u0438\u0442-\u0440\u0430\";num = 6},{name = \"-\";num = 7},{name = \"-\";num = 1},{name = \"-\";num = 2},{name = \"-\";num = 3},{name = \"-\";num = 4},{name = \"-\";num = 5},{name = \"-\";num = 6}]},{day = \"\u0421\u0440\";lessons = [{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 1},{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 2},{name = \"\u041b\u0438\u0442-\u0440\u0430\";num = 3},{name = \"\u041b\u0438\u0442-\u0440\u0430\";num = 4},{name = \"\u041c\u0430\u0442\u0435\u043c.\";num = 5},{name = \"\u041c\u0430\u0442\u0435\u043c.\";num = 6},{name = \"\u0424\u0438\u0437-\u0440\u0430\";num = 7},{name = \"-\";num = 1},{name = \"-\";num = 2},{name = \"-\";num = 3},{name = \"-\";num = 4},{name = \"-\";num = 5}]},{day = \"\u0427\u0442\";lessons = [{name = \"\u0424\u0438\u0437-\u0440\u0430\";num = 1},{name = \"\u0424\u0438\u0437-\u0440\u0430\";num = 2},{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 3},{name = \"\u0410\u043d\u0433.\u044f\u0437.\";num = 4},{name = \"-\";num = 5},{name = \"-\";num = 6},{name = \"-\";num = 7},{name = \"-\";num = 1},{name = \"-\";num = 2},{name = \"-\";num = 3},{name = \"-\";num = 4},{name = \"-\";num = 5},{name = \"-\";num = 6}]},{day = \"\u041f\u0442\";lessons = [{name = \"-\";num = 0},{name = \"\u041e\u0431\u0449\u0435\u0441\u0442.\";num = 1},{name = \"\u041e\u0431\u0449\u0435\u0441\u0442.\";num = 2},{name = \"\u0423/\u043f\u0440 \u0410\u043d\u0433.\";num = 3},{name = \"\u0423/\u043f\u0440 \u0410\u043d\u0433.\";num = 4},{name = \"\u0425\u0438\u043c\u0438\u044f\";num = 5},{name = \"\u042d\u043b./\u0410\u043d\u0433., \u042d\u043b./\u0424\u0438\u0437, \u042d\u043b./\u041e\u0431\u0449., \u042d\u043b./\u041c\u0430\u0442., \u042d\u043b./\u0418\u0441\u0442.\";num = 6},{name = \"-\";num = 1},{name = \"-\";num = 2},{name = \"-\";num = 3},{name = \"-\";num = 4},{name = \"-\";num = 5}]},{day = \"\u0421\u0431\";lessons = [{name = \"\u041c/\u043a\u0443\u0440\u0441\";num = 1},{name = \"\u041e\u0411\u0416\";num = 2},{name = \"\u0424\u0438\u0437\u0438\u043a\u0430\";num = 3},{name = \"\u0424\u0438\u0437\u0438\u043a\u0430\";num = 4},{name = \"\u0418\u043d\u0444\u043e\u0440.\";num = 5},{name = \"-\";num = 6}]}]");// = connection.get();
		/*} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	class Connector extends AsyncTask<String,Void,JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			HttpResponse response;
			HttpEntity entity;
			InputStream ins;
			
			
			try {
				response = client.execute(httpGet);
				entity = response.getEntity();
				ins = entity.getContent();
				try {
					String result;
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256);
					StringBuilder sb = new StringBuilder();
					String line = null;
					
					while ((line = reader.readLine()) != null)  sb.append(line); 
			    		result = sb.toString();
			    	ins.close();
			    	return new JSONObject(result);
				} catch (Exception e) {}
			} catch (ClientProtocolException e) { //TODO
			} catch (IOException e) { //Ошибка когда нет соединения TODO
				return null;
			}
			return null;
		}
		
	}
}
