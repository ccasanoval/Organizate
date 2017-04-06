package com.cesoft.organizate;

import java.util.ArrayList;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.cesoft.organizate.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 04/02/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
class CesGeofenceStore implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>
{
	private static final String TAG = CesGeofenceStore.class.getSimpleName();
	private Context _Context;
	private GoogleApiClient _GoogleApiClient;
	private PendingIntent _PendingIntent;
	private ArrayList<Geofence> _aGeofences;

	CesGeofenceStore(Context context, ArrayList<Geofence> geofences)
	{
		_Context = context;
		_aGeofences = new ArrayList<>(geofences);
		// Build a new GoogleApiClient, specify that we want to use LocationServices by adding the API to the client,
		// specify the connection callbacks are in this class as well as the OnConnectionFailed method.
		_GoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		_GoogleApiClient.connect();
	}

	//// 4 ResultCallback<Status>
	@Override
	public void onResult(@NonNull Status result)
	{
		if(result.isSuccess())
			Log.e(TAG,"CesGeofenceStore:onResult------------------Success!");
		else if(result.hasResolution())
			Log.e(TAG,"CesGeofenceStore:onResult------------------hasResolution");
		else if(result.isCanceled())
			Log.e(TAG,"CesGeofenceStore:onResult------------------Canceled");
		else if(result.isInterrupted())
			Log.e(TAG,"CesGeofenceStore:onResult------------------Interrupted");
	}
	//// 4 OnConnectionFailedListener
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		Log.e(TAG,"CesGeofenceStore:e:Connection failed --------------------------------------------");
	}
	//// 4 ConnectionCallbacks
	@Override
	public void onConnected(Bundle connectionHint)
	{
		GeofencingRequest _GeofencingRequest;
		Log.e(TAG,"CesGeofenceStore:onConnected-----------------------------------------------------");
		if(ActivityCompat.checkSelfPermission(_Context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_Context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)return;

		// We're connected, now we need to create a GeofencingRequest with the geofences we have stored.
		if(_aGeofences.size() > 0)
		{
			_GeofencingRequest = new GeofencingRequest.Builder().addGeofences(_aGeofences).build();
			_PendingIntent = createRequestPendingIntent();
			// Submitting the request to monitor geofences.
			PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences(_GoogleApiClient, _GeofencingRequest, _PendingIntent);
			pendingResult.setResultCallback(this);// Set the result callbacks listener to this class.
		}
	}
	@Override
	public void onConnectionSuspended(int cause)
	{
		Log.e(TAG,"CesGeofenceStore:e:Connection suspended");
	}

	//______________________________________________________________________________________________
	void clear()
	{
		if(_PendingIntent != null)
			LocationServices.GeofencingApi.removeGeofences(_GoogleApiClient, _PendingIntent);
	}

	//______________________________________________________________________________________________
	// This creates a PendingIntent that is to be fired when geofence transitions take place. In this instance, we are using an IntentService to handle the transitions.
	private PendingIntent createRequestPendingIntent()
	{
		if(_PendingIntent == null)
		{
			Intent intent = new Intent(_Context, CesServiceAvisoGeo.class);
			_PendingIntent = PendingIntent.getService(_Context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		return _PendingIntent;
	}

}
