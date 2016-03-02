package com.cesoft.organizate;

import java.util.List;

import com.cesoft.organizate.models.AvisoGeo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import android.app.IntentService;
import android.app.PendingIntent;
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
				break;
			case Geofence.GEOFENCE_TRANSITION_DWELL:
				notificationTitle = getString(R.string.geofen_dwell);
				break;
			case Geofence.GEOFENCE_TRANSITION_EXIT:
				notificationTitle = getString(R.string.geofen_out);
				break;
			default:
				notificationTitle = "Geofence Unknown";
System.err.println("onHandleIntent-----------------------------Geofence Unknown");
				break;
			}
			GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
			List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();
			for(Geofence geof : geofences)
			{
				AvisoGeo ag = AvisoGeo.getById(geof.getRequestId());//TODO: mostar mensaje aviso, pero ir a pantalla del objeto, no del aviso
				sendNotification(this, ag, notificationTitle);
			}
		}
	}

	private void sendNotification(Context context, AvisoGeo ag, String notificationTitle)
	{
		PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wakeLock.acquire();
		Intent intent = new Intent(context, ActAvisoGeoEdit.class);//ActMain.class
		intent.putExtra(AvisoGeo.class.getName(), ag);//TODO:Add id AvisoGeo
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(android.R.drawable.ic_menu_mylocation)//R.mipmap.ic_launcher)
				.setContentTitle(notificationTitle)
				.setContentText(ag.getTexto())
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
				.setAutoCancel(false);
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());
		//Util.playNotificacion(this);Es una notificacion, ya lo hace ella...
		wakeLock.release();
	}
}
