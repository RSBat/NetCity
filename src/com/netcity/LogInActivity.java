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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

	//Описание переменных
	
	//Поля ввода текста
	EditText etUserName, etPassword; //Поля ввода логина и пароля соответственно
	
	//Кнопки
	Button btnLogIn; //Отправляет логин и пароль
	
	//Выпадающие списки
	Spinner serverSlct;
	
	String[] servers = {""};
	
	private static long back_pressed; //Для обработки двойного нажатия на кнопку назад
	
	//Вызывается при создании активити
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //Хз зачем но надо
		setContentView(R.layout.activity_log_in_screen); //Выбираем layout файл для активити
		
		SharedPreferences sPref = getSharedPreferences("NetCity", MODE_PRIVATE);
		
		/*
		 * Проверяем есть ли ключ
		 * Есть - переходим на расписание
		 * Нет - продолжаем
		 */
		if (!sPref.getString("token", "None").equals("None")) {
			Intent intent = new Intent(this, ContentActivity.class); //Создаем ссылку на страницу с расписанием
			startActivity(intent); //Запуск станицы с расписанием
			finish();
		}
		
		getServers(); //Вызов функции отвечающей за получение списка школ
		
		//Присваеваем переменным соответствующие view
		etUserName = (EditText) findViewById(R.id.et_userName); //Поле ввода логина
		etPassword = (EditText) findViewById(R.id.et_password); //Поле ввода пароля
		btnLogIn = (Button) findViewById(R.id.btn_logIn); //Кнопка для отправки логина и пароля
	}

	/**
	 * Метод заполняющий список школ (для Южно-Сахалинска)
	 * TODO написать такиеже для остальных выборов
	 */
	public void getServers() { //Функция для заполнения выпадающего списка
		ConnectorServer connection = new ConnectorServer();
		connection.execute("http://195.88.220.90/v1/login/srv_list", "");
		try {
			String result = connection.get();
			JSONObject json = new JSONObject(result);
			String [] serversNames = new String[json.length()]; //Создаем архив для школ
			servers = new String[json.length()];
			
			Iterator<String> inter = json.keys(); //Получаем ключи для значений в JSON'e и заносим их в Iterator
		
			Integer i = 0; //Создаем счетчик i
		
			while (inter.hasNext() == true) { //Цикл для заполнения массива школами
				serversNames[i] = inter.next(); //Заполняем ячейку массива школой
				i += 1; //Увеличчиваем счетчик на 1
			}
			
			for (i = 0; i < json.length(); i++) {
				servers[i] = json.getString(serversNames[i]);
			}
		
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serversNames); //Создаем адаптер для выпадающего списка используя массив с школами
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Продолжение
		
			serverSlct = (Spinner) findViewById(R.id.serverSelect); //Находим выпадающий список по id
		
			serverSlct.setAdapter(adapter); //Устанавливаем адаптер для выпадающего списка
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			String[] s = {""};
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s); //Создаем адаптер для выпадающего списка используя массив с школами
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Продолжение
		
			serverSlct = (Spinner) findViewById(R.id.serverSelect); //Находим выпадающий список по id
		
			serverSlct.setAdapter(adapter); //Устанавливаем адаптер для выпадающего списка
		}
	}
	
	//Создаем меню по xml файлу
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.log_in_screen, menu); //Загружаем меню из файла
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.action_refresh:
			getServers();
			break;
			
		case R.id.action_enterToken:
			SharedPreferences sPref = getSharedPreferences("NetCity", MODE_PRIVATE);
			Editor prefEd = sPref.edit();
			prefEd.putString("token", "c139897fcadcee085f8eceeec71229b4e9efd741");
			prefEd.commit();
			break;
		}
		return true;
	}
	
	/**
	 * Метод отправляющий логин и парол на сервер и если все правильно то переходим на расписание 
	 */
	public void logIn(View v) {
		//TODO заменить автоматиченские данные в запросе на сервер
		if (etUserName.getText().toString().equals("")) {
			Toast.makeText(this, "Пожайлуста, введите логин", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (etPassword.getText().toString().equals("")) {
			Toast.makeText(this, "Пожайлуста, введите пароль", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Auth connection = new Auth();
		connection.execute(servers[serverSlct.getSelectedItemPosition()], "1", "1", "1", etUserName.getText().toString(), etPassword.getText().toString());
		try {
			String result = connection.get();
			JSONObject jsonResp = new JSONObject(result);
			String status = jsonResp.optString("error", "ok");
			if (status.equals("ok")) {
				SharedPreferences sPref = getSharedPreferences("NetCity", MODE_PRIVATE);
				Editor prefEd = sPref.edit();
				prefEd.putString("token", jsonResp.getString("token"));
				prefEd.commit();
				
				Toast.makeText(this, sPref.getString("token", "none"), Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(this, ContentActivity.class); //Создаем ссылку на страницу с расписанием
				startActivity(intent); //Запуск станицы с расписанием
				finish();
			} else if (status.equals("IOException")){
				Toast.makeText(this, "Не удается установить соединение с сервером. Пожайлуста проверьте интернет-соединение", Toast.LENGTH_LONG).show();
			} else if (status.equals("wrong password")) {
				Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_LONG);
			} else {
				Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Обработка нажатия на кнопку назад
	//Активити закроется только если кнопка нажата два раза подряд
	@Override
	public void onBackPressed() { 
		if (back_pressed + 2000 > System.currentTimeMillis())
			super.onBackPressed();
		else
			Toast.makeText(getBaseContext(), "Для выхода нажмите кнопку \"назад\" еще раз.", Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}
	
	class ConnectorServer extends AsyncTask<String,Void,String> {
		
		@Override
		protected String doInBackground(String... params) {
			ServerRequest sReqAuth = new ServerRequest();
			
			return sReqAuth.connect(params[0], params[1], false, "");
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			try {
				/*
				 * Если соединение не было успешно то в поле error должно быть IOException
				 */
				if (new JSONArray(result).getJSONObject(0).optString("error", "None").equals("IOException")) {
					Toast.makeText(getApplication(), "Не удается установить соединение с сервером. Пожайлуста проверьте интернет-соединение", Toast.LENGTH_LONG).show();
					btnLogIn.setEnabled(false);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			
			StringBuilder sbParams = new StringBuilder(100);
			sbParams.append("/?srv=");
			sbParams.append(params[0]);
			sbParams.append("&sid=");
			sbParams.append(params[1]);
			sbParams.append("&cn=");
			sbParams.append(params[2]);
			sbParams.append("&scid=");
			sbParams.append(params[3]);
			sbParams.append("&login=");
			sbParams.append(params[4]);
			sbParams.append("&password=");
			sbParams.append(passwHash);
			sbParams.append("&pwlen=");
			sbParams.append(params[5].length());
			
			ServerRequest sReqAuth = new ServerRequest();
			
			return sReqAuth.connect("http://195.88.220.90/v1/login/auth", sbParams.toString(), false, "");
		}
	}
}
