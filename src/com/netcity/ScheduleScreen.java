package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleScreen extends Fragment {
	
	//Описание переменных
	
	LinearLayout llSchedule1, llSchedule2;
	
	//Массивы строк
	String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"}; //Дни недели (Скоро не понадобится)
	String[][] lessons = new String[6][15]; //Массив с уроками
	String[][] lessonsNums = new String[6][15]; //Массив с номерами уроков
	
	//JSON
	JSONObject json = new JSONObject(); //JSON для оценок
	
	Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday;
	
	int day = 0;
	
	//Вызывается при создании активити
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen, null);
		
		llSchedule1 = (LinearLayout) v.findViewById(R.id.ll_schedule_1);
		llSchedule2 = (LinearLayout) v.findViewById(R.id.ll_schedule_2);
		
		btnMonday = (Button) v.findViewById(R.id.btn_monday);
		btnTuesday = (Button) v.findViewById(R.id.btn_tuesday);
		btnWednesday = (Button) v.findViewById(R.id.btn_wednesday);
		btnThursday = (Button) v.findViewById(R.id.btn_thursday);
		btnFriday = (Button) v.findViewById(R.id.btn_friday);
		btnSaturday = (Button) v.findViewById(R.id.btn_saturday);
		
		getData(); //Вызываем функцию получения данных
		
		day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
		
		if (day == -1) {
			day = 5;
		}
		return v;
	}
	
	//Функция вывода расписания на экран
	public void showSchedule() {
		//TODO вывод расписания на экран
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
		} else {
			TextView tvDay1 = new TextView(getActivity());
			tvDay1.setText("Day" + day);
			llSchedule1.addView(tvDay1);
			
			byte count1 = 0; //Счетчик отвечающий за смену
			
			for (int i = 0; i < 15; i += 1) { //Цикл заполнения LinearLayout'ов расписанием
				if (lessonsNums[day][i].equals("1.")) { //Если это первый урок в смене
					count1 += 1; //То увеличиваем счетчик на 1
				}
				
				TextView tvLesson = new TextView(getActivity()); //Создаем новый TextView
				tvLesson.setText(lessonsNums[day][i] + "" + lessons[day][i]); //Заполняем TextView номером урока и уроком
				tvLesson.setTextSize(18); //Изменяем размер текста
				
				if (count1 == 2) {
					llSchedule2.addView(tvLesson);
				} else {
					llSchedule1.addView(tvLesson);
				}
			}
		}
	}

	public void getData(){
		Connector connection = new Connector();
		connection.execute("http://195.88.220.90/v1/login/srv_list"); //TODO
		try {
			json = connection.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	class Connector extends AsyncTask<String,Void,JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]); //TODO подстановка нужного IP
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


