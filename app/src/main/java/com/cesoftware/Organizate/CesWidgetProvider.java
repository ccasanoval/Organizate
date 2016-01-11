package com.cesoftware.Organizate;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.orm.SugarContext;

//http://www.vogella.com/tutorials/AndroidWidgets/article.html
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesWidgetProvider extends AppWidgetProvider
{
	public static String ACTION_WIDGET_SHOWAPP = "ActionReceiverShowApp";
	public static String ACTION_WIDGET_CHANGE = "ActionReceiverChange";

	//______________________________________________________________________________________________
	@Override
	public void onEnabled(Context context)
	{
		SugarContext.init(context);
	}

	//______________________________________________________________________________________________
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		// Update the widgets via the service and user click
		ComponentName thisWidget = new ComponentName(context, CesWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		context.startService(intent);

		// Open the app
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		Intent intent2 = new Intent(context.getApplicationContext(), MainActivity.class);
		PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
		remoteViews.setOnClickPendingIntent(R.id.lblNomApp, actionPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

/*
	// Update the widgets via user click
		PendingIntent actionPendingIntent;
		RemoteViews widView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		Intent intent1 = new Intent(context, CesWidgetProvider.class);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		intent1.setAction(ACTION_WIDGET_SHOWAPP);
		widView.setOnClickPendingIntent(R.id.txtTarea, actionPendingIntent);

		Intent intent2 = new Intent(context, CesWidgetProvider.class);
		intent2.setAction(ACTION_WIDGET_SHOWAPP);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
		widView.setOnClickPendingIntent(R.id.lblNomApp, actionPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, widView);
	}

	//______________________________________________________________________________________________
	@Override
	public void onReceive(Context context, Intent intent)
	{
    	if(intent.getAction().equals(ACTION_WIDGET_SHOWAPP))
		{
        	System.err.println("onReceive:" + ACTION_WIDGET_SHOWAPP);
    	}
		else
		{
			System.err.println("onReceive:OTRO");
        	super.onReceive(context, intent);
    	}
	}
	*/

}