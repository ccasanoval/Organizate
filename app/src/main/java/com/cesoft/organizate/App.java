package com.cesoft.organizate;

import android.app.Application;
import android.content.Context;

import com.cesoft.organizate.di.DaggerTodoComponent;
import com.cesoft.organizate.di.TodoComponent;
import com.cesoft.organizate.di.TodoModule;
import com.cesoft.organizate.models.Objeto;

import java.util.List;

import timber.log.Timber;

public final class App extends Application
{
	private TodoComponent mainComponent;
	private List<Objeto> _lista;//TODO: mejor en un modulo de datos globales!!!

	@Override public void onCreate()
	{
		super.onCreate();
		if(BuildConfig.DEBUG)Timber.plant(new Timber.DebugTree());
		mainComponent = DaggerTodoComponent.builder().todoModule(new TodoModule(this)).build();
	}

	public static TodoComponent getComponent(Context context)
	{
		return ((App)context.getApplicationContext()).mainComponent;
	}
	public static List<Objeto> getLista(Context context)
	{
		return ((App)context.getApplicationContext())._lista;
	}
	public static void setLista(Context context, List<Objeto> lista)
	{
		((App)context.getApplicationContext())._lista = lista;
	}
}
