package com.cesoft.organizate;

import java.util.List;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import android.app.IntentService;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo=> activar solo cuando guarde...?
public class CesServiceAvisoGeo extends IntentService
{
	private static final long DELAY_LOAD = 5*60*1000;//TODO: ajustar

	public CesServiceAvisoGeo()
	{
		super("GeofenceIntentService");
		//android.util.Log.v(TAG, "Constructor.");private final String TAG = this.getClass().getCanonicalName();
	}

	public void onCreate()
	{
		super.onCreate();
	}

	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		if( ! geofencingEvent.hasError())
		{
			int transition = geofencingEvent.getGeofenceTransition();
			String notificationTitle;
			switch(transition)
			{
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				notificationTitle = getString(R.string.geofen_in);
//System.err.println("onHandleIntent------------------------------Geofence Entered");
				break;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				notificationTitle = getString(R.string.geofen_dwell);
//System.err.println("onHandleIntent-----------------------------Dwelling in Geofence");
				break;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				notificationTitle = getString(R.string.geofen_out);
//System.err.println("onHandleIntent-----------------------------Geofence Exited");
				break;
			default:
				notificationTitle = "Geofence Unknown";
//System.err.println("onHandleIntent-----------------------------Geofence Unknown");
				break;
			}

			GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
			List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();
			for(int i=0; i < geofences.size(); i++)
				sendNotification(this, geofences.get(i).getRequestId(), notificationTitle);
			//sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
		}
	}

	private void sendNotification(Context context, String notificationText, String notificationTitle)
	{
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(android.R.drawable.ic_menu_mylocation)//R.mipmap.ic_launcher)
				.setContentTitle(notificationTitle)
				.setContentText(notificationText)
				.setDefaults(Notification.DEFAULT_ALL)
				.setAutoCancel(false);
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());
		wakeLock.release();
	}

	/*private String getTriggeringGeofences(Intent intent)
	{
		GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
		List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();
		String[] geofenceIds = new String[geofences.size()];
		for(int i=0; i < geofences.size(); i++)
		{
			geofenceIds[i] = geofences.get(i).getRequestId();
System.err.println("-----------------------------getTriggeringGeofences:"+geofenceIds[i]);
		}
		return TextUtils.join(", ", geofenceIds);
	}*/
}
