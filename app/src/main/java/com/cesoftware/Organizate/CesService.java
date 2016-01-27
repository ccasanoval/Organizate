package com.cesoftware.Organizate;

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
public class CesService extends IntentService
{
	private static final long DELAY_LOAD = 15*60*1000;
	private static final long DELAY_CHECK = 15*60*1000;
	private ArrayList<Aviso> _lista;

	//______________________________________________________________________________________________
	public CesService()
	{
		super("Organizate");
		SugarContext.init(this);
	}

	//______________________________________________________________________________________________
	@Override
	protected void onHandleIntent(Intent workIntent)
	{
		try
		{
			Thread.sleep(DELAY_CHECK/2);

			//String dataString = workIntent.getDataString();
			long tmLoad = System.currentTimeMillis();
			long tmCheck = System.currentTimeMillis();
			while(true)
			{
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
		catch(InterruptedException e){}
	}

	//______________________________________________________________________________________________
	private void cargarLista()
	{
		System.err.println("CesService----cargarLista----");
		try
		{
			Iterator<Aviso> it = Aviso.findAll(Aviso.class);
			while(it.hasNext())
				_lista.add(it.next());
		}
		catch(Exception e)
		{
			_lista = new ArrayList<>();
		}
	}

	//______________________________________________________________________________________________
	private void checkAvisos()
	{
		System.err.println("CesService----checkAvisos----");
		if(_lista == null || _lista.size() == 0)return;
		for(Aviso a : _lista)
		{
			Calendar now = Calendar.getInstance();

			byte[] aMeses = a.getMeses();
			byte[] aDiasMes = a.getDiasMes();
			byte[] aDiasSemana = a.getDiasSemana();
			byte[] aHoras = a.getHoras();
			byte[] aMinutos = a.getMinutos();

			if(aMeses.length > 0 && aMeses[0] != Aviso.TODO)
			{
				for(byte mes : aMeses)
					if(now.get(Calendar.MONTH) == mes)
						break;
				return;
			}
			if(aDiasMes.length > 0 && aDiasMes[0] != Aviso.TODO)
			{
				for(byte diaMes : aDiasMes)
					if(now.get(Calendar.DAY_OF_MONTH) == diaMes)
						break;
				return;
			}
			if(aDiasSemana.length > 0 && aDiasSemana[0] != Aviso.TODO)
			{
				for(byte diaSemana : aDiasSemana)
					if(now.get(Calendar.DAY_OF_WEEK) == diaSemana)
						break;
				return;
			}
			//TODO: hacer algo para que si no especifico la hora no se avisa cada 5 min...
			//TODO: hacer algo para desactivar la alrma: On Off & Off in this day...
			if(aHoras.length > 0 && aHoras[0] != Aviso.TODO)
			{
				for(byte hora : aHoras)
					if(now.get(Calendar.HOUR_OF_DAY) == hora)
						break;
				return;
			}
			if(aMinutos.length > 0 && aMinutos[0] != Aviso.TODO)
			{
				for(byte minuto : aMinutos)
					if(now.get(Calendar.MINUTE) == minuto)
						break;
				return;
			}

			System.err.println("CesService----checkAvisos----ACTIVA EL AVISO");
		}
	}
}
