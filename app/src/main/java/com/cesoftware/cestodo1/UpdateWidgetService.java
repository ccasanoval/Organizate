package com.cesoftware.cestodo1;

import java.util.ArrayList;
import java.util.Iterator;

import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;

import android.content.Intent;
import android.os.IBinder;

import com.cesoftware.cestodo1.models.Objeto;

/**
 * Created by Cesar_Casanova on 04/01/2016.
 */
public class UpdateWidgetService extends Service
{
	private static Long _id = -1L;

	//______________________________________________________________________________________________
	@Override
	//public void onStart(Intent intent, int startId)
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try{
		String s="";
		ArrayList<Objeto> lista = new ArrayList<>();
		//Iterator<Objeto> it =(Iterator<Objeto>)Objeto.findAll(Objeto.class);
		//while(it.hasNext())lista.add(it.next());
		lista = (ArrayList<Objeto>)Objeto.findWithQuery(Objeto.class, "select * from Objeto where _padre is not null and _i_prioridad > 3 order by _i_prioridad desc");
		if(lista == null || lista.size() < 1)
			lista = (ArrayList<Objeto>)Objeto.findWithQuery(Objeto.class, "select * from Objeto where _padre is not null order by _i_prioridad desc");
		if(lista == null || lista.size() < 1)
			lista = (ArrayList<Objeto>)Objeto.findWithQuery(Objeto.class, "select * from Objeto order by _i_prioridad desc");

		if(lista != null && lista.size() > 0)
		{
			Objeto o = lista.get(0);
			if(lista.size() > 1)
			{
				for(int i=0; i < lista.size()-1; i++)
				{
					if(lista.get(i).getId().equals(_id))
					{
						o = lista.get(i+1);
						break;
					}
				}
			}
			_id = o.getId();
			s = o.getNombre();
		}

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

//    ComponentName thisWidget = new ComponentName(getApplicationContext(), CesWidgetProvider.class);
//    int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		for(int widgetId : allWidgetIds)
		{
			RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);

			//int number = (new Random().nextInt(100));
			remoteViews.setTextViewText(R.id.update, s);//"Random: " + String.valueOf(number));

			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(), CesWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();

		}
		catch(Exception e)
		{
			System.err.println("UpdateWidgetService: onStartCommand: ERROR: "+e);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	//______________________________________________________________________________________________
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}

/*
@Override
public int onStartCommand(Intent intent, int flags, int startId) {

    final Handler mHandler = new Handler();
    mRunnable = new Runnable() {
        @Override
        public void run() {
            MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext());
            boolean isInfoAvailable = myDBHelper.isAnyInfoAvailable(getApplicationContext());
            Toast.makeText(getApplicationContext(), String.valueOf(isInfoAvailable), Toast.LENGTH_LONG).show();
            mHandler.postDelayed(mRunnable, 10 * 1000);
        }
    };
    mHandler.postDelayed(mRunnable, 10 * 1000);
    return super.onStartCommand(intent, flags, startId);
}
<service
    android:name=".MyService"
    android:enabled="true"
    android:exported="true"
    android:stopWithTask="false">
</service>
* */