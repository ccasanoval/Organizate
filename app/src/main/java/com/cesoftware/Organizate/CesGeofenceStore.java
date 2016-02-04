package com.cesoftware.Organizate;

import java.util.ArrayList;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 04/02/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesGeofenceStore implements ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<Status>, LocationListener
{
	private final String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private GoogleApiClient mGoogleApiClient;
	private PendingIntent mPendingIntent;
	private ArrayList<Geofence> mGeofences;
	private GeofencingRequest mGeofencingRequest;
	private LocationRequest mLocationRequest;

	/**
	 * Constructs a new GeofenceStore.
	 *
	 * @param context The context to use.
	 * @param geofences List of geofences to monitor.
	 */
	public CesGeofenceStore(Context context, ArrayList<Geofence> geofences)
	{
		mContext = context;
		mGeofences = new ArrayList<Geofence>(geofences);
		mPendingIntent = null;
		// Build a new GoogleApiClient, specify that we want to use LocationServices
		// by adding the API to the client, specify the connection callbacks are in
		// this class as well as the OnConnectionFailed method.
		mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		// This is purely optional and has nothing to do with geofencing.
		// I added this as a way of debugging.
		// Define the LocationRequest.
		mLocationRequest = new LocationRequest();
		// We want a location update every 10 seconds.
		mLocationRequest.setInterval(10000);
		// We want the location to be as accurate as possible.
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mGoogleApiClient.connect();
	}

	@Override
	public void onResult(Status result)
	{
		if(result.isSuccess())
		{
			Log.v(TAG, "Success!");
		} else if(result.hasResolution())
		{
			// TODO Handle resolution
		} else if(result.isCanceled())
		{
			Log.v(TAG, "Canceled");
		} else if(result.isInterrupted())
		{
			Log.v(TAG, "Interrupted");
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		Log.v(TAG, "Connection failed.");
	}

	@Override
	public void onConnected(Bundle connectionHint)
	{
		// We're connected, now we need to create a GeofencingRequest with the geofences we have stored.
		mGeofencingRequest = new GeofencingRequest.Builder().addGeofences(mGeofences).build();
		mPendingIntent = createRequestPendingIntent();
		// This is for debugging only and does not affect geofencing.
		if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			&& ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
		{
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation for ActivityCompat#requestPermissions for more details.
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		// Submitting the request to monitor geofences.
		PendingResult<Status> pendingResult = LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, mGeofencingRequest, mPendingIntent);

		// Set the result callbacks listener to this class.
		pendingResult.setResultCallback(this);
	}

	@Override
	public void onConnectionSuspended(int cause)
	{
		Log.v(TAG, "Connection suspended.");
	}

	/**
	 * This creates a PendingIntent that is to be fired when geofence transitions take place. In this instance, we are using an IntentService to handle the transitions.
	 *
	 * @return A PendingIntent that will handle geofence transitions.
	 */
	private PendingIntent createRequestPendingIntent()
	{
		if(mPendingIntent == null)
		{
			Log.v(TAG, "Creating PendingIntent");
			Intent intent = new Intent(mContext, CesServiceAvisoGeo.class);
			mPendingIntent = PendingIntent.getService(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		return mPendingIntent;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		Log.v(TAG, "Location Information\n"
				+ "==========\n"
				+ "Provider:\t" + location.getProvider() + "\n"
				+ "Lat & Long:\t" + location.getLatitude() + ", "
				+ location.getLongitude() + "\n"
				+ "Altitude:\t" + location.getAltitude() + "\n"
				+ "Bearing:\t" + location.getBearing() + "\n"
				+ "Speed:\t\t" + location.getSpeed() + "\n"
				+ "Accuracy:\t" + location.getAccuracy() + "\n");
	}
}
