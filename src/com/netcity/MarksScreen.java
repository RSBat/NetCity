package com.netcity;

import android.os.AsyncTask;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MarksScreen extends Activity {

	//Кнопки
	Button btnSchedule, btnNews;
	
	int nOfLessons = 20;
	int month1Days = 26;
	int month2Days = 27;
	int month3Days = 24;
	String[] months = {"Декабрь", "Январь", "Февраль"};
	String[][] marks = { {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""},
	{"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""}
	};
	String[][] daysInMonth = { {"","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","","","","",""},
			{"","","","","","","","","","","","","","","","","","","","","","","",""}
	};
	String[] marksAvg = {"","","","","","","","","","","","","","","","","","","",""};
	TableLayout tlLessons, tlMarks, tlAverage;
	TableRow trMonth1, trMonth2, trMonth3;
	TextView tvMonth1, tvMonth2, tvMonth3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marks_screen);
		
		final Intent intNews = new Intent(this, NewsScreen.class);
		
		btnSchedule = (Button) findViewById(R.id.btn_marks_schedule);
		btnNews = (Button) findViewById(R.id.btn_marks_news);
		
		tlMarks = (TableLayout) findViewById(R.id.tl_marks);
		tlLessons = (TableLayout) findViewById(R.id.tl_lessons);
		tlAverage = (TableLayout) findViewById(R.id.tl_avg);
		
		trMonth1 = (TableRow) findViewById(R.id.tr_month1);
		trMonth2 = (TableRow) findViewById(R.id.tr_month2);
		trMonth3 = (TableRow) findViewById(R.id.tr_month3);
		
		tvMonth1 = (TextView) findViewById(R.id.tv_month1);
		tvMonth2 = (TextView) findViewById(R.id.tv_month2);
		tvMonth3 = (TextView) findViewById(R.id.tv_month3);
		
		OnClickListener onCl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId())
				{
				case R.id.btn_marks_news: //Если кнопка перехода на объявления
					startActivity(intNews); //Переходим на экран с объявлениями
					finish();
					break;
				case R.id.btn_marks_schedule: //Если кнопка перехода на расписание
					finish(); //Закрываем активити и попадаем на экран с расписанием
					break;
				}
			}
			
		};
		
		btnSchedule.setOnClickListener(onCl);
		btnNews.setOnClickListener(onCl);
		
		showMarks();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.marks_screen, menu);
		return true;
	}

	public void getData() {
		parce("");
	}
	
	public void parce(String str) {
		
	}
	
	public void showMarks() {
		for (int i = 0; i < nOfLessons / 2; i += 1) {
			{
				TableRow tr = new TableRow(this);
				TextView tv = new TextView(this);
				tv.setText("Название урока");
				tr.addView(tv);
				tlLessons.addView(tr);
			}
			{
				TableRow tr = new TableRow(this);
				TextView tv = new TextView(this);
				tv.setText("Название урока (длинное)");
				tr.addView(tv);
				tlLessons.addView(tr);
			}
		}
		
		{//Выводим дни
			{
				tvMonth1.setText(months[0]);
				for (int i = 1; i < month1Days + 1; i += 1) {
					TextView tv = new TextView(this);
					tv.setText("| " + i + " ");
					trMonth1.addView(tv);
				}
			}
			{
				tvMonth2.setText(months[1]);
				for (int i = 1; i < month2Days + 1; i += 1) {
					TextView tv = new TextView(this);
					tv.setText("| " + i + " ");
					trMonth2.addView(tv);
				}
			}
			{
				tvMonth3.setText(months[2]);
				for (int i = 1; i < month3Days + 1; i += 1) {
					TextView tv = new TextView(this);
					tv.setText("| " + i + " ");
					trMonth3.addView(tv);
				}
			}
		}
		
		//Выводим оценки
		for (int i = 0; i < nOfLessons; i += 1) {
			TableRow tr = new TableRow(this);
			
			for (int j = 0; j < month1Days + month2Days + month3Days; j += 1) {
				TextView tv = new TextView(this);
				tv.setText(marks[i][j]);
				tr.addView(tv);
			}
			
			tlMarks.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
		
		for (int i = 0; i < nOfLessons; i += 1) {
			TableRow tr = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setText("5");
			tr.addView(tv);
			tlAverage.addView(tr);
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
