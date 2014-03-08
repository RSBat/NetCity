package com.netcity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NewsScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_screen, menu);
		return true;
	}

	public void getData() {
		parce("");
	}
	
	public void parce(String str) {
		
	}
	
	public void showNews() {
		
	}
}
