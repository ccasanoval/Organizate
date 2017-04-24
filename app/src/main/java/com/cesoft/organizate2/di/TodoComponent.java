package com.cesoft.organizate2.di;

import com.cesoft.organizate2.ActEdit;
import com.cesoft.organizate2.ActMain;
import com.cesoft.organizate2.svc.CesServiceUpdateWidget;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = TodoModule.class)
public interface TodoComponent
{
	void inject(ActMain v);
	void inject(ActEdit v);
	void inject(CesServiceUpdateWidget v);

}
