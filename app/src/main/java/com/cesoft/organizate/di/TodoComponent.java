package com.cesoft.organizate.di;

import com.cesoft.organizate.ActEdit;
import com.cesoft.organizate.ActMain;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = TodoModule.class)
public interface TodoComponent
{
	void inject(ActMain v);
	void inject(ActEdit v);
}
