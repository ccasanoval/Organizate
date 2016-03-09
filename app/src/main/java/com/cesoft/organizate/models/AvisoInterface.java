package com.cesoft.organizate.models;


// Created by Cesar_Casanova on 09/03/2016.
////////////////////////////////////////////////////////////////////////////////////////////////////
public interface AvisoInterface
{
	public void desactivarPorHoy();

	public void setTexto(String s);
	public String getTexto();
	public void setActivo(boolean v);
	public boolean getActivo();
}
