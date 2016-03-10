package com.cesoft.organizate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

////////////////////////////////////////////////////////////////////////////////////////////////////
// Created by Cesar_Casanova on 12/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
public class AvisoGeo extends AvisoAbs
{
	protected boolean _bActivo = true;
	protected String _sTexto="";

	/*@Ignore
	protected Objeto _o;
		public Objeto getObjeto(){return _o;}
		public void setObjeto(Objeto o){_o=o;}*/
	public Objeto getObjeto()
	{
		List<Objeto> ao = Objeto.find(Objeto.class, "_AVISO_GEO = ?", getId().toString());
		if(ao.size() > 0)return ao.get(0);
		return null;
	}

	//@ Ignore
	protected Date _dtActivo;//Fecha para desactivar un dia //TODO: variable con periodo a aguardar para siguiente aviso: 1h, 1 dia...
	public void desactivarPorHoy()
	{
		_dtActivo = Calendar.getInstance().getTime();
		save();
	}
	public void setTexto(String s){_sTexto = s;}
	public String getTexto(){return _sTexto;}
	public void setActivo(boolean v){_bActivo = v;}
	public boolean getActivo(){return _bActivo;}

	//---Geolocation
	private String _id;
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
	public String toString(){return String.format("{id=%d, txt=%s, act=%b, _dtAct=%s, Pos=%f/%f:%.0f}", getId(), _sTexto, _bActivo, _dtActivo, _lat, _lon, _rad);}


	///----- PARCELABLE
	protected AvisoGeo(Parcel in)
	{
		long l = in.readLong();
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
System.err.println("SAVING AVISO GEO:------" + this);
		return super.save();
	}

	//______________________________________________________________________________________________
	public static Iterator<AvisoGeo> getActivos()
	{
		return AvisoGeo.findAsIterator(AvisoGeo.class, "_B_ACTIVO > 0");
	}
	//______________________________________________________________________________________________
	public static AvisoGeo getById(String id)
	{
		return AvisoGeo.findById(AvisoGeo.class, Long.parseLong(id));
	}

}
