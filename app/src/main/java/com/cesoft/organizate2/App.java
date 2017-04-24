package com.cesoft.organizate2;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.cesoft.organizate2.di.DaggerTodoComponent;
import com.cesoft.organizate2.di.TodoComponent;
import com.cesoft.organizate2.di.TodoModule;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.svc.CesServiceUpdateWidget;
import com.cesoft.organizate2.svc.CesWidgetProvider;

import java.util.List;

import timber.log.Timber;

////////////////////////////////////////////////////////////////////////////////////////////////////
//http://www.vogella.com/tutorials/Dagger/article.html
public final class App extends MultiDexApplication//android.app.Application  Al meter firebase...
{
	private TodoComponent mainComponent;
	private List<Objeto> _lista;//TODO: mejor en un modulo de datos globales!!!

	@Override public void onCreate()
	{
		super.onCreate();
		if(BuildConfig.DEBUG)Timber.plant(new Timber.DebugTree());
		mainComponent = DaggerTodoComponent.builder().todoModule(new TodoModule(this)).build();

		/*ComponentName thisWidget = new ComponentName(getApplicationContext(), CesWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		Intent intent = new Intent(getApplicationContext(), CesServiceUpdateWidget.class);
		//intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		startService(intent);*/
	}

	public static TodoComponent getComponent(Context context)
	{
		return ((App)context.getApplicationContext()).mainComponent;
	}
	//----------------------------------------------------------------------------------------------
	public static List<Objeto> getLista(Context context)
	{
		return ((App)context.getApplicationContext())._lista;
	}
	public static void setLista(Context context, List<Objeto> lista)
	{
		((App)context.getApplicationContext())._lista = lista;
	}
}
