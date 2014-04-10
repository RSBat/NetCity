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

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author Сергей
 * 
 */
public class ScheduleScreen extends Fragment {
	
	//Описание переменных
	
	LinearLayout llSchedule;
	
	TextView tvDayOfWeek;
	
	//Массивы строк
	String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"}; //Дни недели
	
	//JSON
	JSONArray json; //JSON для расписания
	JSONArray jsonWeeks; //JSON для списка недель
	
	Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday;
	Button btnWeekNext, btnWeekPrev;
	
	int day = 0;
	int weekNum = 0;
	String week = "";
	
	/**
	 * <p><b>Главный метод</b></p>
	 * <p>1) Создает View из .xml файла. <br/>
	 * 2) Находит нужные нам View <br/>
	 * 3) Задает кнопкам обработчик нажатия который изменяет значение глобальной переменной (int) day на соответствующее выбранному дню <br/>
	 * 4) Определяем сегодняшний день и заносим его в глобальную переменную (int) day <br/>
	 * 5) В зависимости от дня недели выключаем соответствующую кнопку <br/>
	 * 6) Создаем поток в котором получаем расписание и выводим его на экран</p> 
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.schedule_screen, null);
		
		llSchedule = (LinearLayout) v.findViewById(R.id.ll_schedule);
		
		tvDayOfWeek = (TextView) v.findViewById(R.id.tv_dayOfWeek);
		
		btnMonday = (Button) v.findViewById(R.id.btn_monday);
		btnTuesday = (Button) v.findViewById(R.id.btn_tuesday);
		btnWednesday = (Button) v.findViewById(R.id.btn_wednesday);
		btnThursday = (Button) v.findViewById(R.id.btn_thursday);
		btnFriday = (Button) v.findViewById(R.id.btn_friday);
		btnSaturday = (Button) v.findViewById(R.id.btn_saturday);
		
		btnWeekNext = (Button) v.findViewById(R.id.btn_nextWeek);
		btnWeekPrev = (Button) v.findViewById(R.id.btn_prevWeek);
		
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
		
		btnWeekNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangeWeek gCh = new ChangeWeek();
				gCh.execute(1);
				Toast.makeText(getActivity(), "working", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		btnWeekPrev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangeWeek gCh = new ChangeWeek();
				gCh.execute(0);
				Toast.makeText(getActivity(), "working", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
		
		if (day == -1) {
			day = 0;
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
		
		GetSchedule getSchedule = new GetSchedule();
		getSchedule.execute();
		
		return v;
	}
	
	/**
	 * <p><b>Функция показа расписания</b></p>
	 * <p>Получает день недели из глобальной переменной (int) day и заполняет экран данными из глобального (JSONArray) json</p>
	 */
	public void showSchedule() {
		//TODO вывод расписания на экран
		llSchedule.removeAllViews();
		
		//LinearLayout llSchedule1 = new LinearLayout(getActivity());
		//LinearLayout llSchedule2 = new LinearLayout(getActivity());
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {	
			JSONObject jsonDay;
			try {
				LinearLayout llSchedule1 = new LinearLayout(getActivity());
				llSchedule1.setOrientation(1);
				
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
				llSchedule.addView(llSchedule1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		} else {
			JSONObject jsonDay;
			try {
				LayoutParams llPar = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
				LinearLayout llSchedule1 = new LinearLayout(getActivity());
				LinearLayout llSchedule2 = new LinearLayout(getActivity());
				llSchedule1.setOrientation(1);
				llSchedule2.setOrientation(1);
				llSchedule1.setLayoutParams(llPar);
				llSchedule2.setLayoutParams(llPar);
				
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
				
				llSchedule.addView(llSchedule1);
				llSchedule.addView(llSchedule2);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * <p><b>Выключение кнопки</b></p>
	 * <p>Выключает нужную кнопку выбора дня при этом включая все остальные</p>
	 * @param btn Кнопка которую надо выключить
	 */
	public void setBtnEnabled(Button btn) {
		btnMonday.setEnabled(true);
		btnTuesday.setEnabled(true);
		btnWednesday.setEnabled(true);
		btnThursday.setEnabled(true);
		btnFriday.setEnabled(true);
		btnSaturday.setEnabled(true);
		
		btn.setEnabled(false);
	}
	
	class GetSchedule extends AsyncTask<Void,Void,String> {
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SharedPreferences sPref = getActivity().getSharedPreferences("NetCity", getActivity().MODE_PRIVATE);
			
			ServerRequest sReqSchedule = new ServerRequest();
			ServerRequest sReqWeeks = new ServerRequest();
			
			sReqWeeks.execute("http://195.88.220.90/v1/schedule/week_list", "", "true", sPref.getString("token", "None"));
			
			try {				
				jsonWeeks = new JSONArray(sReqWeeks.get());
				
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
				
				sReqSchedule.execute("http://195.88.220.90/v1/schedule/week", "/?date=" + week, "true", sPref.getString("token", "None"));
				json = new JSONArray(sReqSchedule.get());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				} else {
					Toast.makeText(getActivity(), "Не удается установить соединение с сервером. Пожайлуста проверьте интернет-соединение", Toast.LENGTH_LONG).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	class ChangeWeek extends AsyncTask<Integer, Void, String> {

		@Override
		protected String doInBackground(Integer... params) { //0 - пред неделя 1 - след неделя
			if (params[0] == 1) {
				weekNum -=1;
			} else if (params[0] == 0) {
				weekNum += 1;
			}
			
			try {
				week = (String) jsonWeeks.getJSONObject(weekNum-1).keys().next();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SharedPreferences sPref = getActivity().getSharedPreferences("NetCity", getActivity().MODE_PRIVATE);
			ServerRequest sReq = new ServerRequest();
			sReq.execute("http://195.88.220.90/v1/schedule/week", "/?date=" + week, "true", sPref.getString("token", "None"));
			String result = null;
			try {
				result = sReq.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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


