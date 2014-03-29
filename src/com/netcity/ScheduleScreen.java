package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.lang.Character;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleScreen extends Fragment {
	
	//Описание переменных
	
	LinearLayout llSchedule1, llSchedule2;
	
	TextView tvDayOfWeek;
	
	//Массивы строк
	String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"}; //Дни недели
	String[][] lessons = new String[6][15]; //Массив с уроками
	String[][] lessonsNums = new String[6][15]; //Массив с номерами уроков
	
	//JSON
	JSONArray json;// = new JSONObject(); //JSON для оценок
	
	Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday;
	
	int day = 0;
	
	//Вызывается при создании активити
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen, null);
		
		llSchedule1 = (LinearLayout) v.findViewById(R.id.ll_schedule_1);
		llSchedule2 = (LinearLayout) v.findViewById(R.id.ll_schedule_2);
		
		tvDayOfWeek = (TextView) v.findViewById(R.id.tv_dayOfWeek);
		
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
				switch (btn.getId())
				{
				case R.id.btn_monday:
					day = 0;
					break;
				
				case R.id.btn_tuesday:
					day = 1;
					break;
					
				case R.id.btn_wednesday:
					day = 2;
					break;
					
				case R.id.btn_thursday:
					day = 3;
					break;
					
				case R.id.btn_friday:
					day = 4;
					break;
					
				case R.id.btn_saturday:
					day = 5;
					break;
				}
				setBtnEnabled((Button) btn);
				showSchedule();
			}
			
		};
		
		btnMonday.setOnClickListener(onCl);
		btnTuesday.setOnClickListener(onCl);
		btnWednesday.setOnClickListener(onCl);
		btnThursday.setOnClickListener(onCl);
		btnFriday.setOnClickListener(onCl);
		btnSaturday.setOnClickListener(onCl);
		
		getData(); //Вызываем функцию получения данных
		
		day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
		
		if (day == -1) {
			day = 5;
		}
		
		switch (day)
		{
		case 0:
			btnMonday.setEnabled(false);
			break;
			
		case 1:
			btnTuesday.setEnabled(false);
			break;
			
		case 2:
			btnWednesday.setEnabled(false);
			break;
			
		case 3:
			btnThursday.setEnabled(false);
			break;
			
		case 4:
			btnFriday.setEnabled(false);
			break;
			
		case 5:
			btnSaturday.setEnabled(false);
			break;
		}
		
		showSchedule();
		
		return v;
	}
	
	//Функция вывода расписания на экран
	public void showSchedule() {
		//TODO вывод расписания на экран
		llSchedule1.removeAllViews();
		llSchedule2.removeAllViews();
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {			
			JSONObject jsonDay;
			try {
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
				
				tvDayOfWeek.setText(days[day]);
				
				for (int i = 0; i < jsonLessons.length(); i += 1) {
					JSONObject jsonLes;
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity());
						tvLesson.setText(jsonLes.optInt("num") + ". " + jsonLes.optString("name"));
						tvLesson.setTextSize(18);
						llSchedule1.addView(tvLesson);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			} //TODO Change to getJSON an add try/catch
			
		} else {
			JSONObject jsonDay;
			try {
				jsonDay = json.getJSONObject(day);
				JSONArray jsonLessons = jsonDay.getJSONArray("lessons");
				
				tvDayOfWeek.setText(days[day]);
				
				byte count1 = 0; //Счетчик отвечающий за смену
				
				for (int i = 0; i < jsonLessons.length(); i += 1) { //Цикл заполнения LinearLayout'ов расписанием
					JSONObject jsonLes;
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity()); //Создаем новый TextView
						tvLesson.setText(jsonLes.optInt("num") + ". " + jsonLes.optString("name")); //Заполняем TextView номером урока и уроком
						tvLesson.setTextSize(18); //Изменяем размер текста
					
						if(jsonLes.optInt("num") == 1) {
							count1++;
						}
						
						if (count1 > 1) {
							llSchedule2.addView(tvLesson);
						} else {
							llSchedule1.addView(tvLesson);
						}
					} catch(JSONException e) {
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
	
	public void setBtnEnabled(Button btn) {
		btnMonday.setEnabled(true);
		btnTuesday.setEnabled(true);
		btnWednesday.setEnabled(true);
		btnThursday.setEnabled(true);
		btnFriday.setEnabled(true);
		btnSaturday.setEnabled(true);
		
		btn.setEnabled(false);
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


