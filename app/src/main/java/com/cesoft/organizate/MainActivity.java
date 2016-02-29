package com.cesoft.organizate;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
//import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

import com.cesoft.organizate.models.Objeto;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//MAP API CREDENTIAL: https://console.developers.google.com/apis/credentials?project=shining-medium-121911
//GOOGLE API SIGN : https://developers.google.com/mobile/add?platform=android&cntapi=signin&cntapp=Default%20Demo%20App&cntpkg=com.google.samples.quickstart.signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fstart%3Fconfigured%3Dtrue&cntlbl=Continue%20with%20Try%20Sign-In
//LAUNCH SIGNED APK : https://www.jetbrains.com/idea/help/generating-a-signed-release-apk-through-an-artifact.html

//TODO: Botones con estilo como en Encuentrame
//TODO: No debería rodar servicio de aviso si no hay avisos, activar cuando se cree alguno...? Los avisos que no tienen configuracion deberían borrarse o ignorarse... saltarian a todas horas...
//TODO: cuando abres aviso pero no lo guardas no debería crear aviso
//TODO: Cuando el elemento ocupa dos lineas, contar una extra row al calcular espacio
////////////////////////////////////////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, LocationListener
{
	private static ExpandableListView _expListView;

	//______________________________________________________________________________________________
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//SugarContext.terminate();//look at manifest//No lo llamamos para que widget pueda consultar bbdd
	}

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//-----
		setEgg();
		setAvisos();
		//------
		ActEdit.setParentAct(this);
		_expListView = (ExpandableListView) findViewById(R.id.elv_todo);
		SugarContext.init(this);
