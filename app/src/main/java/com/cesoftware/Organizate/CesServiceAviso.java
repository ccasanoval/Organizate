package com.cesoftware.Organizate;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.IntentService;
import android.content.Intent;

import com.cesoftware.Organizate.models.Aviso;
import com.cesoftware.Organizate.models.Objeto;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

////////////////////////////////////////////////////////////////////////////////////////////////////
/// Created by Cesar_Casanova on 27/01/2016
////////////////////////////////////////////////////////////////////////////////////////////////////
//TODO: Si no hay avisos en bbdd quitar servicio, solo cuando se añada uno, activarlo
public class CesServiceAviso extends IntentService
{
	private static final long DELAY_LOAD = 5*60*1000;//TODO: ajustar
	private static final long DELAY_CHECK = 3*60*1000;
	private ArrayList<Aviso> _lista = new ArrayList<>();

	//______________________________________________________________________________________________
	public CesServiceAviso()
	{
		super("OrganizateAviso");
		//SugarContext.init(this);
	}

	//______________________________________________________________________________________________
	@Override
	protected void onHandleIntent(Intent workIntent)
	{
		try
		{
			SugarContext.init(this);
			//String dataString = workIntent.getDataString();
			long tmLoad = System.currentTimeMillis();
			long tmCheck = System.currentTimeMillis();
			while(true)
			{
System.err.println("CesServiceAviso:onHandleIntent:looping------------");
				Thread.sleep(DELAY_CHECK/2);
				if(tmLoad + DELAY_LOAD < System.currentTimeMillis())
				{
					cargarLista();
					tmLoad = System.currentTimeMillis();
				}
				if(tmCheck + DELAY_CHECK < System.currentTimeMillis())
				{
					checkAvisos();
					tmCheck = System.currentTimeMillis();
				}
			}
		}
		catch(InterruptedException e){System.err.println("CesServiceAviso:onHandleIntent:------------");}
	}

	//______________________________________________________________________________________________
	private void cargarLista()
	{
System.err.println("CesServiceAviso----cargarLista----");
		try
		{
			_lista.clear();
			Iterator<Aviso> it = Aviso.getActivos();
			while(it.hasNext())
				_lista.add(it.next());
System.err.println("CesServiceAviso---------------------cargarLista:"+_lista.size());
		}
		catch(Exception e)
		{
			System.err.println("CesServiceAviso:cargarLista:ERROR:"+e);
			//_lista.clear();
		}
	}

	//______________________________________________________________________________________________
	private void checkAvisos()
	{
System.err.println("CesServiceAviso-------checkAvisos----1");
		if(_lista == null || _lista.size() == 0)return;
		for(Aviso a : _lista)
		{
			if(a.isDueTime())
			{
System.err.println("CesServiceAviso-------checkAvisos----ACTIVA EL AVISO*****************************************************" + a.getTexto());
				Intent intent = new Intent(getBaseContext(), ActAvisoDlg.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("aviso", a);//.getTexto()
				getApplication().startActivity(intent);
			}
		}
	}
}
