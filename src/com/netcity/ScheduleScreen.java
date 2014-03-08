package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcity.DaySelect.scheduleShow;
import com.netcity.DaySelect_xLarge.scheduleShowL;

public class ScheduleScreen extends FragmentActivity implements scheduleShow, scheduleShowL {
	
	//�������� ����������
	
	Button btnMarks;
	
	//������� �����
	String[] days = {"�����������", "�������", "�����", "�������", "�������", "�������"};//
	String[][] lessons = new String[6][15]; //������ � �������
	String[][] lessonsNums = new String[6][15]; //������ � �������� ������
	
	//JSON
	JSONObject json = new JSONObject(); //JSON ��� ������
	
	//���������� ��� �������� ��������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //�������� ����������� �����������
		setContentView(R.layout.activity_schedule_screen); //����������� layout ����
		
		btnMarks = (Button) findViewById(R.id.btn_schedule_marks); //������� ������ ��� �������� �� ������ �� id
		
		final Intent intMarks = new Intent(this, MarksScreen.class); //������� ������ �� �������� � ��������
		
		OnClickListener onCl = new OnClickListener() { //������� ���������� ������� ��� ������

			@Override
			public void onClick(View btn) { //������� �������
				// TODO Auto-generated method stub
				switch (btn.getId()) //����������� ������
				{
				case R.id.btn_schedule_marks: //���� ������ �������� �� ������
					startActivity(intMarks); //��������� �� ����� � ��������
					break;
				}
			}
			
		};
		
		btnMarks.setOnClickListener(onCl); //����������� ���������� ������� ������ �������� �� ������
		
		for (int i = 0; i < 6; i += 1) { //���������� �������� � ������� � �������� � �������� ������ ������� ��������
			for (int j = 0; j < 15; j += 1) {
				lessonsNums[i][j] = "";
				lessons[i][j] = "";
			}
		}
		
