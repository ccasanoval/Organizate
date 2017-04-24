package com.cesoft.organizate2.svc;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;

import android.content.Intent;
import android.os.IBinder;

import com.cesoft.organizate2.App;
import com.cesoft.organizate2.R;
import com.cesoft.organizate2.db.DbObjeto;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.util.Log;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 04/01/2016.
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesServiceUpdateWidget extends Service
{
	private static final String TAG = CesServiceUpdateWidget.class.getSimpleName();//"CesServUpdWdgt";
	private static Handler _h = null;
	private static Runnable _r = null;
	private static final int _DELAY = 5*60*1000;

	@Inject BriteDatabase _db;

	public CesServiceUpdateWidget(){}//Necesario

	@Override
	public void onCreate()
	{
		super.onCreate();
		App.getComponent(getApplicationContext()).inject(this);
	}

	//______________________________________________________________________________________________
	@Override
	//public void onStart(Intent intent, int startId)
	public int onStartCommand(final Intent intent, int flags, int startId)
	{
		if(_h == null)
		{
			_h = new Handler();
			_r = new Runnable()
			{
				@Override
				public void run()
				{
					Log.e(TAG,"--------------  RUN WIDGET  --------------");
					_h.postDelayed(_r, _DELAY);
					cambiarTextoWidget();
				}
			};
			_h.postDelayed(_r, _DELAY);
		}
		cambiarTextoWidget();
		//return
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	//______________________________________________________________________________________________
	@Override public IBinder onBind(Intent intent) { return null; }

	//______________________________________________________________________________________________
	private static int _iTarea = -1;
	private void cambiarTextoWidget()
	{
		//Log.e(TAG, " CAMBIAR TEXTO WIDGET ------------------");
		/*if(intent != null)
		{
			int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			if(allWidgetIds == null)Log.e(TAG, "setWidget:e:--------3333333------------------------- allWidgetIds = NULL");
		}*/

		try
		{
			final Context context = getApplicationContext();

			DbObjeto.getListaSync(_db, new DbObjeto.Listener<Objeto>()
			{
				@Override
				public void onError(Throwable err)
				{
					android.util.Log.e(TAG, "cambiarTextoWidget:e:------------------------------------------------" + err);
					setWidget(context.getString(R.string.sintareas));
				}
				@Override
				public void onDatos(List<Objeto> lista)
				{
					String sTarea = context.getString(R.string.sintareas);
					Objeto.conectarHijos(lista);
					if(lista.size() > 0)
					{
						//App.setLista(context, lista);
						_iTarea++;
						if(lista.size() <= _iTarea) _iTarea = 0;
						Objeto o = lista.get(_iTarea);
						sTarea = o.getNombre();
						if(o.getDescripcion().length() > 0) sTarea += " : " + o.getDescripcion();
					}
					//android.util.Log.e(TAG, "************************-----------"+_iTarea+"-----"+sTarea+"--------------------------------"+l.size());
					setWidget(sTarea);
				}
			});
/*
					//--------------
					_db.createQuery(DbObjeto.TABLE, DbObjeto.QUERY)
							.mapToList(DbObjeto.MAPPER)
							.observeOn(Schedulers.immediate())
							.subscribeOn(Schedulers.immediate())
							.doOnError(new Action1<Throwable>()
							{
								@Override
								public void call(Throwable throwable)
								{
									android.util.Log.e(TAG, "cambiarTextoWidget:e:------------------------------------------------" + throwable);
									setWidget(context.getString(R.string.sintareas));
								}
							})
							.subscribe(new Action1<List<Objeto>>()
							{
								@Override
								public void call(List<Objeto> l)
								{
									String sTarea = context.getString(R.string.sintareas);
									Objeto.conectarHijos(l);
									if(l.size() > 0)
									{
										App.setLista(context, l);

										_iTarea++;
										if(l.size() <= _iTarea) _iTarea = 0;
										Objeto o = l.get(_iTarea);
										sTarea = o.getNombre() + " : " + o.getDescripcion();
									}
									//android.util.Log.e(TAG, "************************-----------"+_iTarea+"-----"+sTarea+"--------------------------------"+l.size());
									setWidget(sTarea);
								}
							});*/

			//--------------
			//stopSelf();
		}
		catch(Exception e)
		{
			Log.e(TAG,"cambiarTextoWidget:e: ------------------------------------",e);
		}
	}

	//----------------------------------------------------------------------------------------------
	private void setWidget(String sTarea)
	{
		//Log.e(TAG, "setWidget:--------------------------------- sTarea = "+sTarea);
		Context context = getApplicationContext();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), CesWidgetProvider.class));
		for(int widgetId : allWidgetIds)
		{
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

			// Actualiza tarea actual
			remoteViews.setTextViewText(R.id.txtTarea, sTarea);

			//  onClick  ->  Actualiza tarea actual
			Intent clickIntent = new Intent(context, CesWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.txtTarea, pendingIntent);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	//----------------------------------------------------------------------------------------------
	// Restaura el servicio cuando se le mata el proceso
	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		Log.e(TAG, "-------------------onTaskRemoved---------------------");
		Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
		restartServiceIntent.setPackage(getPackageName());

		PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		alarmService.set(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + 500,
				restartServicePendingIntent);

		Log.e(TAG, "-------------------Reiniciando...---------------------");
		super.onTaskRemoved(rootIntent);
	}

}