//datosTEST();
//createGeofencesTEST();
		cargarLista();
		//------
		//En layout debes anadir app:layout_behavior="@string/appbar_scrolling_view_behavior" para que el toolbar no se coma el listview
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
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
		if(id == R.id.nuevo)
		{
			Intent intent = new Intent(this, ActEdit.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	//______________________________________________________________________________________________
	private void cargarLista()
	{
		ArrayList<Objeto> lista;
		try
		{
			lista = Objeto.conectarHijos(Objeto.findAll(Objeto.class));
		} catch(Exception e)
		{
			lista = new ArrayList<>();
		}
		ActEdit.setLista(lista);
		//Objeto[] ao = new Objeto[lista.size()];lista.toArray(ao);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
	}

	//______________________________________________________________________________________________
	public void refrescarLista()
	{
		Iterator<Objeto> it = Objeto.findAll(Objeto.class);
		ArrayList<Objeto> lista = Objeto.conectarHijos(it);//TODO:Por que no funciona con la lista pasada????? Lo deja duplicado y el nuevo no es editable???
		ActEdit.setLista(lista);
		_expListView.setAdapter(new NivelUnoListAdapter(this.getApplicationContext(), _expListView, lista));
		_expListView.refreshDrawableState();
	}

	//______________________________________________________________________________________________
	public void selectObjeto(Objeto o)
	{
		//TODO:dejar abierto el nodo modificado, Guardar es estados visible de los objetos para dejarlos luego igual???
		//Recorre la lista de objetos y mira flag: abierto o cerrado, si esta abierto despliegalo...
		//_expListView.setSelectedChild(groupPosition, childPosition, true);
	}

	//______________________________________________________________________________________________
	public void setEgg()//TODO: Mejorar huevo
	{
		Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
		tb.setOnTouchListener(new View.OnTouchListener()
		{
			private int _nClicks = 0;
			private Date _dtClicks;

			public boolean onTouch(View v, MotionEvent me)
			{
				if(_nClicks == 0)
					_dtClicks = new Date();
				System.err.println("1----click:" + _nClicks + " : " + (new Date().getTime() - _dtClicks.getTime()));
				_nClicks++;
				if(new Date().getTime() - _dtClicks.getTime() > 1000)
				{
					_nClicks = 1;
					_dtClicks = new Date();
					System.err.println("2----click:" + _nClicks + " : " + (new Date().getTime() - _dtClicks.getTime()));
					return true;
				} else if(_nClicks > 7)
				{
					_nClicks = 0;
					//http://www.anddev.org/simple_splash_screen-t811.html
					//http://stackoverflow.com/questions/5486789/how-do-i-make-a-splash-screen
					System.err.println("EGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
					Intent i = new Intent(MainActivity.this, ActSplash.class);
					MainActivity.this.startActivity(i);
					return true;
				}
				return false;
			}
		});
	}

	//______________________________________________________________________________________________
	public void setAvisos()
	{
		Intent i = new Intent(this, CesServiceAviso.class);
		//i.setData("");
		startService(i);
	}

	//List<Geofence> mGeofenceList;
	private List<Geofence> mGeofenceList2 = new ArrayList<Geofence>();
	//private CesGeofenceStore mGeofenceStorage = new CesGeofenceStore(this);
	private CesGeofence mGeofence1, mGeofence2, mGeofence3;
	private GoogleApiClient mGoogleApiClient;
	public void createGeofencesTEST()
	{
		mGoogleApiClient = new GoogleApiClient.Builder(this.getApplicationContext())
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		mGoogleApiClient.connect();
		mLocationRequest = new LocationRequest();// This is purely optional and has nothing to do with geofencing. I added this as a way of debugging. Define the LocationRequest.
		mLocationRequest.setInterval(10000);// We want a location update every 10 seconds.
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);// We want the location to be as accurate as possible.

		/*mGeofenceList.add(new Geofence.Builder()
			.setRequestId("entry.getKey")// Set the request ID of the geofence. This is a string to identify this geofence.
			.setCircularRegion(40.4890984, -3.6512994, 1000)
			.setExpirationDuration(Geofence.NEVER_EXPIRE)
    		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
    		.build());*/
		// Create internal "flattened" objects containing the geofence data.
		mGeofence1 = new CesGeofence("ZONA_SHT",        //id
				40.4890984,        //lat
				-3.6512994,        //lon
				1000,            //radius meters
				5 * 60 * 1000,        //geofence expiration time
				Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
		mGeofence2 = new CesGeofence("ZONA_OLD", 40.4228029, -3.5339735, 1000, Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
		mGeofence3 = new CesGeofence("ZONA_NEW", 40.4172096, -3.574815, 1000, Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
		// Store these flat versions in SharedPreferences and add them to the geofence list.
		/*mGeofenceStorage.setGeofence("1", mGeofence1);
		mGeofenceStorage.setGeofence("2", mGeofence2);
		mGeofenceStorage.setGeofence("3", mGeofence3);*/
		/*mGeofenceList.add(mGeofence1.toGeofence());
		mGeofenceList.add(mGeofence2.toGeofence());
		mGeofenceList.add(mGeofence3.toGeofence());*/

		mGeofenceList2.add(new Geofence.Builder()
			.setRequestId("ZONA_SHT")
			.setCircularRegion(40.4890984, -3.6512994, 1000)
			.setExpirationDuration(Geofence.NEVER_EXPIRE)
    		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
    		.build());
		mGeofenceList2.add(new Geofence.Builder()
			.setRequestId("ZONA_OLD")
			.setCircularRegion(40.4228029, -3.5339735, 1000)
			.setExpirationDuration(Geofence.NEVER_EXPIRE)
    		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
    		.build());
		mGeofenceList2.add(new Geofence.Builder().setRequestId("ZONA_NEW").setCircularRegion(40.4172096, -3.574815, 1000).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());
	}

	private GeofencingRequest getGeofencingRequest()
	{
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);//INITIAL_TRIGGER_DWELL
		builder.addGeofences(mGeofenceList2);
		return builder.build();
	}

	PendingIntent mGeofencePendingIntent;
	private PendingIntent getGeofencePendingIntent()
	{
		// Reuse the PendingIntent if we already have it.
		if(mGeofencePendingIntent != null)
			return mGeofencePendingIntent;
		Intent intent = new Intent(this, CesServiceAvisoGeo.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofences() and removeGeofences().
		mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return mGeofencePendingIntent;
	}


	private LocationRequest mLocationRequest;
	GeofencingRequest mGeofencingRequest;
	@Override
	public void onConnected(Bundle bundle)
	{
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)return;
		LocationServices.GeofencingApi
				.addGeofences(mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent())
				.setResultCallback(this);

		mGeofencingRequest = getGeofencingRequest();
		getGeofencePendingIntent();
		// This is for debugging only and does not affect geofencing.
		if(ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			&& ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			return;
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		// Submitting the request to monitor geofences.
		PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, mGeofencingRequest, mGeofencePendingIntent);
		// Set the result callbacks listener to this class.
		pendingResult.setResultCallback(this);
	}
	@Override
	public void onConnectionSuspended(int i)
	{
		System.err.println("-------------------------------onConnectionSuspended:"+i);
	}
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		System.err.println("-------------------------------onConnectionFailed:"+connectionResult);
	}
	@Override
	public void onLocationChanged(Location location)
	{
		System.err.println("-------------------------------onLocationChanged:"+location);
	}
	@Override
	public void onResult(Status status)
	{
		System.err.println("-------------------------------onResult:"+status);
	}


	/* STOP
	LocationServices.GeofencingApi.removeGeofences(
            mGoogleApiClient,
            // This is the same pending intent that was used in addGeofences().
            getGeofencePendingIntent()
    ).setResultCallback(this); // Result processed in onResult().
	}
	 */


	//______________________________________________________________________________________________
	/*private static void datosTEST()
	{
		int i=0;
		Objeto[] lista = new Objeto[13];
		Objeto o0, o1, o2;

		Objeto.deleteAll(Objeto.class);

		o0 = new Objeto("HEALTH", null);
			o1 = new Objeto("SLEEP WELL", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Don't loose time No TV", o1);
				o1.addHijo(o2);
				o2 = new Objeto("After diner go bed", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Count & Plan sleep hours", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Don't waste daytime 4 recover", o1);
				o1.addHijo(o2);
			o1 = new Objeto("EXERCISE", o0);
			o0.addHijo(o1);
				o2 = new Objeto("GYM 2-3 times a week", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Home weights once", o1);
				o1.addHijo(o2);
			o1 = new Objeto("EAT WELL", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Fresh Vegs & Fruit", o1);
				o1.addHijo(o2);
				o2 = new Objeto("No shitty food!", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Protein shakes", o1);
				o1.addHijo(o2);
			o1 = new Objeto("METRO", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Care 4ur clothes", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Care 4ur skin", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new Objeto("WEALTH", null);
			o1 = new Objeto("RENT T HOUSE", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Change Sofa", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Clean terrace", o1);
				o1.addHijo(o2);
				o2 = new Objeto("Sell bike parts", o1);
				o1.addHijo(o2);
			o1 = new Objeto("DONT SPEND WITHOUT NEED", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Don't abuse AliExpress", o1);
				o1.addHijo(o2);
		lista[i++]=o0;

		o0 = new Objeto("TIME", null);
			o1 = new Objeto("PLAN ACTIVITIES", o0);
			o0.addHijo(o1);
				o2 = new Objeto("Day, Week, Month schedules", o1);
				o1.addHijo(o2);
			o1 = new Objeto("MULTITASKING", o0);
			o0.addHijo(o1);
		lista[i++]=o0;
		o0 = new Objeto("CUATRO", null);
		lista[i++]=o0;

		for(int j=0; j < i; j++)
		{
			long id = lista[j].save();
			Objeto[] ao =  lista[j].getHijos();
			for(int k=0; k < ao.length; k++)
			{
				ao[k].save();
				Objeto[] ao2 =  ao[k].getHijos();
				for(int l=0; l < ao2.length; l++)
					ao2[l].save();
			}
		}
	}*/
}

