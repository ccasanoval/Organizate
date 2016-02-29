package com.cesoft.organizate;

import java.util.ArrayList;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
		mContext = context;
		mGeofences = new ArrayList<Geofence>(geofences);
		mPendingIntent = null;
		// Build a new GoogleApiClient, specify that we want to use LocationServices by adding the API to the client,
		// specify the connection callbacks are in this class as well as the OnConnectionFailed method.
		mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
		mLocationRequest = new LocationRequest();// This is purely optional and has nothing to do with geofencing. I added this as a way of debugging. Define the LocationRequest.
		mLocationRequest.setInterval(10000);// We want a location update every 10 seconds.
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);// We want the location to be as accurate as possible.
		mGoogleApiClient.connect();
	}

	@Override
	public void onResult(@NonNull Status result)
	{
		if(result.isSuccess())
		{
			Log.v(TAG, "Success!");
		}
		else if(result.hasResolution())
		{
			// TODO Handle resolution
			Log.v(TAG, "hasResolution");
		}
		else if(result.isCanceled())
		{
			Log.v(TAG, "Canceled");
		}
		else if(result.isInterrupted())
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



	//---------------------



	// The SharedPreferences object in which geofences are stored.
	private final SharedPreferences mPrefs;
	// The name of the SharedPreferences.
	private static final String SHARED_PREFERENCES = "SharedPreferences";

	/**
	 * Create the SharedPreferences storage with private access only.
	 */
	public CesGeofenceStore(Context context)
	{
		mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
	}


	public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
	public static final String KEY_LATITUDE = "com.example.wearable.geofencing.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.example.wearable.geofencing.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.example.wearable.geofencing.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION = "com.example.wearable.geofencing.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE = "com.example.wearable.geofencing.KEY_TRANSITION_TYPE";
	public CesGeofence getGeofence(String id)
	{
		// Get the latitude for the geofence identified by id, or INVALID_FLOAT_VALUE if it doesn't
		// exist (similarly for the other values that follow).
		double lat = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE), INVALID_FLOAT_VALUE);
		double lng = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), INVALID_FLOAT_VALUE);
		float radius = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS), INVALID_FLOAT_VALUE);
		long expirationDuration = mPrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION), INVALID_LONG_VALUE);
		int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE), INVALID_INT_VALUE);
		// If none of the values is incorrect, return the object.
		if (lat != INVALID_FLOAT_VALUE
				&& lng != INVALID_FLOAT_VALUE
				&& radius != INVALID_FLOAT_VALUE
				&& expirationDuration != INVALID_LONG_VALUE
				&& transitionType != INVALID_INT_VALUE)
		{
			return new CesGeofence(id, lat, lng, radius, expirationDuration, transitionType);
		}
		// Otherwise, return null.
		return null;
	}

	/**
	 * Save a geofence.
	 * @param geofence The SimpleGeofence with the values you want to save in SharedPreferences.
	 */
	public void setGeofence(String id, CesGeofence geofence)
	{
		// Get a SharedPreferences editor instance. Among other things, SharedPreferences
		// ensures that updates are atomic and non-concurrent.
		SharedPreferences.Editor prefs = mPrefs.edit();
		// Write the Geofence values to SharedPreferences.
		prefs.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) geofence.getLatitude());
		prefs.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) geofence.getLongitude());
		prefs.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), geofence.getRadius());
		prefs.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
				geofence.getExpirationDuration());
		prefs.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
				geofence.getTransitionType());
		// Commit the changes.
		prefs.commit();
	}

	/**
	 * Remove a flattened geofence object from storage by removing all of its keys.
	 */
	public void clearGeofence(String id)
	{
		SharedPreferences.Editor prefs = mPrefs.edit();
		prefs.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
		prefs.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
		prefs.remove(getGeofenceFieldKey(id, KEY_RADIUS));
		prefs.remove(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION));
		prefs.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
		prefs.commit();
	}

	/**
	 * Given a Geofence object's ID and the name of a field (for example, KEY_LATITUDE), return
	 * the key name of the object's values in SharedPreferences.
	 * @param id The ID of a Geofence object.
	 * @param fieldName The field represented by the key.
	 * @return The full key name of a value in SharedPreferences.
	 */
	public static final String KEY_PREFIX = "com.example.wearable.geofencing.KEY";
	private String getGeofenceFieldKey(String id, String fieldName)
	{
		return KEY_PREFIX + "_" + id + "_" + fieldName;
	}

}
