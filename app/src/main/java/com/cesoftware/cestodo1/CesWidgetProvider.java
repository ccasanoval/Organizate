package com.cesoftware.cestodo1;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.cesoftware.cestodo1.models.Objeto;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//http://www.vogella.com/tutorials/AndroidWidgets/article.html
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesWidgetProvider extends AppWidgetProvider
{
	//private static final String ACTION_CLICK = "ACTION_CLICK";
	//private static ArrayList<Objeto> _lista;
	//private static int _i = 0;

	//______________________________________________________________________________________________
	@Override
	public void onEnabled(Context context)
	{
		SugarContext.init(context);
		//_lista = Objeto.conectarHijos(Objeto.findAll(Objeto.class));
		//_lista = new ArrayList<>();
		//Iterator<Objeto> it = Objeto.findAll(Objeto.class);//.find(Objeto.class, " _iPrioridad > 3 ");//
		//while(it.hasNext())_lista.add(it.next());
		//TODO: guardar nivel en objeto y aqui buscar los que no son nivel 1 que son demasiado generales...
		//_lista = (ArrayList<Objeto>)Objeto.findWithQuery(Objeto.class, "select * from Objeto where _padre is null and _i_prioridad > 3 order by _i_prioridad desc");
//System.err.println("onEnabled----------"+_lista.size());
	}

	//______________________________________________________________________________________________
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		ComponentName thisWidget = new ComponentName(context, CesWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		// Update the widgets via the service
		context.startService(intent);
	}

	//______________________________________________________________________________________________
/*	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		//TODO: Buscar tareas nivel 3 o 2 con fecha limite cercana o con prioridad alta....
		//TODO: Add button to open app
		ComponentName thisWidget = new ComponentName(context, CesWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for(int widgetId : allWidgetIds)
		{
			Bundle options = appWidgetManager.getAppWidgetOptions(widgetId);
			int category = options.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);
			boolean isLockScreen = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;//Es un widget en la pantalla de bloqueo
System.err.println("isLockScreen----------"+isLockScreen);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

			if(_lista == null || _lista.size() < 1)
			{
				remoteViews.setTextViewText(R.id.update, "Lista de tareas vacía");//TODO: a recursos
System.err.println("onUpdate----------_lista.size()<1" + _i);
			}
			else
			{
System.err.println("onUpdate----------" + _i+":::"+_lista.size());
				_i = _i++ % _lista.size();
System.err.println("onUpdate----------" + _i + " : " + _lista.size() +" : "+ _lista.get(_i).getNombre());

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
	}*/
}
