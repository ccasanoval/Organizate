package com.cesoft.organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 12/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class AvisoGeo extends SugarRecord implements Parcelable
{
	private boolean _bActivo = true;
	private String _sTexto="";

	//Geolocation
	private String _id;
	private double _lat, _lon;
	private float _rad = 500;//min 100m - max 2km?
	private long _delay;//TODO:remove

	//TODO: variable con periodo a aguardar para siguiente aviso: 1h, 1 dia...
	//@ Ignore
	private Date _dtActivo;//Fecha para desactivar un dia
	public void desactivarPorHoy()
	{
		_dtActivo = Calendar.getInstance().getTime();
		save();
	}

	public void setTexto(String s){_sTexto = s;}
	public String getTexto(){return _sTexto;}

	public void setActivo(boolean v){_bActivo = v;}
	public boolean getActivo(){return _bActivo;}

	public double getLatitud(){return _lat;}
	public double getLongitud(){return _lon;}
	public float getRadio(){return _rad;}
	public void setGeoPosicion(double lat, double lon, float rad)
	{//TODO:Check valid
		_lat=lat;
		_lon=lon;
		_rad=rad<10?10:rad;
	}

	///-----
	public AvisoGeo(){}
	public String toString(){return String.format("{id=%d, act=%b, txt=%s, _dtAct=%s, Pos=%f/%f:%.0f}", getId(), _bActivo, _sTexto, _dtActivo, _lat, _lon, _rad);}


	///----- PARCELABLE
	protected AvisoGeo(Parcel in)
	{
		long l;
		int i;
		byte[] ai;
		//
		l = in.readLong();
		if(l >= 0)setId(l);
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
		int[] ai;
		dest.writeLong(getId() != null ? getId() : -1);
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
//System.err.println("SAVING AVISO GEO:------" + this);
		return super.save();
	}

	//______________________________________________________________________________________________
	public static Iterator<AvisoGeo> getActivos()
	{
		return AvisoGeo.findAsIterator(AvisoGeo.class, "_B_ACTIVO > 0");// .findAll(Aviso.class);
	}

	//______________________________________________________________________________________________
	public static AvisoGeo getById(String id)
	{
		return AvisoGeo.findById(AvisoGeo.class, Long.parseLong(id));
	}
}
