package com.cesoft.organizate2.models;

import android.os.Parcelable;

// Created by Cesar_Casanova on 10/03/2016.
////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class AvisoAbs implements Parcelable
{
	public abstract void desactivarPorHoy();

	String _id;
		public String getId(){return _id;}
		public void setId(String v){_id = v;}
	boolean _bActivo = true;
		public boolean isActivo(){return _bActivo;}
		public void setActivo(boolean v){_bActivo=v;}
	String _sTexto="";
		public String getTexto(){return _sTexto;}
		public void setTexto(String v){_sTexto=v;}

}
