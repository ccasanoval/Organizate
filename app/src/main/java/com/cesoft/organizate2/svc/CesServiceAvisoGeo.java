package com.cesoft.organizate2.svc;

import java.util.List;

import com.cesoft.organizate2.ActEdit;
import com.cesoft.organizate2.App;
import com.cesoft.organizate2.R;
import com.cesoft.organizate2.util.Util;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesServiceAvisoGeo extends IntentService
{
	private static final String TAG = CesServiceAvisoGeo.class.getSimpleName();
	public CesServiceAvisoGeo()
	{
		super("GeofenceIntentService");
	}
	//public void onCreate(){super.onCreate();}
	//public void onDestroy(){super.onDestroy();}

	//----------------------------------------------------------------------------------------------
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
				break;
			}
Log.e(TAG,"onHandleIntent------------------------------------------------------------------"+notificationTitle);
			GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
			List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();
			for(Geofence geof : geofences)
			{
				List<Objeto> lista = App.getLista(this);//es mejor consulta en bbdd ???
				String id = geof.getRequestId();
				if(lista != null)
				for(Objeto o : lista)
				{
					if(o.getId().equals(id))
					{
						Log.e(TAG, "checkAvisos:----ACTIVA EL AVISO GEO*****************************************************" + o);
						Intent i = new Intent(getBaseContext(), ActEdit.class);
						i.putExtra(Objeto.class.getName(), o);
						Util.showAviso(getBaseContext(), getString(R.string.aviso_geo), o.getAvisoGeo(), i);
						break;
					}
				}
				else
					Log.e(TAG, "checkAvisos:----LIST == NULL*****************************************************");
			}
		}
	}

	//----------------------------------------------------------------------------------------------
	// Restaura el servicio cuando se le mata el proceso
	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		Context context = getApplicationContext();
		Log.e(TAG, "-------------------onTaskRemoved---------------------");
		Intent restartServiceIntent = new Intent(context, this.getClass());
		restartServiceIntent.setPackage(getPackageName());

		PendingIntent restartServicePendingIntent = PendingIntent.getService(context, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmService.set(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 500,
				restartServicePendingIntent);

		Log.e(TAG, "-------------------Reiniciando...---------------------");
		super.onTaskRemoved(rootIntent);
	}

}
