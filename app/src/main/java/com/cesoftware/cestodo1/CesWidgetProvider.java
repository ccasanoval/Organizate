package com.cesoftware.cestodo1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.cesoftware.cestodo1.models.Objeto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//http://www.vogella.com/tutorials/AndroidWidgets/article.html
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesWidgetProvider extends AppWidgetProvider
{
	private static final String ACTION_CLICK = "ACTION_CLICK";
	private static ArrayList<Objeto> _lista;
	private static int _i = 0;

	//______________________________________________________________________________________________
	@Override
	public void onEnabled(Context context)
	{
		_lista = Objeto.conectarHijos(Objeto.findAll(Objeto.class));
System.err.println("----------"+_lista.size());
	}

	//______________________________________________________________________________________________
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		//TODO: Buscar tareas nivel 3 o 2 con fecha limite cercana o con prioridad alta....
		//TODO: Add button to open app
		ComponentName thisWidget = new ComponentName(context, CesWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for(int widgetId : allWidgetIds)
		{
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

			if(_lista == null || _lista.size() < 1)
			{
				remoteViews.setTextViewText(R.id.update, "Lista de tareas vacía");//TODO: a recursos
			}
			else
			{
				_i = _i++ % _lista.size();
System.err.println("----------" + _i + " : " + _lista.size() +" : "+ _lista.get(_i).getNombre());

				remoteViews.setTextViewText(R.id.update, _lista.get(_i).getNombre());

				// Register an onClickListener
				Intent intent = new Intent(context, CesWidgetProvider.class);
				intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
				appWidgetManager.updateAppWidget(widgetId, remoteViews);
			}
		}
	}
}
