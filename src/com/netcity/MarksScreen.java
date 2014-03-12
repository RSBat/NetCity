package com.netcity;

import android.os.AsyncTask;
import android.os.Bundle;

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

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MarksScreen extends Fragment {
	
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
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.marks_screen, null);
		
		tlMarks = (TableLayout) v.findViewById(R.id.tl_marks);
		tlLessons = (TableLayout) v.findViewById(R.id.tl_lessons);
		tlAverage = (TableLayout) v.findViewById(R.id.tl_avg);
		
		trMonth1 = (TableRow) v.findViewById(R.id.tr_month1);
		trMonth2 = (TableRow) v.findViewById(R.id.tr_month2);
		trMonth3 = (TableRow) v.findViewById(R.id.tr_month3);
		
		tvMonth1 = (TextView) v.findViewById(R.id.tv_month1);
		tvMonth2 = (TextView) v.findViewById(R.id.tv_month2);
		tvMonth3 = (TextView) v.findViewById(R.id.tv_month3);
		
		//showMarks(); TODO
		return v;
	}

	public void getData() {
		parce("");
	}
	
	public void parce(String str) {
		
	}
	
	/*public void showMarks() { TODO
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
	}*/
	
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
