package com.cesoft.organizate.svc;

import java.util.List;

import com.cesoft.organizate.ActEdit;
import com.cesoft.organizate.App;
import com.cesoft.organizate.R;
import com.cesoft.organizate.util.Util;
import com.cesoft.organizate.models.Objeto;
import com.cesoft.organizate.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import android.app.IntentService;
import android.content.Intent;

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
Log.e("CesSvcAGeo","onHandleIntent------------------------------------------------------------------Geofence Unknown");
				break;
			}
Log.e("CesSvcAGeo","onHandleIntent------------------------------------------------------------------"+notificationTitle);
			GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
			List<Geofence> geofences = geofenceEvent.getTriggeringGeofences();
			for(Geofence geof : geofences)
			{
				List<Objeto> lista = App.getLista(this);//es mejor consulta en bbdd ???
				String id = geof.getRequestId();
				for(Objeto o : lista)
				{
					if(o.getId().equals(id))
					{
						Log.e("CesSvcAGeo", "checkAvisos:----ACTIVA EL AVISO GEO*****************************************************" + o);
						Intent i = new Intent(getBaseContext(), ActEdit.class);
						i.putExtra(Objeto.class.getName(), o);
						Util.showAviso(getBaseContext(), getString(R.string.aviso_tem), o.getAvisoTem(), i);
						break;
					}
				}
			}
		}
	}

}
