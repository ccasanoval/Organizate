package com.cesoftware.Organizate;

import com.cesoftware.Organizate.models.AvisoGeo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


////////////////////////////////////////////////////////////////////////////////////////////////////
//Todo: Añadir boton en ActEdit
////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoGeoEdit extends AppCompatActivity implements GoogleMap.OnCameraChangeListener
{
	private AvisoGeo _a;
	private TextView _txtAviso;
	private Switch _swtActivo;
	private GoogleMap _Map;
	//private MapView _vMap;
	ArrayList<Geofence> _Geofences;
	ArrayList<LatLng> _GeofenceCoordinates;
	ArrayList<Integer> _GeofenceRadius;
	private CesGeofenceStore _GeofenceStore;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_avisogeo_edit);
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
		//------------------------------------------------------------------------------------------
		try
		{
			_a = this.getIntent().getParcelableExtra("avisoGeo");
			setValores();
		} catch(Exception e)
		{
			System.err.println("ActAvisoGeoEdit:onCreate:ERROR:" + e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
		_Geofences = new ArrayList<Geofence>();
		_GeofenceCoordinates = new ArrayList<LatLng>();
		_GeofenceRadius = new ArrayList<Integer>();
		// Adding geofence coordinates to array.
		_GeofenceCoordinates.add(new LatLng(43.042861, -87.911559));
		_GeofenceCoordinates.add(new LatLng(43.042998, -87.909753));
		_GeofenceCoordinates.add(new LatLng(43.040732, -87.921364));
		_GeofenceCoordinates.add(new LatLng(43.039912, -87.897038));
		// Adding associated geofence radius' to array.
		_GeofenceRadius.add(100);
		_GeofenceRadius.add(50);
		_GeofenceRadius.add(160);
		_GeofenceRadius.add(160);
		// Bulding the geofences and adding them to the geofence array.
		// Performing Arts Center
		_Geofences.add(new Geofence.Builder().setRequestId("Performing Arts Center")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(_GeofenceCoordinates.get(0).latitude, _GeofenceCoordinates.get(0).longitude, _GeofenceRadius.get(0)).setExpirationDuration(Geofence.NEVER_EXPIRE)
						// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
				.setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		// Starbucks
		_Geofences.add(new Geofence.Builder().setRequestId("Starbucks")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(_GeofenceCoordinates.get(1).latitude, _GeofenceCoordinates.get(1).longitude, _GeofenceRadius.get(1)).setExpirationDuration(Geofence.NEVER_EXPIRE)
						// Required when we use the transition type of GEOFENCE_TRANSITION_DWELL
				.setLoiteringDelay(30000).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		// Milwaukee Public Museum
		_Geofences.add(new Geofence.Builder().setRequestId("Milwaukee Public Museum")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(_GeofenceCoordinates.get(2).latitude, _GeofenceCoordinates.get(2).longitude, _GeofenceRadius.get(2)).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		// Milwaukee Art Museum
		_Geofences.add(new Geofence.Builder().setRequestId("Milwaukee Art Museum")
				// The coordinates of the center of the geofence and the radius in meters.
				.setCircularRegion(_GeofenceCoordinates.get(3).latitude, _GeofenceCoordinates.get(3).longitude, _GeofenceRadius.get(3)).setExpirationDuration(Geofence.NEVER_EXPIRE).setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT).build());
		// Add the geofences to the GeofenceStore object.
		_GeofenceStore = new CesGeofenceStore(this, _Geofences);

		//------------------------------------------------------------------------------------------

		SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
		//_vMap = (MapView)findViewById(R.id.map);
		//_vMap.onCreate(savedInstanceState);
		mapFragment.getMapAsync(new OnMapReadyCallback()
		{
			@Override
			public void onMapReady(GoogleMap map)
			{
				_Map = map;
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				map.setTrafficEnabled(true);
				map.setIndoorEnabled(true);
				map.setBuildingsEnabled(true);
				map.getUiSettings().setZoomControlsEnabled(true);
				setUpMap();
			}
		});
	}

	@Override
	protected void onStop()
	{
		//_GeofenceStore.disconnect();
		super.onStop();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		/*int i = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this.getBaseContext());
		if(i == ConnectionResult.SUCCESS)//SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID
			setUpMapIfNeeded();
		else
			GoogleApiAvailability.getInstance().getErrorDialog(this, i, 0);*/
	}

	/*private void setUpMapIfNeeded()
	{
		// Do a null check to confirm that we have not already instantiated the map.
		if(_Map == null)
		{
			// Try to obtain the map from the SupportMapFragment.
			MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
			//getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this.contex);
			// Check if we were successful in obtaining the map.
			if(_Map != null)
				setUpMap();
		}
	}
	/*@Override
	public void onMapReady(GoogleMap map)
	{
		//DO WHATEVER YOU WANT WITH GOOGLEMAP
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setTrafficEnabled(true);
		map.setIndoorEnabled(true);
		map.setBuildingsEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);

		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
System.err.println("----------ActivityCompat.checkSelfPermission--------------");
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation for ActivityCompat#requestPermissions for more details.
			return;
		}
		map.setMyLocationEnabled(true);

		_Map = map;
		setUpMap();
	}*/


	private void setUpMap()
	{
		// Centers the camera over the building and zooms int far enough to show the floor picker.
		_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.039634, -87.908395), 14));
		_Map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		_Map.setIndoorEnabled(false);
		_Map.setOnCameraChangeListener(this);

		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
System.err.println("----------ActivityCompat.checkSelfPermission--------------");
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation for ActivityCompat#requestPermissions for more details.
			return;
		}
		_Map.setMyLocationEnabled(true);
	}

	@Override
	public void onCameraChange(CameraPosition position)
	{
		// Makes sure the visuals remain when zoom changes.
		for(int i = 0; i < _GeofenceCoordinates.size(); i++)
		{
			_Map.addCircle(new CircleOptions().center(_GeofenceCoordinates.get(i))
					.radius(_GeofenceRadius.get(i))
					.fillColor(0x40ff0000)
					.strokeColor(Color.TRANSPARENT).strokeWidth(2));
		}
	}

	//____________________________________________________________________________________________________________________________________________________

	//______________________________________________________________________________________________
	private void setValores()
	{
		//_isNuevo = false;
		_txtAviso.setText(_a.getTexto());
		_swtActivo.setChecked(_a.getActivo());

/*		Geofence g = new Geofence.Builder()
				.setRequestId("")
				.setCircularRegion(
						entry.getValue().latitude,
						entry.getValue().longitude,
						SyncStateContract.Constants.GEOFENCE_RADIUS_IN_METERS
				)
				.setExpirationDuration(SyncStateContract.Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
						Geofence.GEOFENCE_TRANSITION_EXIT)
				.build()*/

	}

	// DB SAVE
	private void saveValores()
	{
		_a.setTexto(_txtAviso.getText().toString());
		_a.setActivo(_swtActivo.isChecked());
		Intent data = new Intent();
    	data.putExtra("aviso", _a);
		setResult(Activity.RESULT_OK, data);
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
	//______________________________________________________________________________________________
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		saveValores();
		return super.onOptionsItemSelected(item);
    }

}
