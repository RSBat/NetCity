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

import android.os.AsyncTask;
import android.util.Log;

/**
 * ����� ���������� ������ �� ������ � ������������ ����� � ���� ������ ���������� JSON
 * 
 * @param 1 url �� �������� ���� ��������� ������
 * @param 2 �������������� ���������
 * @param 3 ���� �� ���������� ���� ����������� true/false
 * @param 4 ���� �����������
 * 
 * @returns 1 ������ ���������� JSON
 * 
 * @author ������
 */
public class ServerRequest {

	/*
	 * @param 1 url �� �������� ���� ��������� ������
	 * @param 2 �������������� ���������
	 * @param 3 ���� �� ���������� ���� ����������� true/false
	 * @param 4 ���� �����������
	 */
	
	/**
	 * 
	 * @param url URL �� ������� ���� ��������� ������
	 * @param params �������������� ���������
	 * @param tokenRequired ����� �� ����
	 * @param token ���� �����������
	 * @return ������ ���������� JSONArray
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
			try {
				String result;
				BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "utf-8"), 256);
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				while ((line = reader.readLine()) != null)  sb.append(line); 
		    		result = sb.toString();
		    	ins.close();
		    	
		    	Log.w("MYLOG", result);
		    	
		    	return result;
			} catch (Exception e) {
				return "[{\"error\":\"error\"}]";
			}
		} catch (ClientProtocolException e) {
			return "[{\"error\":\"ClientProtocolException\"}]";
		} catch (IOException e) { //������ ����� ��� ����������
			return "[{\"error\":\"IOException\"}]";
		}
		//return "[{\"error\":\"error\"}]";
	}
}