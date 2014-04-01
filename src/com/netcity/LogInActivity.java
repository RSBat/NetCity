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

	//Описание переменных
	
	//Поля ввода текста
	EditText etUserName, etPassword; //Поля ввода логина и пароля соответственно
	
	//Чекбоксы
	CheckBox chbRememberMe; //Отвечает за сохранение логина и пароля между сессиями (по словам Димы это не понадобится т.к. будет одноразовая авторизация)
	
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
		
		//Присваеваем переменным соответствующие view
		etUserName = (EditText) findViewById(R.id.et_userName); //Поле ввода логина
		etPassword = (EditText) findViewById(R.id.et_password); //Поле ввода пароля
		chbRememberMe = (CheckBox) findViewById(R.id.chb_rememberMe); //Чекбокс спрашивающий надо ли сохранять логин и пароль
		btnLogIn = (Button) findViewById(R.id.btn_logIn); //Кнопка для отправки логина и пароля
		
		getServers(); //Вызов функции отвечающей за получение списка школ
	}

	public void getServers() { //Функция для заполнения выпадающего списка
		ConnectorServer connection = new ConnectorServer();
		connection.execute("http://195.88.220.90/v1/login/srv_list");
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
		getServers();
		Toast.makeText(this, "Обновлено", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	//Выход пользователя
	public void logOut(View v) {
		//TODO отправка данных на сервер для выхода из нетскула
	}
	
	//Отправка логина и пароля и в случае удачи переход на расписание
	public void logIn(View v) {
		//TODO отправка логина и пароля на сервер	
		Auth connection = new Auth();
		connection.execute(servers[serverSlct.getSelectedItemPosition()], "1", "1", "1", etUserName.getText().toString(), etPassword.getText().toString());
		try {
			String result = connection.get();
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, ContentActivity.class); //Создаем ссылку на страницу с расписанием
			startActivity(intent); //Запуск станицы с расписанием
			finish();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
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
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient(); //Создаем стандартный HTTP клиент
			HttpGet httpGet = new HttpGet(params[0]); //Создаем Get-запрос на сервер
			HttpResponse response; //Отвечает за ответ
			HttpEntity entity; //Хз что это
			InputStream ins; //Поток с пришедшими данными
			
			try {
				response = client.execute(httpGet); //Получения ответа на Get-запрос
				entity = response.getEntity(); //Хз что это
				ins = entity.getContent(); //Получаем поток из пришедших данных
				try {
					String result = null; //Строка с результатом
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256); //Буффер с данными из полученного потока
					StringBuilder sb = new StringBuilder(); //Конструктор строк для получения готовой строки
					String line = null; //Строка из буффера
					
					while ((line = reader.readLine()) != null)  sb.append(line); //Получаем данные из буфера и заносим их в конструктор строк
			    	
					result = sb.toString(); //Получаем готовую строку
			    	ins.close(); //Закрываем поток с полученными данными
			    	
			    	return result;
				} catch (Exception e) {}
			} catch (ClientProtocolException e) { //TODO
			} catch (IOException e) { //Ошибка когда нет соединения
				return "";
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result == "") Toast.makeText(getApplication(), "Не удается установить соединение с сервером. Пожайлуста проверьте интернет-соединение", Toast.LENGTH_LONG).show();//btnLogIn.setEnabled(false);
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
			
			HttpClient client = new DefaultHttpClient(); //Создаем стандартный HTTP клиент
			HttpGet httpGet = new HttpGet(urlSb.toString()); //Создаем Get-запрос на сервер
			HttpResponse response; //Отвечает за ответ
			HttpEntity entity; //Хз что это
			InputStream ins; //Поток с пришедшими данными
			
			try {
				response = client.execute(httpGet); //Получения ответа на Get-запрос
				entity = response.getEntity(); //Хз что это
				ins = entity.getContent(); //Получаем поток из пришедших данных
				try {
					String result = null; //Строка с результатом
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256); //Буффер с данными из полученного потока
					StringBuilder sb = new StringBuilder(); //Конструктор строк для получения готовой строки
					String line = null; //Строка из буффера
					
					while ((line = reader.readLine()) != null)  sb.append(line); //Получаем данные из буфера и заносим их в конструктор строк
			    	
					result = sb.toString(); //Получаем готовую строку
			    	ins.close(); //Закрываем поток с полученными данными
			    	
			    	return result;
				} catch (Exception e) {}
			} catch (ClientProtocolException e) { //TODO
			} catch (IOException e) { //Ошибка когда нет соединения
				return "";
			}
			return "";
		}
	}
}
