package com.cesoft.organizate.models;

import android.os.Parcel;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 12/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class AvisoGeo extends AvisoAbs
{
	/*@Ignore
	protected Objeto _o;
		public Objeto getObjeto(){return _o;}
		public void setObjeto(Objeto o){_o=o;}*/
	public Objeto getObjeto()
	{//TODO
		/*List<Objeto> ao = Objeto.find(Objeto.class, "_AVISO_GEO = ?", getId().toString());
		if(ao.size() > 0)return ao.get(0);*/
		return null;
	}

	//@ Ignore
	protected Date _dtActivo;//Fecha para desactivar un dia //TODO: variable con periodo a aguardar para siguiente aviso: 1h, 1 dia...
	public void desactivarPorHoy()
	{
		_dtActivo = Calendar.getInstance().getTime();
		save();
	}
	public void reactivarPorHoy()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		_dtActivo = cal.getTime();
		save();
	}

	//---Geolocation
	private double _lat, _lon;
	private float _rad = 500;//min 100m - max 2km?

	public double getLatitud(){return _lat;}
	public double getLongitud(){return _lon;}
	public float getRadio(){return _rad;}
	public void setGeoPosicion(double lat, double lon, float rad)
	{
		if(lat <= 85 && lat >= -85)
			_lat=lat;
		if(lon <= 180 && lon >= -180)
			_lon=lon;
		_rad = rad < 10 ? 10 : rad;
	}

	///-----
	public AvisoGeo(){}//NO BORRAR: Necesario para sugar
	public AvisoGeo(String s){_sTexto = s;}
	public AvisoGeo(String id, String texto, boolean activo, double lat, double lon, float rad)
	{
		_id=id; _sTexto=texto; _bActivo=activo;
		_lat=lat; _lon=lon; _rad=rad;

	}
	public String toString(){return String.format(Locale.ENGLISH, "{id=%s, txt=%s, act=%b, _dtAct=%s, Pos=%f/%f:%.0f}", getId(), _sTexto, _bActivo, _dtActivo, _lat, _lon, _rad);}


	///----- PARCELABLE
	protected AvisoGeo(Parcel in)
	{
		setId(in.readString());
		//
		_bActivo = in.readByte() > 0;
		_sTexto = in.readString();
		//
		_lat = in.readDouble();
		_lon = in.readDouble();
		_rad = in.readFloat();
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(getId());
		//
		dest.writeByte(_bActivo?(byte)1:(byte)0);
		dest.writeString(_sTexto);
		//
		dest.writeDouble(_lat);
		dest.writeDouble(_lon);
		dest.writeFloat(_rad);
	}
	@Override
	public int describeContents()
	{
		return 0;
	}
	public static final Creator<AvisoGeo> CREATOR = new Creator<AvisoGeo>()
	{
		@Override
		public AvisoGeo createFromParcel(Parcel in)
		{
			return new AvisoGeo(in);
		}
		@Override
		public AvisoGeo[] newArray(int size)
		{
			return new AvisoGeo[size];
		}
	};

	//______________________________________________________________________________________________
	public long save()
	{
System.err.println("SAVING AVISO GEO:------" + this);
		//TODO
		return -1;
		//return super.save();
	}

	//______________________________________________________________________________________________
	public static Iterator<AvisoGeo> getActivos()
	{
		//TODO
		return null;
		//return AvisoGeo.findAsIterator(AvisoGeo.class, "_B_ACTIVO > 0");
	}
	//______________________________________________________________________________________________
	public static AvisoGeo getById(String id)
	{
		//TODO
		return null;
		//return AvisoGeo.findById(AvisoGeo.class, Long.parseLong(id));
	}

}
