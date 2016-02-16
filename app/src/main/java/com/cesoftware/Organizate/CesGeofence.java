package com.cesoftware.Organizate;

import com.google.android.gms.location.Geofence;

////////////////////////////////////////////////////////////////////////////////////////////////////
public class CesGeofence
{
	private final String _Id;
	private final double _Latitude;
	private final double _Longitude;
	private final float _Radius;
	private long _ExpirationDuration;
	private int _TransitionType;

	/**
	 * @param geofenceId The Geofence's request ID.
	 * @param latitude Latitude of the Geofence's center in degrees.
	 * @param longitude Longitude of the Geofence's center in degrees.
	 * @param radius Radius of the geofence circle in meters.
	 * @param expiration Geofence expiration duration.
	 * @param transition Type of Geofence transition.
	 */
	public CesGeofence(String geofenceId, double latitude, double longitude, float radius, long expiration, int transition)
	{
		this._Id = geofenceId;
		this._Latitude = latitude;
		this._Longitude = longitude;
		this._Radius = radius;
		this._ExpirationDuration = expiration;
		this._TransitionType = transition;
	}

	public String getId(){return _Id;}
	public double getLatitude(){return _Latitude;}
	public double getLongitude(){return _Longitude;}
	public float getRadius(){return _Radius;}
	public long getExpirationDuration(){return _ExpirationDuration;}
	public int getTransitionType(){return _TransitionType;}

	/**
	 * Creates a Location Services Geofence object from a CesGeofence.
	 * @return A Geofence object.
	 */
	public Geofence toGeofence()
	{
		return new Geofence.Builder()
			.setRequestId(_Id)
			.setTransitionTypes(_TransitionType)
			.setCircularRegion(_Latitude, _Longitude, _Radius)
			.setExpirationDuration(_ExpirationDuration)
			.build();
	}
}
