package com.cesoft.organizate;


import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.cesoft.organizate.db.DbObjeto;
import com.cesoft.organizate.models.Objeto;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


// PLAY STORE DEVELOPER CONSOLE : https://play.google.com/apps/publish/?hl=es&dev_acc=11164117065791896000
//MAP API CREDENTIAL: https://console.developers.google.com/apis/credentials?project=shining-medium-121911
//GOOGLE API SIGN : https://developers.google.com/mobile/add?platform=android&cntapi=signin&cntapp=Default%20Demo%20App&cntpkg=com.google.samples.quickstart.signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fstart%3Fconfigured%3Dtrue&cntlbl=Continue%20with%20Try%20Sign-In
//LAUNCH SIGNED APK : https://www.jetbrains.com/idea/help/generating-a-signed-release-apk-through-an-artifact.html

/*
GoogleService failed to initialize, status: 10, Missing an expected resource: 'R.string.google_app_id' for initializing Google services.
Possible causes are missing google-services.json or com.google.gms.google-services gradle plugin.
Scheduler not set. Not logging error/warn.
Uploading is not possible. App measurement disabled

https://developers.google.com/identity/sign-in/android/start?hl=en
https://developers.google.com/mobile/add?platform=android&cntapi=signin&cntapp=Default%20Demo%20App&cntpkg=com.google.samples.quickstart.signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fstart%3Fconfigured%3Dtrue&cntlbl=Continue%20with%20Try%20Sign-In
*/


//TODO: importar y exportar por correo
//TODO: Mejorar widget: Botones para navegar por tareas y mejorar aspecto :https://developer.android.com/design/patterns/widgets.html
//TODO: Nueva version con Backendless & Firebase
//TODO: Cuando el elemento ocupa dos lineas, contar una extra row al calcular espacio !!!!!!

//TODO: indexed by google  https://moz.com/blog/how-to-get-your-app-content-indexed-by-google
//TODO: Searchable : https://developer.android.com/guide/topics/search/search-dialog.html?hl=es
//TODO: backup : https://developer.android.com/preview/backup/index.html
//TODO: proguard ?

//TODO:Settings dialog: Widget Time change? Desactivar por (hoy|hora|...)
//TODO: No debería rodar servicio de aviso si no hay avisos, activar cuando se cree alguno...? Los avisos que no tienen configuracion deberían borrarse o ignorarse... saltarian a todas horas...

//TODO: Private but free git host? gitHub make ur code public...

