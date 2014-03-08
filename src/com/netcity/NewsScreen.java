package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NewsScreen extends Activity {

	//Buttons
	Button btnSchedule, btnMarks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_screen);
		
		btnSchedule = (Button) findViewById(R.id.btn_news_schedule);
		btnMarks = (Button) findViewById(R.id.btn_news_marks);
		
		final Intent intMarks = new Intent(this, MarksScreen.class);
		
		OnClickListener onCl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId())
				{
				case R.id.btn_news_marks: //Если кнопка перехода на оценки
					startActivity(intMarks); //Переходим на экран с оценками
					finish();
					break;
				case R.id.btn_news_schedule: //Если кнопка перехода на расписание
					finish(); //Закрываем активити и попадаем на экран с расписанием
					break;
				}
			}
			
		};
		
		btnSchedule.setOnClickListener(onCl);
		btnMarks.setOnClickListener(onCl);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_screen, menu);
		return true;
	}

	public void getData() {
		parce("");
	}
	
	public void parce(String str) {
		
	}
	
	public void showNews() {
		
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
