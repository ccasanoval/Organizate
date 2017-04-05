package com.cesoft.organizate.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { DbModule.class, })
public final class TodoModule
{
	private final Application application;
	public TodoModule(Application application)
	{
		this.application = application;
	}
	@Provides
	@Singleton
	Application provideApplication()
	{
		return application;
	}
}
