package com.cesoftware.Organizate;

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
import android.util.Log;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo
public class CesServiceAvisoGeo extends IntentService
{
	private static final long DELAY_LOAD = 5*60*1000;//TODO: ajustar => activar solo cuando guarde...?

	private final String TAG = this.getClass().getCanonicalName();

	public CesServiceAvisoGeo()
	{
		super("GeofenceIntentService");
		Log.v(TAG, "Constructor.");
	}

	public void onCreate()
	{
		super.onCreate();
		Log.v(TAG, "onCreate");
	}

	public void onDestroy()
	{
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		Log.v(TAG, "onHandleIntent");
		if(!geofencingEvent.hasError())
		{
			int transition = geofencingEvent.getGeofenceTransition();
			String notificationTitle;

			switch(transition)
			{
			case Geofence.GEOFENCE_TRANSITION_ENTER:
				notificationTitle = "Geofence Entered";
				Log.v(TAG, "------------------------------Geofence Entered");
				break;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				notificationTitle = "Geofence Dwell";
				Log.v(TAG, "-----------------------------Dwelling in Geofence");
				break;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				notificationTitle = "Geofence Exit";
				Log.v(TAG, "-----------------------------Geofence Exited");
				break;
			default:
				notificationTitle = "-----------------------------Geofence Unknown";
				Log.v(TAG, "-----------------------------Geofence Unknown");
				break;
			}

			sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
		}
	}

	private void sendNotification(Context context, String notificationText, String notificationTitle)
	{
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();

		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(notificationTitle)
				.setContentText(notificationText)
				.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(false);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());

		wakeLock.release();
	}

	private String getTriggeringGeofences(Intent intent)
	{
		GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
		List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();

		String[] geofenceIds = new String[geofences.size()];

		for(int i = 0; i < geofences.size(); i++)
		{
			geofenceIds[i] = geofences.get(i).getRequestId();
		}

		return TextUtils.join(", ", geofenceIds);
	}
}
