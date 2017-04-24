package com.cesoft.organizate2;

import com.cesoft.organizate2.models.AvisoGeo;
import com.cesoft.organizate2.util.Log;
import com.cesoft.organizate2.util.Util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class ActAvisoGeoEdit extends AppCompatActivity implements GoogleMap.OnCameraIdleListener, OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
		//, ResultCallback<Status>
{
	private static final String TAG = ActAvisoGeoEdit.class.getSimpleName();
	private static final int DELAY_LOCATION = 60000;
	private AvisoGeo _a;
	private TextView _txtAviso;
	private Switch _swtActivo;
	private GoogleMap _Map;
	private String[] _asRadio = {"10 m", "50 m", "100 m", "200 m", "300 m", "400 m", "500 m", "750 m", "1 Km", "2 Km", "3 Km", "4 Km", "5 Km", "7.5 Km", "10 Km"};
	private int[] _adRadio = {10, 50, 100, 200, 300, 400, 500, 750, 1000, 2000, 3000, 4000, 5000, 7500, 10000};
	private Spinner _spnRadio;
	private float _radio;
	private Location _loc, _locLast;
	private TextView _lblPosicion;
	private GoogleApiClient _GoogleApiClient;
	private LocationRequest _LocationRequest;
	private Marker _marker;
	private Circle _circle;

	//______________________________________________________________________________________________
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_avisogeo_edit);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		//
		ActionBar actionBar = getSupportActionBar();
		if(actionBar!=null)actionBar.setDisplayHomeAsUpEnabled(true);
		//
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		if(fab != null)
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ActAvisoGeoEdit.this.finish();
				}
			});

		_txtAviso = (TextView) findViewById(R.id.txtAviso);
		_swtActivo = (Switch) findViewById(R.id.bActivo);
		_lblPosicion = (TextView) findViewById(R.id.lblPosicion);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, _asRadio);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_spnRadio = (Spinner) findViewById(R.id.spnRadio);
		if (_spnRadio != null) {
			_spnRadio.setAdapter(adapter);
			_spnRadio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					_radio = _adRadio[position];
					setMarker();//Para cambiar radio
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					_radio = 50;
				}
			});
		}
		ImageButton btnActPos = (ImageButton) findViewById(R.id.btnActPos);
		if (btnActPos != null)
			btnActPos.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (_locLast != null)
						setPosAviso(_locLast);
				}
			});
		//------------------------------------------------------------------------------------------
		_GoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		_GoogleApiClient.connect();
		_LocationRequest = new LocationRequest();
		_LocationRequest.setInterval(DELAY_LOCATION);
		//_LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		_LocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		pideGPS();
		//------------------------------------------------------------------------------------------
		try {
			_a = this.getIntent().getParcelableExtra(AvisoGeo.class.getName());
			setValores();
		} catch (Exception e) {
			Log.e(TAG, "onCreate:e:-----------------------------------------------------------------", e);
			this.finish();
		}
		//------------------------------------------------------------------------------------------
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopTracking();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startTracking();
	}

	//______________________________________________________________________________________________
	private void setValores() {
		_txtAviso.setText(_a.getTexto());
		_swtActivo.setChecked(_a.isActivo());
		_radio = _a.getRadio();//TODO:radio por defecto en settings
		setPosAviso(_a.getLatitud(), _a.getLongitud());
		Log.e(TAG, "---------------------------------------------------------------" + _a.getLatitud() + ", " + _a.getLongitud());
		for (int i = 0; i < _adRadio.length; i++) {
			if (_radio == _adRadio[i]) {
				_spnRadio.setSelection(i);
				break;
			}
		}
	}

	private void setPosAviso(Location loc) {
		setPosAviso(loc.getLatitude(), loc.getLongitude());
	}

	private void setPosAviso(double lat, double lon) {
		if (_loc == null) _loc = new Location("dummyprovider");
		_loc.setLatitude(lat);
		_loc.setLongitude(lon);
		_lblPosicion.setText(Util.formatLatLon(_loc.getLatitude(), _loc.getLongitude()));
		setMarker();
	}

	private void setMarker() {
		try {
			if (_Map == null) return;
			if (_marker != null) _marker.remove();
			LatLng pos = new LatLng(_loc.getLatitude(), _loc.getLongitude());
			MarkerOptions mo = new MarkerOptions()
					.position(pos)
					.title(getString(R.string.aviso))
					.snippet(_a.getTexto());

			_marker = _Map.addMarker(mo);
			_Map.moveCamera(CameraUpdateFactory.newLatLng(pos));
			_Map.animateCamera(CameraUpdateFactory.zoomTo(15));

			if (_circle != null) _circle.remove();
			_circle = _Map.addCircle(new CircleOptions()
					.center(pos)
					.radius(_radio)
					.strokeColor(Color.TRANSPARENT)
					.fillColor(0x55AA0000));//Color.BLUE
		} catch (Exception e) {
			Log.e(TAG, "setMarker:e:-------------------------------------------------", e);
		}
	}

	// DB SAVE
	private void saveValores() {
		_a.setTexto(_txtAviso.getText().toString());
		_a.setActivo(_swtActivo.isChecked());
		_a.setGeoPosicion(_loc.getLatitude(), _loc.getLongitude(), _radio);
		_a.reactivarPorHoy();
		Intent data = new Intent();
		data.putExtra(AvisoGeo.class.getName(), _a);
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
		switch(item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_user:
				saveValores();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//______________________________________________________________________________________________
	// PARA OnMapReadyCallback
	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		Log.e(TAG, "************************************  onMapReady  ************************************************");
		_Map = googleMap;
		if (_a != null && (_a.getLatitud() != 0 || _a.getLongitud() != 0))
		{
			LatLng latLng = new LatLng(_a.getLatitud(), _a.getLongitud());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
			_Map.animateCamera(cameraUpdate);
		}
		_Map.getUiSettings().setZoomControlsEnabled(true);
		_Map.getUiSettings().setMapToolbarEnabled(true);
		_Map.getUiSettings().setAllGesturesEnabled(true);
		_Map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
		{
			@Override
			public void onMapClick(LatLng latLng)
			{
				setPosAviso(latLng.latitude, latLng.longitude);
			}
		});
		setPosAviso(_a.getLatitud(), _a.getLongitud());
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			pideGPS();
		else
			_Map.setMyLocationEnabled(true);
	}

	//______________________________________________________________________________________________
	//// 4 ConnectionCallbacks
	@Override
	public void onConnected(Bundle bundle)
	{
		startTracking();
	}
	@Override
	public void onConnectionSuspended(int i){}
	private void startTracking()
	{
		if(_GoogleApiClient != null && _GoogleApiClient.isConnected())
		{
//Log.e(TAG, "----------------_GoogleApiClient.isConnected()="+_GoogleApiClient.isConnected());
			if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)return;
				LocationServices.FusedLocationApi.requestLocationUpdates(_GoogleApiClient, _LocationRequest, this);
		}
	}
	private void stopTracking()
	{
		if(_GoogleApiClient != null && _GoogleApiClient.isConnected())
		LocationServices.FusedLocationApi.removeLocationUpdates(_GoogleApiClient, this);
	}
	//______________________________________________________________________________________________
	//// 4 OnConnectionFailedListener
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult){}
	//______________________________________________________________________________________________
	//// 4 LocationListener
	@Override
	public void onLocationChanged(Location location)
	{
//Log.e(TAG, String.format(Locale.ENGLISH, "********************** %f, %f   -  %f : %f", location.getLatitude(), location.getLongitude(), _loc.getLatitude(), _loc.getLongitude()));
		_locLast = location;
		if(_loc.getLatitude() == 0 && _loc.getLongitude() == 0)
			setPosAviso(_locLast);
	}
	//______________________________________________________________________________________________
	//// 4 ResultCallback
	//@Override public void onResult(@NonNull Status status){}


	@Override
	public void onCameraIdle()
	{
		_Map.getCameraPosition();
		//gooleMap.setOnCameraIdleListener(mClusterManager);
		if(_a != null && (_a.getLatitud() != 0 || _a.getLongitud() != 0))
			_Map.addCircle(
				new CircleOptions()
					.center(new LatLng(_a.getLatitud(), _a.getLongitud()))
					.radius(_a.getRadio())
					.fillColor(0x40ff0000)
					.strokeColor(Color.TRANSPARENT).strokeWidth(2));
	}

	//----------------------------------------------------------------------------------------------
	public void pideGPS()
	{
		//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ! ha.canAccessLocation())activarGPS(true);
		int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
		if(permissionCheck == PackageManager.PERMISSION_DENIED)
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 6969);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
	{
		if(requestCode == 6969)
			if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				try{_Map.setMyLocationEnabled(true);}catch(SecurityException ignore){}
	}
	//______________________________________________________________________________________________
	/*private void pideGPS()
	{
		//https://developers.google.com/android/reference/com/google/android/gms/location/SettingsApi
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(_LocationRequest);
		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(_GoogleApiClient, builder.build());

		result.setResultCallback(new ResultCallback<LocationSettingsResult>()
		{
     		@Override
     		public void onResult(@NonNull LocationSettingsResult result)
			{
         		final Status status = result.getStatus();
         		//final LocationSettingsStates le =
						result.getLocationSettingsStates();
         		switch(status.getStatusCode())
				{
             	case LocationSettingsStatusCodes.SUCCESS:
					Log.e(TAG, "LocationSettingsStatusCodes.SUCCESS");
					// All location settings are satisfied. The client can initialize location requests here.
					break;
				case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
					Log.e(TAG, "LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
					// Location settings are not satisfied. But could be fixed by showing the user a dialog.
					break;
				case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
					Log.e(TAG, "LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
					// Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
					break;
				}
			}
		});
	}*/
}
