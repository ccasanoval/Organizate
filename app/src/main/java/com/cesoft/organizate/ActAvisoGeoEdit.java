package com.cesoft.organizate;

import com.cesoft.organizate.models.AvisoGeo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


////////////////////////////////////////////////////////////////////////////////////////////////////
//Todo: Añadir boton en ActEdit
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoGeoEdit extends AppCompatActivity implements GoogleMap.OnCameraChangeListener, OnMapReadyCallback
{
	private AvisoGeo _a;
	private TextView _txtAviso;
	private Switch _swtActivo;
	private GoogleMap _Map;

	private String[] _asRadio = {"10 m", "50 m", "100 m", "200 m", "300 m", "400 m", "500 m", "750 m", "1 Km", "2 Km", "3 Km", "4 Km", "5 Km", "7.5 Km", "10 Km"};
	private int[]    _adRadio = { 10,     50,     100,     200,     300,     400,     500,     750,     1000,   2000,   3000,   4000,   5000,   7500,     10000};
	private Spinner  _spnRadio;
	private float    _radio;
	private Location _locLast;
	private TextView _lblPosicion;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_avisogeo_edit);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				ActAvisoGeoEdit.this.finish();
			}
		});

		_txtAviso = (TextView) findViewById(R.id.txtAviso);
		_swtActivo = (Switch) findViewById(R.id.bActivo);
		_spnRadio = (Spinner)findViewById(R.id.spnRadio);

		_lblPosicion = (TextView)findViewById(R.id.lblPosicion);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, _asRadio);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spnRadio.setAdapter(adapter);
		_spnRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				_radio = _adRadio[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				_radio = 1000;//TODO:radio por defecto en settings
			}
		});

		//TODO:
		/*
		ImageButton btnMapa = (ImageButton) findViewById(R.id.btnMapa);
		btnMapa.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(getBaseContext(), ActMaps.class);
				i.putExtra("objeto", _a);
				startActivityForResult(i, ACC_MAPA);//TODO: si es guardado, borrado => refresca la vista, si no nada
			}
		});*/

		//------------------------------------------------------------------------------------------
		try
		{
			_a = this.getIntent().getParcelableExtra("avisoGeo");
			setValores();
		} catch(Exception e)
		{
			System.err.println("ActAvisoGeoEdit:onCreate:e:" + e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------

		//TODO: Hacer en inicio?? en servicio de avisos?
	/*	ArrayList<Geofence> _Geofences;
	ArrayList<LatLng> _GeofenceCoordinates;
	ArrayList<Integer> _GeofenceRadius;

		_Geofences = new ArrayList<>();
		_GeofenceCoordinates = new ArrayList<>();
		_GeofenceRadius = new ArrayList<>();
		// Adding geofence coordinates to array.
		_GeofenceCoordinates.add(new LatLng(40.4890984, -3.6512994));
		_GeofenceCoordinates.add(new LatLng(40.4228029, -3.5339735));
		_GeofenceCoordinates.add(new LatLng(40.4890984, -3.6512994));
		// Adding associated geofence radius' to array.
		_GeofenceRadius.add(1500);
		_GeofenceRadius.add(1500);
		_GeofenceRadius.add(1500);
		// Bulding the geofences and adding them to the geofence array.
		// Performing Arts Center
		_Geofences.add(new Geofence.Builder().setRequestId("Geofence 1").setCircularRegion(_GeofenceCoordinates.get(0).latitude, _GeofenceCoordinates.get(0).longitude, _GeofenceRadius.get(0)).setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
		_Geofences.add(new Geofence.Builder().setRequestId("Geofence 2").setCircularRegion(_GeofenceCoordinates.get(1).latitude, _GeofenceCoordinates.get(1).longitude, _GeofenceRadius.get(1)).setExpirationDuration(Geofence.NEVER_EXPIRE).setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		_Geofences.add(new Geofence.Builder().setRequestId("Geofence 3").setCircularRegion(_GeofenceCoordinates.get(2).latitude, _GeofenceCoordinates.get(2).longitude, _GeofenceRadius.get(2)).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		_GeofenceStore = new CesGeofenceStore(this, _Geofences);*/
		//------------------------------------------------------------------------------------------
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		/*int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.getBaseContext());
		if(i != ConnectionResult.SUCCESS)//SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID
			GoogleApiAvailability.getInstance().getErrorDialog(this, i, 0);*/
	}

	private void setPosAct(String s){_lblPosicion.setText(s);}
	private void setPosAct(double lat, double lon){_lblPosicion.setText(lat + "/" + lon);}
	//______________________________________________________________________________________________
	private void setValores()
	{
		_txtAviso.setText(_a.getTexto());
		_swtActivo.setChecked(_a.getActivo());

		/*_radio = _a.getRadio();
		for(int i=0; i < _adRadio.length; i++)
		{
			if(_radio == _adRadio[i])
				{
					_spnRadio.setSelection(i);
					break;
				}
		}*/

		{
System.err.println("--------0");
			if(_locLast == null)_locLast = new Location("dummyprovider");
			_locLast.setLatitude(_a.getLatitud());
			_locLast.setLongitude(_a.getLongitud());
			setPosAct(_a.getLatitud(), _a.getLongitud());
System.err.println("--------1");
			System.err.println("--------2"+_a.getRadio());
			_radio = _a.getRadio();
			//int spinnerPosition = _adapter.getPosition("10 Km");
			System.err.println("--------3");
			for(int i=0; i < _adRadio.length; i++)
			{
				if(_radio == _adRadio[i])
				{
					_spnRadio.setSelection(i);
					break;
				}
			}
		}
	}

	// DB SAVE
	private void saveValores()
	{
		_a.setTexto(_txtAviso.getText().toString());
		_a.setActivo(_swtActivo.isChecked());
		_a.setGeoPosicion(_locLast.getLatitude(), _locLast.getLongitude(), _radio);//TODO: if not null
		Intent data = new Intent();
		data.putExtra("aviso", _a);
		setResult(android.app.Activity.RESULT_OK, data);
		finish();
	}

	//____________________________________________________________________________________________________________________________________________________
	/// MENU
	//______________________________________________________________________________________________
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		saveValores();
		return super.onOptionsItemSelected(item);
	}

	//______________________________________________________________________________________________
	// PARA OnMapReadyCallback
	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		_Map = googleMap;

		if(_a != null && (_a.getLatitud()!=0 || _a.getLongitud()!=0))
		{
			LatLng latLng = new LatLng(_a.getLatitud(), _a.getLongitud());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
			_Map.animateCamera(cameraUpdate);
		}

		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)return;
		_Map.setMyLocationEnabled(true);
	}

	//______________________________________________________________________________________________
	// PARA GoogleMap.OnCameraChangeListener
	@Override
	public void onCameraChange(CameraPosition position)
	{
System.err.println("---------ActAvisoGeoEdit:onCameraChange");
		/*for(int i=0; i < _GeofenceCoordinates.size(); i++)// Makes sure the visuals remain when zoom changes.
		{
			_Map.addCircle(new CircleOptions().center(_GeofenceCoordinates.get(i)).radius(_GeofenceRadius.get(i)).fillColor(0x40ff0000).strokeColor(Color.TRANSPARENT).strokeWidth(2));
		}*/
		if(_a != null && (_a.getLatitud()!=0 || _a.getLongitud()!=0))
		{
			_Map.addCircle(new CircleOptions()
					.center(new LatLng(_a.getLatitud(), _a.getLongitud()))
					.radius(_a.getRadio())
					.fillColor(0x40ff0000)
					.strokeColor(Color.TRANSPARENT).strokeWidth(2));
		}
	}
}
