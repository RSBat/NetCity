package com.netcity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ContentActivity extends ActionBarActivity {

	private static long back_pressed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		Tab tab;
		
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4){
			tab = actionBar.newTab().setText("Расписание").setTabListener(new TabListener<ScheduleScreen_xLarge>(this, "Расписание", ScheduleScreen_xLarge.class));
			actionBar.addTab(tab);
		} else {
			tab = actionBar.newTab().setText("Расписание").setTabListener(new TabListener<ScheduleScreen>(this, "Расписание", ScheduleScreen.class));
			actionBar.addTab(tab);
		}
		
		tab = actionBar.newTab().setText("Объявления").setTabListener(new TabListener<NewsScreen>(this, "Объявления", NewsScreen.class));
		actionBar.addTab(tab);
		
		tab = actionBar.newTab().setText("Оценки").setTabListener(new TabListener<MarksScreen>(this, "Оценки", MarksScreen.class));
		actionBar.addTab(tab);
		
		actionBar.setDisplayShowTitleEnabled(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_showNotif", false));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.content_screen, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
	
	@Override
	public void onBackPressed() {
	if (back_pressed + 2000 > System.currentTimeMillis())
	super.onBackPressed();
	else
	Toast.makeText(getBaseContext(), "Для выхода нажмите кнопку \"назад\" еще раз.", Toast.LENGTH_SHORT).show();
	back_pressed = System.currentTimeMillis();
	}
	
	class TabListener<T extends Fragment> implements ActionBar.TabListener {
	    private Fragment mFragment;
	    private final ActionBarActivity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public TabListener(ActionBarActivity activity, String tag, Class<T> clz) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // Check if the fragment is already initialized
	        mActivity.getSupportActionBar().setTitle(mTag);
	    	if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	        // User selected the already selected tab. Usually do nothing.
	    }
	}
}
