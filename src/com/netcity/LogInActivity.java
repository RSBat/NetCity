package com.netcity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	String[] servers = {""};
	
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
	}

	public void getServers() { //������� ��� ���������� ����������� ������
		ConnectorServer connection = new ConnectorServer();
		connection.execute("http://195.88.220.90/v1/login/srv_list");
		try {
			String result = connection.get();
			JSONObject json = new JSONObject(result);
			String [] serversNames = new String[json.length()]; //������� ����� ��� ����
			servers = new String[json.length()];
			
			Iterator<String> inter = json.keys(); //�������� ����� ��� �������� � JSON'e � ������� �� � Iterator
		
			Integer i = 0; //������� ������� i
		
			while (inter.hasNext() == true) { //���� ��� ���������� ������� �������
				serversNames[i] = inter.next(); //��������� ������ ������� ������
				i += 1; //������������ ������� �� 1
			}
			
			for (i = 0; i < json.length(); i++) {
				servers[i] = json.getString(serversNames[i]);
			}
		
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serversNames); //������� ������� ��� ����������� ������ ��������� ������ � �������
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
			String[] s = {""};
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s); //������� ������� ��� ����������� ������ ��������� ������ � �������
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //�����������
		
			serverSlct = (Spinner) findViewById(R.id.serverSelect); //������� ���������� ������ �� id
		
			serverSlct.setAdapter(adapter); //������������� ������� ��� ����������� ������
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
		Auth connection = new Auth();
		connection.execute(servers[serverSlct.getSelectedItemPosition()], "1", "1", "1", etUserName.getText().toString(), etPassword.getText().toString());
		try {
			String result = connection.get();
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, ContentActivity.class); //������� ������ �� �������� � �����������
			startActivity(intent); //������ ������� � �����������
			finish();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
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
		}
	}
	
	class Auth extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String passwHash = "";
			final String MD5 = "MD5";
			
		    try {
		        // Create MD5 Hash
		        MessageDigest digest = java.security.MessageDigest
		                .getInstance(MD5);
		        digest.update(params[5].getBytes());
		        byte messageDigest[] = digest.digest();

		        // Create Hex String
		        StringBuilder hexString = new StringBuilder();
		        for (byte aMessageDigest : messageDigest) {
		            String h = Integer.toHexString(0xFF & aMessageDigest);
		            while (h.length() < 2)
		                h = "0" + h;
		            hexString.append(h);
		        }
		        passwHash = hexString.toString();

		    } catch (NoSuchAlgorithmException e) {
		        e.printStackTrace();
		    }
			
			StringBuilder urlSb = new StringBuilder(100);
			urlSb.append("http://195.88.220.90/v1/login/auth/?srv=");
			urlSb.append(params[0]);
			urlSb.append("&sid=");
			urlSb.append(params[1]);
			urlSb.append("&cn=");
			urlSb.append(params[2]);
			urlSb.append("&scid=");
			urlSb.append(params[3]);
			urlSb.append("&login=");
			urlSb.append(params[4]);
			urlSb.append("&password=");
			urlSb.append(passwHash);
			urlSb.append("&pwlen=");
			urlSb.append(params[5].length());
			
			HttpClient client = new DefaultHttpClient(); //������� ����������� HTTP ������
			HttpGet httpGet = new HttpGet(urlSb.toString()); //������� Get-������ �� ������
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
	}
}
