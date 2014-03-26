package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LogInActivity extends ActionBarActivity {

	//�������� ����������
	
	//���� ����� ������
	EditText etUserName, etPassword; //���� ����� ������ � ������ ��������������
	
	//��������
	CheckBox chbRememberMe; //�������� �� ���������� ������ � ������ ����� �������� (�� ������ ���� ��� �� ����������� �.�. ����� ����������� �����������)
	
	//������
	Button btnLogIn; //���������� ����� � ������
	
	//���������� ������
	Spinner serverSlct;
	
	private static long back_pressed; //��� ��������� �������� ������� �� ������ �����
	
	//���������� ��� �������� ��������
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //�� ����� �� ����
		setContentView(R.layout.activity_log_in_screen); //�������� layout ���� ��� ��������
		
		//����������� ���������� ��������������� view
		etUserName = (EditText) findViewById(R.id.et_userName); //���� ����� ������
		etPassword = (EditText) findViewById(R.id.et_password); //���� ����� ������
		chbRememberMe = (CheckBox) findViewById(R.id.chb_rememberMe); //������� ������������ ���� �� ��������� ����� � ������
		btnLogIn = (Button) findViewById(R.id.btn_logIn); //������ ��� �������� ������ � ������
		
		getServers(); //����� ������� ���������� �� ��������� ������ ����
		
		startService(new Intent(this, NotifService.class));
		
	}

	public void getServers() { //������� ��� ���������� ����������� ������
		ConnectorServer connection = new ConnectorServer();
		connection.execute("http://195.88.220.90/v1/login/srv_list");
		try {
			String result = connection.get();
			JSONObject json = new JSONObject(result);
			String[] servers = new String[json.length()]; //������� ����� ��� ����
			
			Iterator<String> inter = json.keys(); //�������� ����� ��� �������� � JSON'e � ������� �� � Iterator
		
			Integer i = 0; //������� ������� i
		
			while (inter.hasNext() == true) { //���� ��� ���������� ������� �������
				servers[i] = inter.next(); //��������� ������ ������� ������
				i += 1; //������������ ������� �� 1
			}
		
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, servers); //������� ������� ��� ����������� ������ ��������� ������ � �������
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //�����������
		
			serverSlct = (Spinner) findViewById(R.id.serverSelect); //������� ���������� ������ �� id
		
			serverSlct.setAdapter(adapter); //������������� ������� ��� ����������� ������
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
	}
	
	//������� ���� �� xml �����
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.log_in_screen, menu); //��������� ���� �� �����
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		getServers();
		Toast.makeText(this, "���������", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	//����� ������������
	public void logOut(View v) {
		//TODO �������� ������ �� ������ ��� ������ �� ��������
	}
	
	//�������� ������ � ������ � � ������ ����� ������� �� ����������
	public void logIn(View v) {
		//TODO �������� ������ � ������ �� ������	
		Intent intent = new Intent(this, ContentActivity.class); //������� ������ �� �������� � �����������
		startActivity(intent); //������ ������� � �����������
		finish();
	}
	
	//��������� ������� �� ������ �����
	//�������� ��������� ������ ���� ������ ������ ��� ���� ������
	@Override
	public void onBackPressed() { 
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();
		else
			Toast.makeText(getBaseContext(), "��� ������ ������� ������ \"�����\" ��� ���.", Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}
	
	class ConnectorServer extends AsyncTask<String,Void,String> {
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient(); //������� ����������� HTTP ������
			HttpGet httpGet = new HttpGet(params[0]); //������� Get-������ �� ������
			HttpResponse response; //�������� �� �����
			HttpEntity entity; //�� ��� ���
			InputStream ins; //����� � ���������� �������
			
			try {
				response = client.execute(httpGet); //��������� ������ �� Get-������
				entity = response.getEntity(); //�� ��� ���
				ins = entity.getContent(); //�������� ����� �� ��������� ������
				try {
					String result = null; //������ � �����������
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256); //������ � ������� �� ����������� ������
					StringBuilder sb = new StringBuilder(); //����������� ����� ��� ��������� ������� ������
					String line = null; //������ �� �������
					
					while ((line = reader.readLine()) != null)  sb.append(line); //�������� ������ �� ������ � ������� �� � ����������� �����
			    	
					result = sb.toString(); //�������� ������� ������
			    	ins.close(); //��������� ����� � ����������� �������
			    	
			    	return result;
				} catch (Exception e) {}
			} catch (ClientProtocolException e) { //TODO
			} catch (IOException e) { //������ ����� ��� ����������
				return "";
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == "") Toast.makeText(getApplication(), "�� ������� ���������� ���������� � ��������. ���������� ��������� ��������-����������", Toast.LENGTH_LONG).show();//btnLogIn.setEnabled(false);
			//else btnLogIn.setEnabled(true);
		}
	}
}
