package com.cesoft.organizate2;

import android.app.Application;
import android.widget.Toast;

import com.cesoft.organizate2.db.DbObjeto;
import com.cesoft.organizate2.models.Objeto;
import com.cesoft.organizate2.svc.CesServiceAviso;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Presenter Main
@Singleton
class PreMain
{
	private static final String TAG = PreMain.class.getSimpleName();

	private Subscription _subscripcion;
	private ActMain _view;

	private Application _app;
	private BriteDatabase _db;
	@Inject PreMain(Application app, BriteDatabase db)
	{
		_db = db;
		_app = app;
	}

	//----------------------------------------------------------------------------------------------
	void unsubscribe()
	{
		if(_subscripcion != null)
			_subscripcion.unsubscribe();
		_subscripcion = null;
	}

	//----------------------------------------------------------------------------------------------
	void subscribe(ActMain view)
	{
		_view = view;
		_subscripcion = _db.createQuery(DbObjeto.TABLE, DbObjeto.QUERY)
			.mapToList(DbObjeto.MAPPER)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribeOn(Schedulers.io())
			.doOnError(new Action1<Throwable>()
			{
				@Override
				public void call(Throwable throwable)
				{
					android.util.Log.e(TAG, "onResume:createQuery:doOnError:Tarea------------------------------------------------"+throwable);
					Toast.makeText(_app, _app.getString(R.string.error_carga_lista), Toast.LENGTH_LONG).show();//TODO: to view
				}
			})
			.subscribe(new Action1<List<Objeto>>()
			{
				@Override
				public void call(List<Objeto> lista)
				{
					Objeto.conectarHijos(lista);
					//android.util.Log.e(TAG, "onResume:createQuery:subscribe:Tarea:------------------------------------------------"+lista.size());
					App.setLista(_app, lista);

					_view.showData(lista);

					if(Objeto.hayAvisoActivo(lista) && _view.pideGPS())
						CesServiceAviso.start(_app);
					else
						CesServiceAviso.stop();
				}
			});
	}

	//----------------------------------------------------------------------------------------------
	void onPermisionResult()
	{
		CesServiceAviso.stop();
		CesServiceAviso.start(_app);
	}
}
