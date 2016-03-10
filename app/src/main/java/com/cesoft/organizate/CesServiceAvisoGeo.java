package com.cesoft.organizate;

import java.util.List;

import com.cesoft.organizate.models.AvisoGeo;
import com.cesoft.organizate.models.Objeto;
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
				Objeto o = Objeto.getById(geof.getRequestId());
				Intent i = new Intent(this, ActEdit.class);
				intent.putExtra(Objeto.class.getName(), o);
				Util.showAviso(this, notificationTitle, o.getAvisoGeo(), i);
			}
		}
	}

}
