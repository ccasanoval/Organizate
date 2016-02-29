package com.cesoft.organizate;

import android.app.IntentService;
import android.content.Intent;

import com.cesoft.organizate.models.Aviso;
import com.cesoft.organizate.models.AvisoGeo;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo
public class CesServiceAviso extends IntentService
{
	private static final long DELAY_LOAD = 5*60*1000;//TODO: ajustar
	private static final long DELAY_CHECK = 3*60*1000;
	private ArrayList<Aviso> _lista = new ArrayList<>();

	private CesGeofenceStore _GeofenceStore;
	private ArrayList<AvisoGeo> _listaGeo = new ArrayList<>();
		public ArrayList<AvisoGeo> getAvisosGeo(){return _listaGeo;};

	//______________________________________________________________________________________________
	public CesServiceAviso()
	{
		super("OrganizateAviso");
		//SugarContext.init(this);
	}

	//______________________________________________________________________________________________
	@Override
	protected void onHandleIntent(Intent workIntent)
	{
		try
		{
			SugarContext.init(this);
			//String dataString = workIntent.getDataString();
			long tmLoad = System.currentTimeMillis() - 2*DELAY_LOAD;
			long tmCheck = System.currentTimeMillis() - 2*DELAY_LOAD;
			while(true)
			{
System.err.println("CesServiceAviso:onHandleIntent:looping------------");
				Thread.sleep(DELAY_CHECK/2);
				if(tmLoad + DELAY_LOAD < System.currentTimeMillis())
				{
					cargarLista();
					cargarListaGeo();
					tmLoad = System.currentTimeMillis();
				}
				if(tmCheck + DELAY_CHECK < System.currentTimeMillis())
				{
					checkAvisos();
					tmCheck = System.currentTimeMillis();
				}
			}
		}
		catch(InterruptedException e){System.err.println("CesServiceAviso:onHandleIntent:e:"+e);}
	}

	//______________________________________________________________________________________________
	private void cargarListaGeo()
	{
System.err.println("CesServiceAviso----cargarListaGeo----*************************************");
		try
		{
			_listaGeo.clear();
			if(_GeofenceStore != null)_GeofenceStore.del();
			ArrayList<Geofence> aGeofences = new ArrayList<>();
			Iterator<AvisoGeo> it = AvisoGeo.getActivos();
			while(it.hasNext())
			{
				AvisoGeo ag = it.next();
				_listaGeo.add(ag);
				aGeofences.add(new Geofence.Builder()
						.setRequestId(ag.getTexto())
						.setCircularRegion(ag.getLatitud(), ag.getLongitud(), ag.getRadio())
						.setExpirationDuration(Geofence.NEVER_EXPIRE)
						.setLoiteringDelay(30000)// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
						.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());
			}
			System.err.println("CesServiceAviso---------------------cargarListaGeo:" + _listaGeo.size());
//			aGeofences.add(new Geofence.Builder().setRequestId("Geofence 1").setCircularRegion(_GeofenceCoordinates.get(0).latitude, _GeofenceCoordinates.get(0).longitude, _GeofenceRadius.get(0)).setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
//			aGeofences.add(new Geofence.Builder().setRequestId("Geofence 2").setCircularRegion(_GeofenceCoordinates.get(1).latitude, _GeofenceCoordinates.get(1).longitude, _GeofenceRadius.get(1)).setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());
//			aGeofences.add(new Geofence.Builder().setRequestId("Geofence 3").setCircularRegion(_GeofenceCoordinates.get(2).latitude, _GeofenceCoordinates.get(2).longitude, _GeofenceRadius.get(2)).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());
			_GeofenceStore = new CesGeofenceStore(this, aGeofences);
		}
		catch(Exception e)
		{
			System.err.println("CesServiceAviso:cargarListaGeo:e:"+e);
			//_lista.clear();
		}
	}
	//______________________________________________________________________________________________
	private void cargarLista()
	{
System.err.println("CesServiceAviso----cargarLista----*************************************");
		try
		{
			_lista.clear();
			Iterator<Aviso> it = Aviso.getActivos();
			while(it.hasNext())
				_lista.add(it.next());
System.err.println("CesServiceAviso---------------------cargarLista:"+_lista.size());
		}
		catch(Exception e)
		{
			System.err.println("CesServiceAviso:cargarLista:e:"+e);
			//_lista.clear();
		}
	}

	//______________________________________________________________________________________________
	private void checkAvisos()
	{
System.err.println("CesServiceAviso-------checkAvisos----*************************************");
		if(_lista == null || _lista.size() == 0)return;
		for(Aviso a : _lista)
		{
			if(a.isDueTime())
			{
System.err.println("CesServiceAviso-------checkAvisos----ACTIVA EL AVISO*****************************************************" + a.getTexto());
				Intent intent = new Intent(getBaseContext(), ActAvisoDlg.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("aviso", a);//.getTexto()
				getApplication().startActivity(intent);
			}
		}
	}
}
