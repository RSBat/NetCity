package com.netcity;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotifService extends Service {

	ExecutorService es;
	
	public void onCreate() {
		super.onCreate();
		es = Executors.newFixedThreadPool(1);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Checker ch = new Checker();
		es.execute(ch);
		
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void showNotif() {
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, ContentActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ContentActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		
		Notification notif =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("My notification")
		        .setContentText("Hello World!")
		        .setContentIntent(resultPendingIntent)
		        .build();
		
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, notif);
	}
	
	class Checker implements Runnable {
	    
	    public Checker() {
	    
	    }
	    
	    public void run() {
	    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    	while (true)
	    	{
	    		try {
					TimeUnit.SECONDS.sleep(60);
					if (sharedPref.getBoolean("pref_notif_showNotif", false)) {
						StringBuilder sb = new StringBuilder();
						sb.append(Calendar.HOUR_OF_DAY);
						sb.append(":");
						sb.append(Calendar.MINUTE);
						Calendar c = Calendar.getInstance();
						if (c.get(Calendar.HOUR_OF_DAY) == 22) {
							showNotif();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    }
	}
}
