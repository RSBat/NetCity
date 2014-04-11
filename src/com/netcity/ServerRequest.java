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

	/**
	 * @param 1 url �� �������� ���� ��������� ������
	 * @param 2 �������������� ���������
	 * @param 3 ���� �� ���������� ���� ����������� true/false
	 * @param 4 ���� �����������
	 */
	protected String connect(String... params) {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(params[0] + params[1]);
		HttpResponse response;
		HttpEntity entity;
		InputStream ins;
		
		if (params[2].equals("true")) {
			httpGet.addHeader("Auth-Token", params[3]);
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
