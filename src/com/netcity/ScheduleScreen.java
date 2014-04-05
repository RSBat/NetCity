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

import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author ������
 * 
 */
public class ScheduleScreen extends Fragment {
	
	//�������� ����������
	/**
	 * ����� ����� ������ ��� ����������.
	 * <p>� portrait ������ ������������ ��� ������ ����� ���������� <br/> 
	 * � landscape ������ ������������ ��� ������ ���������� ������ �����</p>
	 */
	LinearLayout llSchedule1;
	
	/**
	 * ������ ����� ������ ��� ����������.
	 * <p>� portrait ������ �� ������������ <br/>
	 * � landscape ������ ������������ ��� ������ ���������� ������ �����</p>
	 */
	LinearLayout llSchedule2;
	
	/**
	 * 
	 */
	TextView tvDayOfWeek;
	
	//������� �����
	String[] days = {"�����������", "�������", "�����", "�������", "�������", "�������"}; //��� ������
	String[][] lessons = new String[6][15]; //������ � �������
	String[][] lessonsNums = new String[6][15]; //������ � �������� ������
	
	//JSON
	JSONArray json;// = new JSONObject(); //JSON ��� ������
	
	Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday;
	
	int day = 0;
	
	/**
	 * <p><b>������� �����</b></p>
	 * <p>1) ������� View �� .xml �����. <br/>
	 * 2) ������� ������ ��� View <br/>
	 * 3) ������ ������� ���������� ������� ������� �������� �������� ���������� ���������� (int) day �� ��������������� ���������� ��� <br/>
	 * 4) ���������� ����������� ���� � ������� ��� � ���������� ���������� (int) day <br/>
	 * 5) � ����������� �� ��� ������ ��������� ��������������� ������ <br/>
	 * 6) ������� ����� � ������� �������� ���������� � ������� ��� �� �����</p> 
	 */
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
		
		//getData(); //�������� ������� ��������� ������
		
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
		
		Connector connection = new Connector();
		connection.execute();
		
		return v;
	}
	
	//������� ������ ���������� �� �����
	/**
	 * <p><b>������� ������ ����������</b></p>
	 * <p>�������� ���� ������ �� ���������� ���������� (int) day � ��������� ����� ������� �� ����������� (JSONArray) json</p>
	 */
	public void showSchedule() {
		//TODO ����� ���������� �� �����
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
				
				byte count1 = 0; //������� ���������� �� �����
				
				for (int i = 0; i < jsonLessons.length(); i += 1) { //���� ���������� LinearLayout'�� �����������
					JSONObject jsonLes;
					try {
						jsonLes = jsonLessons.getJSONObject(i);
						TextView tvLesson = new TextView(getActivity()); //������� ����� TextView
						tvLesson.setText(jsonLes.optInt("num") + ". " + jsonLes.optString("name")); //��������� TextView ������� ����� � ������
						tvLesson.setTextSize(18); //�������� ������ ������
					
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

	/**
	 * <p><b>������� ��������� ���������� � �������</b></p>
	 * <p>�������� ���� ������ �� ���������� ���������� (int) day � ��������� ����� ������� �� ����������� (JSONArray) json</p>
	 */
	/*public void getData(){
		Connector connection = new Connector();
		connection.execute(); //TODO
		try {
			String res = connection.get();
			//Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
			Log.w("MYLOG", res);
			json = new JSONArray(res);
			showSchedule();
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
	} */
	
	/**
	 * <p><b>���������� ������</b></p>
	 * <p>��������� ������ ������ ������ ��� ��� ���� ������� ��� ���������</p>
	 * @param btn ������ ������� ���� ���������
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
	
	class Connector extends AsyncTask<Void,Void,String> {
		
		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			SharedPreferences sPref = getActivity().getSharedPreferences("NetCity", getActivity().MODE_PRIVATE);
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://195.88.220.90/v1/schedule/week");
			HttpResponse response;
			HttpEntity entity;
			InputStream ins;
			
			Log.w("MYLOG", sPref.getString("token", "None"));
			
			httpGet.setHeader("Auth-Token", sPref.getString("token", "None"));
			
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
			    	
			    	return result;
				} catch (Exception e) {}
			} catch (ClientProtocolException e) {
				return "ClientProtocolException";
			} catch (IOException e) { //������ ����� ��� ����������
				return "IOException";
			}
			return "Error";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

	    	try {
				json = new JSONArray(result);
				showSchedule();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