		getData(); //�������� ������� ��������� ������
		
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) < 4) { //������� ������� ���� ��� ���������� ���� ������� �����������
			int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
			if (day == -1) {
				day = 0;
			}
			
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				showSchedule(0, day);
			} else {
				showSchedule(1, day);
			}
		} else {
			int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
			if (day == -1) {
				day = 0;
			}
			if (day % 2 == 1) {
				day -= 1;
			}
			
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				showSchedule(2, Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2);
			} else {
				showSchedule(3, Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2);
			}
		}
	}
	
	//������� ������ ���������� �� �����
	public void showSchedule(int mode, int day) {
		//TODO ����� ���������� �� �����
		Fragment frSchList; //�������� � ������� ����� ������������ ����������
		switch (mode)
		{
		case 0: //���� Portrait phone
			frSchList = getSupportFragmentManager().findFragmentById(R.id.schList); //������� ������ �������� �� id
			LinearLayout llSchList = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList1); //�������� LinearLayout �� ���������
			llSchList.removeAllViews(); //������� ��� �������� �������� �� LinearLayout
			
			TextView dayOfWeek = (TextView) frSchList.getView().findViewById(R.id.tvDayOfWeek); //������� TextView ���������� �� ���� ������
			dayOfWeek.setText(days[day]); //������ ����� ��� TextView ����������� �� ���� ������
			
			for (int i = 0; i < 15; i += 1) { //���� ��� ���������� LinearLayout �� ��������� �����������
				TextView tvLesson = new TextView(this); //������� ����� TextVeiw 
				tvLesson.setText(lessonsNums[day][i] + "" + lessons[day][i]); //������ ����� ��� TextView � ���� ������ ����� � ������ �����
				tvLesson.setTextSize(18); //�������� ������ ������
				llSchList.addView(tvLesson); //������ TextView � LinearLayout
			}
			break;
			
		case 1: //���� landscape phone
			frSchList = getSupportFragmentManager().findFragmentById(R.id.schListL); //������� �������� �� id
			LinearLayout llSchList1 = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList1); //������� LinearLayout ��� ������ ������ �����
			LinearLayout llSchList2 = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList2); //������� LinearLayout ��� ������ ������ �����
			TextView dayOfWeekL = (TextView) frSchList.getView().findViewById(R.id.tvDayOfWeek); //������� TextView ���������� �� ���� ������
			byte count1 = 0; //������� ���������� �� �����
			
			dayOfWeekL.setText(days[day]); //������ ����� ��� TextView ����������� �� ���� ������
			llSchList1.removeAllViews(); //������� ������ LinearLayout
			llSchList2.removeAllViews(); //������� ������ LinearLayout
			
			for (int i = 0; i < 15; i += 1) { //���� ���������� LinearLayout'�� �����������
				if (lessonsNums[day][i].equals("1.")) { //���� ��� ������ ���� � �����
					count1 += 1; //�� ����������� ������� �� 1
				}
				
				TextView tvLesson = new TextView(this); //������� ����� TextView
				tvLesson.setText(lessonsNums[day][i] + "" + lessons[day][i]); //��������� TextView ������� ����� � ������
				tvLesson.setTextSize(18); //�������� ������ ������
				
				if (count1 == 2) {
					llSchList2.addView(tvLesson);
				} else {
					llSchList1.addView(tvLesson);
				}
			}
			break;
			
		case 2: //���� portrait tablet
			frSchList = getSupportFragmentManager().findFragmentById(R.id.schList_xL);
			LinearLayout llSchList_xL = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList1L);
			llSchList_xL.removeAllViews();
			
			TextView tvDay = new TextView(this); //������� TextView ���������� �� ������ ���� ������
			tvDay.setText(days[day]); //������ ����� ��� TextView ����������� �� ������ ���� ������
			tvDay.setTextSize(30); //�������� ������ ������
			llSchList_xL.addView(tvDay);
			
			for (int i = 0; i < 15; i += 1) {
				TextView tvLesson = new TextView(this);
				tvLesson.setText(lessonsNums[day][i] + "" + lessons[day][i]);
				tvLesson.setTextSize(20);
				llSchList_xL.addView(tvLesson);
			}
			
			TextView tvDay2 = new TextView(this); //������� TextView ���������� �� ������ ���� ������
			tvDay2.setText(days[day + 1]); //������ ����� ��� TextView ����������� �� ������ ���� ������
			tvDay2.setTextSize(34); //�������� ������ ������
			llSchList_xL.addView(tvDay2);
			
			for (int i = 0; i < 15; i += 1) {
				TextView tvLesson = new TextView(this);
				tvLesson.setText(lessonsNums[day + 1][i] + "" + lessons[day + 1][i]);
				tvLesson.setTextSize(20);
				llSchList_xL.addView(tvLesson);
			}
			break;
			
		case 3: //���� landscape tablet
			frSchList = getSupportFragmentManager().findFragmentById(R.id.schList_xL_L);
			LinearLayout llSchList1_xL = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList1L);
			LinearLayout llSchList2_xL = (LinearLayout) frSchList.getView().findViewById(R.id.llSchList2L);
			
			llSchList1_xL.removeAllViews();
			llSchList2_xL.removeAllViews();
			
			TextView tvDay1 = new TextView(this);
			tvDay1.setText(days[day]); //������ ����� ��� TextView ����������� �� ���� ������
			tvDay1.setTextSize(30); //�������� ������ ������
			llSchList1_xL.addView(tvDay1);
			
			for (int i = 0; i < 15; i += 1) {
				TextView tvLesson = new TextView(this);
				tvLesson.setText(lessonsNums[day][i] + "" + lessons[day][i]);
				tvLesson.setTextSize(20);
				llSchList1_xL.addView(tvLesson);
			}
			
			TextView tvDay12 = new TextView(this);
			tvDay12.setText(days[day + 1]);
			tvDay12.setTextSize(34); //�������� ������ ������
			llSchList2_xL.addView(tvDay12);
			
			for (int i = 0; i < 15; i += 1) {
				TextView tvLesson = new TextView(this);
				tvLesson.setText(lessonsNums[day + 1][i] + "" + lessons[day + 1][i]);
				tvLesson.setTextSize(20);
				llSchList2_xL.addView(tvLesson);
			}
			break;
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
		
		parse("��|1;2;3;4;5;6;1;2;3;4;5;6;|��������;���-��;�����;�����;���;���������;-;-;-;-;-;-;" //TODO �������� ��� JSON
				+ "(*)��|1;2;3;4;5;6;7;1;2;3;4;5;6;|������.;��.��.;���.��;���.��;���.��;-;-;-;-;-;-;-;-;"
				+ "(*)��|1;2;3;4;5;6;7;1;2;3;4;5;|--;--;--;--;--;--;--;-;-;-;-;-;"
				+ "(*)��|1;2;3;4;5;6;7;1;2;3;4;5;6;|���-��;���.��;���;������.;���.��;���.��;-;-;-;-;-;-;-;"
				+ "(*)��|1;2;3;4;5;6;1;2;3;4;5;|--;--;--;--;--;--;-;-;-;-;-;"
				+ "(*)��|1;2;3;4;5;6;|--;--;--;--;--;--;");
		}

	//������� ���� �� xml �����
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_screen, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.action_refresh:
			getData();
			Toast.makeText(this, "���������", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_logOut:
			//TODO ����� �� �������
			break;
		}
		return true;
	}
	
	public void parse(String str) { //��������� ���������� ������ �� �������� � �������� ����� �������� //TODO ���������� ��� JSON
		int index = str.length();
		String[] daySchedule = new String[6];
		
		for (int i = 5; i >= 1; i -= 1) { //�������� ������ � ����� ������
			int indexN = str.lastIndexOf("(*)", index - 1);
			daySchedule[i] = str.substring(indexN, index);
			Log.w("MYLOG", "dSch" + daySchedule[i]);
			index = indexN;
		}
		
		daySchedule[0] = str.substring(0, index);
		Log.w("MYLOG", daySchedule[0]);
		
		for (int i = 0; i < 6; i += 1) {
			StringBuilder sb = new StringBuilder(10);
			int k = 0;
			
			index = daySchedule[i].indexOf("|");
			
			sb.insert(0, daySchedule[i].substring(index+1, daySchedule[i].indexOf("|", index+1)));
			Log.w("MYLOG", sb.toString());
			
			while (sb.length() > 0) { //�������� ������ ������
				lessonsNums[i][k] = sb.substring(0, sb.indexOf(";")) + ".";
				sb.delete(0, sb.indexOf(";")+1);
				k += 1;
				Log.w("MYLOG", sb.toString());
			}
			
			index = daySchedule[i].indexOf("|", index + 1);
			
			sb.insert(0, daySchedule[i].substring(index + 1));
			
			Log.w("MYLOG","prLessons" + sb.toString());
			
			k = 0;
			
			while (sb.length() > 0) { //�������� �����
				lessons[i][k] = sb.substring(0, sb.indexOf(";"));
				sb.delete(0, sb.indexOf(";")+1);
				Log.w("MYLOG", "lessons " + lessons[i][k]);
				k += 1;
			}
		}
	}
	
	class Connector extends AsyncTask<String,Void,JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]); //TODO ����������� ������� IP
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
			} catch (IOException e) { //������ ����� ��� ���������� TODO
				return null;
			}
			return null;
		}
		
	}
}


