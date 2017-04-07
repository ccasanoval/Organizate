package com.cesoft.organizate.svc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.cesoft.organizate.ActEdit;
import com.cesoft.organizate.App;
import com.cesoft.organizate.R;
import com.cesoft.organizate.util.Util;
import com.cesoft.organizate.models.AvisoGeo;
import com.cesoft.organizate.models.Objeto;
import com.cesoft.organizate.util.Log;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;


////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class CesServiceAviso extends IntentService
{
	private static final String TAG = CesServiceAviso.class.getSimpleName();
	private static final int GEOFEN_DWELL_TIME = 5*60000;//TODO:customize in settings...
	private static final long DELAY_LOAD = 10*60*1000;//TODO: ajustar
	private static final long DELAY_CHECK = 5*60*1000;

	private ArrayList<Objeto> _lista = new ArrayList<>();

	private CesGeofenceStore _GeofenceStore;
	private ArrayList<AvisoGeo> _listaGeo = new ArrayList<>();

	private static CesServiceAviso INSTANCE = null;
	private boolean _bRun = true;
		public static void stop()
		{
			if(INSTANCE != null)
			{
				INSTANCE._bRun=false;
				INSTANCE.stopSelf();
				if(INSTANCE._GeofenceStore != null)
				INSTANCE._GeofenceStore.clear();
				INSTANCE._GeofenceStore = null;
				INSTANCE = null;
			}
		}
		public static void start(Context c)
		{
			if(INSTANCE == null)
				c.startService(new Intent(c, CesServiceAviso.class));
		}


	//______________________________________________________________________________________________
	public CesServiceAviso()
	{
		super("OrganizateAviso");
		INSTANCE = this;
	}

	//______________________________________________________________________________________________
	@Override
	protected void onHandleIntent(Intent workIntent)
	{
		try
		{
			//String dataString = workIntent.getDataString();
			long tmLoad = System.currentTimeMillis() - 2*DELAY_LOAD;
			long tmCheck = System.currentTimeMillis() - 2*DELAY_LOAD;
			while(_bRun)
			{
Log.e(TAG, "---------------- LOOP ------------------------------------------------------------------");
				if(tmLoad + DELAY_LOAD < System.currentTimeMillis())
				{
					cargarListaTem();
					cargarListaGeo();
					tmLoad = System.currentTimeMillis();
				}
				if(tmCheck + DELAY_CHECK < System.currentTimeMillis())
				{
					checkAvisos();
					tmCheck = System.currentTimeMillis();
				}
				Thread.sleep(DELAY_CHECK/2);
			}
		}
		catch(InterruptedException e){Log.e(TAG, "onHandleIntent:e:---------------------------------",e);}
	}

	//______________________________________________________________________________________________
	private void cargarListaGeo()
	{
		try
		{
			_listaGeo.clear();
			if(_GeofenceStore != null)_GeofenceStore.clear();
			ArrayList<Geofence> aGeofences = new ArrayList<>();
			List<Objeto> lista = App.getLista(this);
			if(lista != null)
			for(Objeto o : lista)
			{
				if(o.getAvisoGeo() != null && o.getAvisoGeo().isActivo())
				{
					AvisoGeo a = o.getAvisoGeo();
					_listaGeo.add(a);
					aGeofences.add(new Geofence.Builder().setRequestId(a.getId())
							.setCircularRegion(a.getLatitud(), a.getLongitud(), a.getRadio()).setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(GEOFEN_DWELL_TIME)// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
							.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)// | Geofence.GEOFENCE_TRANSITION_EXIT
							.build());
				}
			}
//Log.e(TAG, "-------------------------------cargarListaGeo 9:-----------------------------------" + _listaGeo.size());
			//aGeofences =
			_GeofenceStore = new CesGeofenceStore(this, aGeofences);
		}
		catch(Exception e)
		{
			Log.e(TAG, "cargarListaGeo:e:-------------------------------------------",e);
		}
	}
	//______________________________________________________________________________________________
	private void cargarListaTem()
	{
		try
		{
			_lista.clear();
			List<Objeto> lista = App.getLista(this);
			if(lista != null)
			for(Objeto o : lista)
				if(o.getAvisoTem() != null && o.getAvisoTem().isActivo())
					_lista.add(o);
//Log.e(TAG, "------*********************************************------------------cargarLista:"+_lista.size());
		}
		catch(Exception e)
		{
			Log.e(TAG, "cargarLista:e:--------------------------------------------------------------",e);
		}
	}

	//______________________________________________________________________________________________
	private void checkAvisos()
	{
		if(_lista == null || _lista.size() == 0)return;
		for(Objeto o : _lista)
		{
			if(o == null)
				Log.e(TAG, "CesServiceAviso:checkAvisos: o == null !!!!!!!!!!!!!!!!!!! ");
			else
			if(o.getAvisoTem().isDueTime())
			{
Log.e(TAG, "checkAvisos:----ACTIVA EL AVISO*****************************************************" + o);
				Intent intent = new Intent(getBaseContext(), ActEdit.class);
				intent.putExtra(Objeto.class.getName(), o);
				Util.showAviso(getBaseContext(), getString(R.string.aviso_tem), o.getAvisoTem(), intent);
			}
		}
	}
}
