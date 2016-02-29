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
	private float _rad;//min 100m - max 2km?
	private long _delay;

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

	///-----
	public AvisoGeo(){}
	public String toString(){return "{id="+getId()+", act="+_bActivo+", txt="+_sTexto+", _dtAct="+_dtActivo+" }";}


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
		return AvisoGeo.findAsIterator(AvisoGeo.class, "_B_ACTIVO > 0");// .findAll(Aviso.class);
	}

}
