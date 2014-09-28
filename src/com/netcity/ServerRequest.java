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

import android.util.Log;

/**
 * Класс посылающий запрос на сервер и возвращающий ответ в виде строки содержащей JSON
 *  * 
 * @author Сергей
 */
public class ServerRequest {

	/**
	 * 
	 * @param url URL на который надо отправить запрос
	 * @param params Дополнительные параметры
	 * @param tokenRequired Нужен ли ключ
	 * @param token Ключ авторизации
	 * @return Строка содержащая JSONArray
	 * или [{"error":"IOException"}] - нет соединения
	 */
	protected String connect(String url, String params, boolean tokenRequired, String token) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url + params);
		HttpResponse response;
		HttpEntity entity;
		InputStream ins;
		
		if (tokenRequired) {
			httpGet.addHeader("Auth-Token", token);
		}
		
		try {
			response = client.execute(httpGet);
			entity = response.getEntity();
			ins = entity.getContent();
			
			String result;
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null)  sb.append(line); 
	    		result = sb.toString();
	    	ins.close();
		    	
	    	Log.w("MYLOG", result);
	    	
	    	return result;
			
		} catch (ClientProtocolException e) {
			return "[{\"error\":\"ClientProtocolException\"}]";
		} catch (IOException e) { //Ошибка когда нет соединения
			return "[{\"error\":\"IOException\"}]";
		}
	}
}
