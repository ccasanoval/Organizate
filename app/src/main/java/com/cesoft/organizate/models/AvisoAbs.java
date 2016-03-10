package com.cesoft.organizate.models;

import android.os.Parcelable;
import com.orm.SugarRecord;

// Created by Cesar_Casanova on 10/03/2016.
////////////////////////////////////////////////////////////////////////////////////////////////////
public abstract class AvisoAbs extends SugarRecord implements Parcelable
{
	public abstract void desactivarPorHoy();

	public abstract void setTexto(String s);
	public abstract String getTexto();
	public abstract void setActivo(boolean v);
	public abstract boolean getActivo();
}