//TODO: mejorar sistema de fecha de aviso: lista de fecha-horas  y mejorar aspecto botones...
//TODO: Revisar "no molestar por hoy"
//TODO: Toolbar en settings : problema, se cambio modo de barra por lista...
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActMain extends AppCompatActivity
{
	private static final String TAG = ActMain.class.getSimpleName();

	private static ExpandableListView _expListView;

	@Inject BriteDatabase db;
	private Subscription subTarea;//, subAvisoTem, subAvisoGeo;

	//______________________________________________________________________________________________
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	//______________________________________________________________________________________________
	@Override
	public void onResume()
	{
		super.onResume();

		subTarea = db.createQuery(DbObjeto.TABLE, DbObjeto.QUERY)
			.mapToList(DbObjeto.MAPPER)
			.observeOn(AndroidSchedulers.mainThread())
			.doOnError(new Action1<Throwable>()
			{
				@Override
				public void call(Throwable throwable)
				{
					Log.e(TAG, "onResume:createQuery:doOnError:Tarea------------------------------------------------"+throwable);
				}
			})
			.subscribe(new Action1<List<Objeto>>()
			{
				@Override
				public void call(List<Objeto> lista)
				{
					try
					{
						Objeto.conectarHijos(lista);
						Log.e(TAG, "onResume:createQuery:subscribe:Tarea:------------------------------------------------"+lista.size());
						//for(Objeto o : lista)Log.e(TAG, "onResume:-----------"+o);
					}
					catch(Exception e)
					{
						lista = new ArrayList<>();
					}
					App.setLista(ActMain.this, lista);
					_expListView.setAdapter(new NivelUnoListAdapter(ActMain.this.getApplicationContext(), _expListView, lista));
				}
			});

		/*subAvisoGeo = db.createQuery(DbAvisoGeo.TABLE, DbAvisoGeo.QUERY)
				.mapToList(DbAvisoGeo.MAPPER)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError(new Action1<Throwable>()
				{
					@Override
					public void call(Throwable throwable)
					{
						Log.e(TAG, "onResume:createQuery:doOnError:AvisoGeo------------------------------------------------"+throwable);
					}
				})
				.subscribe(new Action1<List<AvisoGeo>>()
				{
					@Override
					public void call(List<AvisoGeo> lista)
					{
						App.setListaAvisoGeo(ActMain.this, lista);
						//sync con la carga de tareas para no ejecutar esto primero?????
					}
				});
		subAvisoTem = db.createQuery(DbAvisoTem.TABLE, DbAvisoTem.QUERY)
				.mapToList(DbAvisoTem.MAPPER)
				.observeOn(AndroidSchedulers.mainThread())
				.doOnError(new Action1<Throwable>()
				{
					@Override
					public void call(Throwable throwable)
					{
						Log.e(TAG, "onResume:createQuery:doOnError:AvisoGeo------------------------------------------------"+throwable);
					}
				})
				.subscribe(new Action1<List<AvisoTem>>()
				{
					@Override
					public void call(List<AvisoTem> lista)
					{
						App.setListaAvisoTem(ActMain.this, lista);
					}
				});*/
		/*}
		catch(Exception e)
		{
			Log.e(TAG, "onResume:e:-----------------------------------------------------------------",e);
		}*/
	}
	//______________________________________________________________________________________________
	@Override public void onPause()
	{
		super.onPause();
		subTarea.unsubscribe();
		//subAvisoGeo.unsubscribe();
		//subAvisoTem.unsubscribe();
	}

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//-----
		startHuevo();
		startAvisoService();
		//------
		_expListView = (ExpandableListView)findViewById(R.id.elv_todo);

		App.getComponent(this).inject(this);

//datosTEST();
//datosTESTgeo();
//createGeofencesTEST();
		cargarLista();
		//------
		//En layout debes anadir app:layout_behavior="@string/appbar_scrolling_view_behavior" para que el toolbar no se coma el listview
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		if(fab != null)
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(ActMain.this, ActEdit.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	//______________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	//______________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		/*if(id == R.id.nuevo)
		{
			Intent intent = new Intent(this, ActEdit.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}*/
		if(id == R.id.config)
		{
			//Intent intent = new Intent(this, ActConfig.class);
			//startActivity(intent);
			startActivityForResult(new Intent(this, ActConfig.class), 69);
		}
		return super.onOptionsItemSelected(item);
	}

	//______________________________________________________________________________________________
	private void cargarLista()
	{
		/*ArrayList<DbObjeto> lista;
		try
		{
			lista = DbObjeto.conectarHijos(DbObjeto.findAll(DbObjeto.class));
		} catch(Exception e)
		{
			lista = new ArrayList<>();
		}
		ActEdit.setLista(lista);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));*/
	}

	//______________________________________________________________________________________________
	public void refrescarLista()
	{
		/*Iterator<DbObjeto> it = DbObjeto.findAll(DbObjeto.class);
		ArrayList<DbObjeto> lista = DbObjeto.conectarHijos(it);//Por que no funciona con la lista pasada????? Lo deja duplicado y el nuevo no es editable???
		ActEdit.setLista(lista);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
		_expListView.refreshDrawableState();*/
	}

	//______________________________________________________________________________________________
	public void startHuevo()
	{
		Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
		if(tb != null)
		tb.setOnClickListener(new View.OnClickListener()
		{
			private int _nClicks = 0;
			private Date _dtClicks;
			@Override
			public void onClick(View v)
			{
				if(_nClicks == 0)
					_dtClicks = new Date();
				_nClicks++;
				if(new Date().getTime() - _dtClicks.getTime() > 1000)
				{
					_nClicks = 1;
					_dtClicks = new Date();
				}
				else if(_nClicks > 4)
				{
					_nClicks = 0;
					//http://www.anddev.org/simple_splash_screen-t811.html
					//http://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen
					Intent i = new Intent(ActMain.this, ActSplash.class);
					ActMain.this.startActivity(i);
				}
			}
		});
	}

	//______________________________________________________________________________________________
	public void startAvisoService()
	{
		Intent i = new Intent(this, CesServiceAviso.class);
		startService(i);
	}



/*
	private static void datosTESTgeo()
	{
		try{
		AvisoGeo ag;

		ag = new AvisoGeo();
		ag.setActivo(true);
		ag.setTexto("Geofence uno");
		ag.setGeoPosicion(40.4890984, -3.6512994, 1500);
		ag.save();

		ag = new AvisoGeo();
		ag.setActivo(true);
		ag.setTexto("Geofence dos");
		ag.setGeoPosicion(40.4228029, -3.5339735, 1500);
		ag.save();

		ag = new AvisoGeo();
		ag.setActivo(true);
		ag.setTexto("Geofence tres");
		ag.setGeoPosicion(40.4170875, -3.5746174, 500);
		ag.save();

		}catch(Exception e){System.err.println("datosTESTgeo:e:"+e);}
	}
*/
	//______________________________________________________________________________________________
	/*private static void datosTEST()
	{
		int i=0;
		DbObjeto[] lista = new DbObjeto[13];
		DbObjeto o0, o1, o2;

		DbObjeto.deleteAll(DbObjeto.class);

		o0 = new DbObjeto("HEALTH", null);
			o1 = new DbObjeto("SLEEP WELL", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Don't loose time No TV", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("After diner go bed", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Count & Plan sleep hours", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Don't waste daytime 4 recover", o1);
				o1.addHijo(o2);
			o1 = new DbObjeto("EXERCISE", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("GYM 2-3 times a week", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Home weights once", o1);
				o1.addHijo(o2);
			o1 = new DbObjeto("EAT WELL", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Fresh Vegs & Fruit", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("No shitty food!", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Protein shakes", o1);
				o1.addHijo(o2);
			o1 = new DbObjeto("METRO", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Care 4ur clothes", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Care 4ur skin", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new DbObjeto("WEALTH", null);
			o1 = new DbObjeto("RENT T HOUSE", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Change Sofa", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Clean terrace", o1);
				o1.addHijo(o2);
				o2 = new DbObjeto("Sell bike parts", o1);
				o1.addHijo(o2);
			o1 = new DbObjeto("DONT SPEND WITHOUT NEED", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Don't abuse AliExpress", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new DbObjeto("TIME", null);
			o1 = new DbObjeto("PLAN ACTIVITIES", o0);
			o0.addHijo(o1);
				o2 = new DbObjeto("Day, Week, Month schedules", o1);
				o1.addHijo(o2);
			o1 = new DbObjeto("MULTITASKING", o0);
			o0.addHijo(o1);
		lista[i++]=o0;
		o0 = new DbObjeto("CUATRO", null);
		lista[i++]=o0;

		for(int j=0; j < i; j++)
		{
			long id = lista[j].save();
			DbObjeto[] ao =  lista[j].getHijos();
			for(int k=0; k < ao.length; k++)
			{
				ao[k].save();
				DbObjeto[] ao2 =  ao[k].getHijos();
				for(int l=0; l < ao2.length; l++)
					ao2[l].save();
			}
		}
	}*/
}

